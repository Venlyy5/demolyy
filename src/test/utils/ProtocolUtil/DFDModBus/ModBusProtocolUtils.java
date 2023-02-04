package com.dfd.platform.utils.protocol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dfd.common.assembly.redis.IotRedisUtil;
import com.dfd.platform.constants.ModBusExceptionEnum;
import com.dfd.platform.constants.ProtocolConstant;
import com.dfd.platform.model.IotMstDevice;
import com.dfd.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

import static com.dfd.platform.utils.protocol.HexUtils.*;

/**
 * @author LiYangYang
 * @date 2022/12/27
 * TCP: 事务ID + 协议标识 + 后续字节长度 + SlaveID + 功能码 + 数据(根据功能码)
 * RTU: SlaveID + 功能码 + 数据(根据功能码) + CRC
 * 错误帧: 从机地址 + [命令码+0x80] + 错误码 + CRC
 */
public class ModBusProtocolUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(ModBusProtocolUtils.class);
    /**--------------------------------------
     * 生成 模块采集请求报文
     * @param deviceConnectJson  设备连接信息
     * @param moduleModelJson 模块模型
     * @return 请求报文集合
     */
    public static List<String> getModuleCollectRequest(JSONObject deviceConnectJson, JSONObject moduleModelJson){
        LOGGER.info("$$$$$$$$$$ [ModBus]生成模块采集报文, 入参deviceConnectJson:{}", deviceConnectJson);

        Integer moduleType = moduleModelJson.getInteger(ProtocolConstant.MODULE_TYPE);
        String moduleStartId = moduleModelJson.getString(ProtocolConstant.MODULE_START_ID);
        String moduleEndId = moduleModelJson.getString(ProtocolConstant.MODULE_END_ID);
        int moduleWordLength = HexUtils.hexString2Int(moduleEndId) - HexUtils.hexString2Int(moduleStartId);
        if (moduleWordLength==0){
            LOGGER.info("$$$$$$$$$$ [ModBus]生成模块采集报文 该模块无功能点可采集");
            return null;
        }

        ModBusEntity modBusEntity = new ModBusEntity();
        modBusEntity.setUnitId(numberToHex(deviceConnectJson.getInteger(ProtocolConstant.SLAVE_ADDR), 1));
        ArrayList<String> dataList = new ArrayList<>();

        // 选择模块寄存器类型
        switch (moduleType){
            case ProtocolConstant.COIL:
                modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_READ_COILS);
                dataList.add(moduleStartId + HexUtils.numberToHex(moduleWordLength,2));
                break;

            case ProtocolConstant.DISCRETE_INPUT:
                modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_READ_DISCRETE_INPUTS);
                dataList.add(moduleStartId + HexUtils.numberToHex(moduleWordLength,2));
                break;

            case ProtocolConstant.HOLDING_REGISTER:
                modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_READ_HOLDING_REGISTERS);

                if (moduleWordLength>124){
                    int start = HexUtils.hexString2Int(moduleStartId);
                    for (int i = 0; i < moduleWordLength / 124; i++) {
                        dataList.add(HexUtils.numberToHex(start, 2) + HexUtils.numberToHex(124, 2));
                        start += 124;
                    }
                    dataList.add(HexUtils.numberToHex(start, 2)  + HexUtils.numberToHex(moduleWordLength % 124, 2));
                }else {
                    dataList.add(moduleStartId + HexUtils.numberToHex(moduleWordLength,2));
                }
                break;

            case ProtocolConstant.INPUT_REGISTER:
                modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_READ_INPUT_REGISTERS);

                if (moduleWordLength>124){
                    int start = HexUtils.hexString2Int(moduleStartId);
                    for (int i = 0; i < moduleWordLength / 124; i++) {
                        dataList.add(HexUtils.numberToHex(start, 2) + HexUtils.numberToHex(124, 2));
                        start += 124;
                    }
                    dataList.add(HexUtils.numberToHex(start, 2)  + HexUtils.numberToHex(moduleWordLength % 124, 2));
                }else {
                    dataList.add(moduleStartId + HexUtils.numberToHex(moduleWordLength,2));
                }
                break;

            default:
                LOGGER.info("$$$$$$$$$$ [ModBus]Unsupported Register Type");
                break;
        }

        // PDU
        ArrayList<ModBusEntity> modBusEntityList = new ArrayList<>();
        if (dataList.size()==1){
            modBusEntity.setData(dataList.get(0));
            modBusEntityList.add(modBusEntity);
        }else {
            for (int i = 0; i < dataList.size(); i++) {
                ModBusEntity modBusEntityMultiple = new ModBusEntity();
                modBusEntityMultiple.setUnitId(modBusEntity.getUnitId());
                modBusEntityMultiple.setFunctionCode(modBusEntity.getFunctionCode());
                modBusEntityMultiple.setData(dataList.get(i));
                modBusEntityList.add(modBusEntityMultiple);
            }
        }

        // 报文集合
        ArrayList<String> contentList = new ArrayList<>();
        // TCP / RTU
        if (ProtocolConstant.RTU.equals(deviceConnectJson.getString(ProtocolConstant.MESSAGE_TYPE))){
            for (int i = 0; i < modBusEntityList.size(); i++) {
                ModBusEntity modBusEntityRTU = modBusEntityList.get(i);
                modBusEntityRTU.setCrc(CRC16Utils.getCRC(modBusEntityRTU.toHexString()));
                contentList.add(modBusEntityRTU.toHexString());
            }
        }else {
            for (int i = 0; i < modBusEntityList.size(); i++) {
                ModBusEntity modBusEntityTCP = modBusEntityList.get(i);
                modBusEntityTCP.setTransactionId(numberToHex(new Random().nextInt(65535), 2));
                modBusEntityTCP.setProtocolMark("0000");
                modBusEntityTCP.setLength("0006");
                contentList.add(modBusEntityTCP.toHexString());
            }
        }
        LOGGER.info("$$$$$$$$$$ [ModBus]生成模块采集报文 出参List={}", contentList);
        return contentList;
    }

    /**-----------------
     * 解析 模块采集响应
     * {
     * 	"profile":{
     * 		"deviceId": "test_device_1",
     *   },
     * 	"method":"direct.set",
     * 	"time":1234567890000,
     * 	"messageId":"123456789",
     * 	"version": "1.0",
     * 	"param":{
     * 		"content":{
     * 			"request":"48f10000000601040000007c,123bbbbb0000,123ccccc0000",
     * 			"response":"123aaaaa**0000,123bbbbb**0000,123ccccc**0000"
     *        }
     *   }
     * }
     * @param nettyJson
     * @return DFDJson
     */
    public static JSONObject analyzingCollectResponse(JSONObject nettyJson, String moduleMark){
        LOGGER.info("$$$$$$$$$$ [ModBus]解析采集响应 入参NettyJson={}, ModuleMark={}", nettyJson, moduleMark);

        JSONObject DFDJson = new JSONObject();
        JSONObject profileNettyJson = nettyJson.getJSONObject(ProtocolConstant.PROFILE);
        String deviceId = profileNettyJson.getString(ProtocolConstant.DEVICE_ID);
        JSONObject paramNettyJson = nettyJson.getJSONObject("param");

        JSONObject contentNettyJson = paramNettyJson.getJSONObject(ProtocolConstant.CONTENT);
        String response = contentNettyJson.getString(ProtocolConstant.RESPONSE);
        if (StringUtils.isEmpty(response) || response.contains("null") || response.contains("NULL")){
            return null;
        }

        //从Redis获取模块模型
        IotMstDevice device = IotRedisUtil.getHash("deviceInfo", deviceId);
        if (null==device){
            LOGGER.info("$$$$$$$$$$ [ERROR]ModBus Redis无此设备信息, deviceId={}",deviceId);
            return null;
        }
        JSONObject deviceConnectJson = JSONObject.parseObject(device.getDeviceConnectInfo());
        JSONObject moduleModelJson = JSONObject.parseObject(String.valueOf(IotRedisUtil.hGet("modelThingModuleInfo:" + device.getModelCode(), moduleMark)));
        if (null==moduleModelJson){
            LOGGER.info("$$$$$$$$$$ [ERROR]ModBus Redis无此模块模型信息, moduleMark={}", moduleMark);
            return null;
        }

        JSONArray functions = moduleModelJson.getJSONArray(ProtocolConstant.FUNCTIONS);
        String messageType = deviceConnectJson.getString(ProtocolConstant.MESSAGE_TYPE);
        String resultResponse = "";
        String[] split = response.split(",");
        for (int i = 0; i < split.length; i++) {
            ModBusEntity modBusEntity = contentHex2ModBusEntity(split[i], messageType);
            // 异常返回
            if (HexUtils.hexString2Int(modBusEntity.getFunctionCode()) > 128){
                JSONObject exceptionJson = new JSONObject();
                exceptionJson.put("code", modBusEntity.getData());
                exceptionJson.put("description", ModBusExceptionEnum.getByCode(modBusEntity.getData()).getDescription());
                DFDJson.put("exception", exceptionJson);
                LOGGER.info("$$$$$$$$$$ [ModBus Exception]解析采集响应 DFDJson={}", DFDJson);
                return DFDJson;
            }
            // 正常拼接数据
            resultResponse += modBusEntity.getData().substring(2);
        }

        LOGGER.info("$$$$$$$$$$ [ModBus] DataHex Result Response={}", resultResponse);

        String[] hexArr = HexUtils.formatterHex(resultResponse).split(" ");
        // 组装"data"
        JSONObject dataJson = new JSONObject(new LinkedHashMap<>());
        int moduleType = moduleModelJson.getInteger(ProtocolConstant.MODULE_TYPE);
        switch (moduleType){
            case ProtocolConstant.COIL:
            case ProtocolConstant.DISCRETE_INPUT:
                String boolResult = "";
                for (int i = 0; i < hexArr.length; i++) {
                    StringBuilder reverse = new StringBuilder(HexUtils.hexString2binaryString(hexArr[i])).reverse();
                    boolResult += reverse.toString();
                }

                LOGGER.info("$$$$$$$$$$ [ModBus]Boolean Result={}", boolResult);

                for (int i = 0; i < functions.size(); i++) {
                    JSONObject function = (JSONObject)functions.get(i);
                    dataJson.put(function.getString(ProtocolConstant.FUNCTION_MARK), boolResult.charAt(i));
                }
                break;

            case ProtocolConstant.HOLDING_REGISTER:
            case ProtocolConstant.INPUT_REGISTER:

                int offset = 0;
                for (int i = 0; i < functions.size() && offset < hexArr.length; i++) {
                    JSONObject function = (JSONObject)functions.get(i);
                    String dataType = function.getString(ProtocolConstant.DATA_TYPE);
                    String functionMark = function.getString(ProtocolConstant.FUNCTION_MARK);

                    switch (dataType){
                        case ProtocolConstant.DATA_TYPE_ENUM:
                            String enumHex = hexArr[offset] + hexArr[offset+1];
                            int enumValue = Integer.valueOf(enumHex, 16);

                            JSONArray dataInfo = function.getJSONArray(ProtocolConstant.DATA_INFO);
                            for (int k = 0; k < dataInfo.size(); k++) {
                                JSONObject enumJson = (JSONObject)dataInfo.get(k);
                                Integer enumIntValue = Integer.valueOf(enumJson.getString("value"));
                                if (enumIntValue==enumValue){
                                    dataJson.put(functionMark, enumValue);
                                    break;
                                }
                            }
                            offset += 2;
                            break;

                        case ProtocolConstant.DATA_TYPE_INT16:
                            String int16Hex = hexArr[offset] + hexArr[offset+1];
                            dataJson.put(functionMark, HexUtils.hexStringToShort(int16Hex));
                            offset += 2;
                            break;

                        case ProtocolConstant.DATA_TYPE_UINT16:
                            String uint16Hex = hexArr[offset] + hexArr[offset+1];
                            dataJson.put(functionMark, Integer.parseInt(uint16Hex, 16));
                            offset += 2;
                            break;

                        case ProtocolConstant.DATA_TYPE_INT32:
                            String int32Hex = "";
                            for (int j = 0; j < 4; j++) {
                                int32Hex += hexArr[offset+j];
                            }
                            dataJson.put(functionMark, hexString2Int(int32Hex));
                            offset += 4;
                            break;

                        case ProtocolConstant.DATA_TYPE_UINT32:
                            String uint32Hex = "";
                            for (int j = 0; j < 4; j++) {
                                uint32Hex += hexArr[offset+j];
                            }
                            dataJson.put(functionMark, Long.parseLong(uint32Hex, 16));
                            offset += 4;
                            break;

                        case ProtocolConstant.DATA_TYPE_FLOAT:
                            String floatHex = "";
                            for (int j = 3; j >= 0; j--) {
                                floatHex += hexArr[offset+j];
                            }
                            dataJson.put(functionMark, HexUtils.hexString2Float(floatHex));
                            offset += 4;
                            break;
                        default:
                            LOGGER.info("$$$$$$$$$$ [ModBus]Unsupported Data Type={}", dataType);
                            break;
                    }
                }
                break;

            default:
                LOGGER.info("$$$$$$$$$$ [ModBus]Unsupported Register Type={}", moduleType);
                break;
        }

        JSONObject paramDFDJson = new JSONObject();
        paramDFDJson.put(ProtocolConstant.CMD, ProtocolConstant.CMD_POST);
        paramDFDJson.put(ProtocolConstant.MODULE_MARK, moduleMark);
        paramDFDJson.put(ProtocolConstant.DATA, dataJson);
        DFDJson.put(ProtocolConstant.PARAMS, paramDFDJson);

        profileNettyJson.put(ProtocolConstant.PROJECT_ID, device.getProjectId());
        DFDJson.put(ProtocolConstant.PROFILE, profileNettyJson);
        DFDJson.put(ProtocolConstant.METHOD, nettyJson.getString(ProtocolConstant.METHOD));
        DFDJson.put(ProtocolConstant.MESSAGE_ID, nettyJson.getString(ProtocolConstant.MESSAGE_ID));
        DFDJson.put(ProtocolConstant.VERSION, "1.0");
        DFDJson.put(ProtocolConstant.TIME, nettyJson.getLongValue(ProtocolConstant.TIME));
        //DFDJson.put(ProtocolConstant.CONTENT, contentNettyJson);

        LOGGER.info("$$$$$$$$$$ [ModBus]解析采集响应 出参DFDJson={}", DFDJson);
        return DFDJson;
    }

    /**-------------------------
     * 生成 接口读写请求
     * @param interfaceJson 下发Json
     * @return RequestString
     */
    public static String getInterfaceRequest(JSONObject interfaceJson, JSONObject deviceConnectJson, JSONObject moduleModelJson){
        LOGGER.info("$$$$$$$$$$ [ModBus]生成接口请求报文 入参interfaceJson={}, deviceConnectJson={}", interfaceJson, deviceConnectJson);
        String startId = interfaceJson.getString("startId");
        JSONObject params = interfaceJson.getJSONObject(ProtocolConstant.PARAMS);
        String cmd = params.getString(ProtocolConstant.CMD);

        // String moduleMark = params.getString("moduleMark");
        // JSONObject profile = interfaceJson.getJSONObject("profile");
        // String deviceId = profile.getString("deviceId");
        //从Redis获取模块模型
        // IotMstDevice device = IotRedisUtil.getHash("deviceInfo", deviceId);
        // if (null==device){
        //     LOGGER.info("$$$$$$$$$$$ [ERROR]ModBus Redis无此设备信息，deviceId：{}",deviceId);
        //     return null;
        // }
        // JSONObject deviceConnectJson = JSONObject.parseObject(device.getDeviceConnectInfo());
        // JSONObject moduleModelJson = JSONObject.parseObject(String.valueOf(IotRedisUtil.hGet("modelThingModuleInfo:" + device.getModelCode(), moduleMark)));
        // if (null==moduleModelJson){
        //     LOGGER.info("$$$$$$$$$$$ [ERROR]ModBus Redis无此模块模型信息 , moduleMark:{}", moduleMark);
        //     return null;
        // }
        // JSONObject deviceConnectJson = Test_DFDBus.getDeviceConnectInfo();
        // JSONObject moduleModelJson = Test_DFDBus.getModuleModel();

        String messageType = deviceConnectJson.getString(ProtocolConstant.MESSAGE_TYPE);
        int moduleType = moduleModelJson.getInteger(ProtocolConstant.MODULE_TYPE);
        JSONArray functions = moduleModelJson.getJSONArray(ProtocolConstant.FUNCTIONS);
        int idx = getFunctionIndexByStartId(functions, startId);
        if (idx == -1){
            LOGGER.info("$$$$$$$$$$ [ModBus]生成接口请求报文 起始地址:{} 的功能点不存在",startId);
            return null;
        }

        ModBusEntity modBusEntity = new ModBusEntity();
        modBusEntity.setUnitId(HexUtils.numberToHex(deviceConnectJson.getInteger(ProtocolConstant.SLAVE_ADDR),1));
        switch (cmd){
            // Read Request
            case ProtocolConstant.CMD_GET:
                JSONArray paramsDataGet = params.getJSONArray(ProtocolConstant.DATA);
                int total = paramsDataGet.size();
                switch (moduleType){
                    case ProtocolConstant.COIL:
                        modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_READ_COILS);
                        modBusEntity.setData(startId + HexUtils.numberToHex(total,2));
                        break;

                    case ProtocolConstant.DISCRETE_INPUT:
                        modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_READ_DISCRETE_INPUTS);
                        modBusEntity.setData(startId + HexUtils.numberToHex(total,2));
                        break;

                    case ProtocolConstant.HOLDING_REGISTER:
                        modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_READ_HOLDING_REGISTERS);
                        modBusEntity.setData(startId + HexUtils.numberToHex(getFunctionWordLength(functions, total, idx),2));
                        break;

                    case ProtocolConstant.INPUT_REGISTER:
                        modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_READ_INPUT_REGISTERS);
                        modBusEntity.setData(startId + HexUtils.numberToHex(getFunctionWordLength(functions, total, idx),2));
                        break;
                }

                // TCP/RTU
                if (ProtocolConstant.RTU.equals(messageType)){
                    modBusEntity.setCrc(CRC16Utils.getCRC(modBusEntity.toHexString()));
                }else {
                    modBusEntity.setTransactionId(numberToHex(new Random().nextInt(65535), 2));
                    modBusEntity.setProtocolMark("0000");
                    modBusEntity.setLength("0006");
                }
                break;


            // Write Request
            case ProtocolConstant.CMD_SET:
                Map<String, Object> dataMap = params.getJSONObject(ProtocolConstant.DATA);
                Set<String> keySet = dataMap.keySet();
                int size = dataMap.size();

                String result = "";
                switch (moduleType){
                    case ProtocolConstant.COIL:
                        if (size == 1){
                            modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_WRITE_SINGLE_COIL);
                            for (String key : keySet) {
                                int boolValue = ((BigDecimal)dataMap.get(key)).intValue();
                                result = boolValue==0 ? ProtocolConstant.COIL_OFF:ProtocolConstant.COIL_ON;
                            }
                            modBusEntity.setData(startId + result);
                        }else {
                            modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_WRITE_MULTIPLE_COILS);
                            String coilTotal = numberToHex(size, 2);
                            String length = size%8==0? HexUtils.numberToHex(size/8, 1):HexUtils.numberToHex(size/8 + 1, 1);

                            boolean[] boolArr = new boolean[size];
                            for (int i = 0; i < size; i++) {
                                JSONObject function = (JSONObject)functions.get(i + idx);
                                int value = ((BigDecimal)dataMap.get(function.getString(ProtocolConstant.FUNCTION_MARK))).intValue();
                                boolArr[i] = value!=0;
                            }
                            // boolean[] -> byte[] -> HexStr
                            modBusEntity.setData(startId + coilTotal + length + byteArray2HexString(booleans2Bytes(boolArr)));
                        }
                        break;

                    case ProtocolConstant.HOLDING_REGISTER:
                        if (size == 1){
                            JSONObject function = (JSONObject)functions.get(idx);
                            String dataType = function.getString(ProtocolConstant.DATA_TYPE);
                            Number value = (Number)dataMap.get(function.getString(ProtocolConstant.FUNCTION_MARK));
                            // 只有enum,int16,uint16是单寄存器写
                            if (ProtocolConstant.DATA_TYPE_ENUM.equals(dataType) || ProtocolConstant.DATA_TYPE_INT16.equals(dataType) || ProtocolConstant.DATA_TYPE_UINT16.equals(dataType)){
                                modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_WRITE_SINGLE_REGISTER);
                                modBusEntity.setData(startId + getHexByFunctionModel(function, value));
                            }else {
                                modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_WRITE_MULTIPLE_REGISTERS);
                                modBusEntity.setData(startId + "0002" + "04" + getHexByFunctionModel(function, value));
                            }
                        }else {
                            modBusEntity.setFunctionCode(ProtocolConstant.MODBUS_WRITE_MULTIPLE_REGISTERS);
                            int functionWordLength = getFunctionWordLength(functions, size, idx);
                            String registerTotal = numberToHex(functionWordLength, 2);
                            String byteLength = numberToHex(functionWordLength*2, 1);

                            for (int i = 0; i < size; i++) {
                                JSONObject function = (JSONObject)functions.get(i + idx);
                                result += getHexByFunctionModel(function, (Number)dataMap.get(function.getString(ProtocolConstant.FUNCTION_MARK)));
                            }

                            modBusEntity.setData(startId + registerTotal + byteLength + result);
                        }
                        break;
                }
                // TCP/RTU
                if (ProtocolConstant.RTU.equals(messageType)){
                    modBusEntity.setCrc(CRC16Utils.getCRC(modBusEntity.toHexString()));
                }else {
                    modBusEntity.setLength(HexUtils.numberToHex(modBusEntity.toHexString().length()/2, 2));
                    modBusEntity.setTransactionId(numberToHex(new Random().nextInt(65535), 2));
                    modBusEntity.setProtocolMark("0000");
                }
                break;

            default:
                LOGGER.info("$$$$$$$$$$ [ModBus]生成接口请求报文 Unsupported CMD:{}", cmd);
                return null;
        }

        String requestStr = modBusEntity.toHexString();
        LOGGER.info("$$$$$$$$$$ [ModBus]生成接口请求报文, 出参={}", requestStr);
        return requestStr;
    }

    /**
     * 解析 接口读写响应
     * @param hexStr 16进制报文
     * @param interfaceJson 下发Json
     * @param deviceConnectJson 设备连接信息
     * @param moduleModelJson 模块模型
     * @return DFDJson
     */
    public static JSONObject analyzingInterfaceResponse(String hexStr, JSONObject interfaceJson, JSONObject deviceConnectJson, JSONObject moduleModelJson){
        LOGGER.info("$$$$$$$$$$ [ModBus]解析接口响应报文 入参hexStr={}, interfaceJson={}, deviceConnectJson={}", hexStr, interfaceJson, deviceConnectJson);
        // JSONObject profileNettyJson = nettyJson.getJSONObject("profile");
        // String deviceId = profileNettyJson.getString("deviceId");
        // JSONObject paramNettyJson = nettyJson.getJSONObject("param");
        // JSONObject contentNettyJson = paramNettyJson.getJSONObject("content");
        // String request = contentNettyJson.getString("request");
        // String response = contentNettyJson.getString("response");

        JSONObject DFDJson = new JSONObject();
        JSONObject profileInterface = interfaceJson.getJSONObject(ProtocolConstant.PROFILE);
        //String deviceId = profileInterface.getString("deviceId");
        String startId = interfaceJson.getString("startId");
        String response = hexStr;

        JSONObject paramsInterfaceJson = interfaceJson.getJSONObject(ProtocolConstant.PARAMS);
        String moduleMark = paramsInterfaceJson.getString(ProtocolConstant.MODULE_MARK);

        //从Redis获取设备连接信息
        // IotMstDevice device = IotRedisUtil.getHash("deviceInfo", deviceId);
        // if (null==device){
        //     LOGGER.info("$$$$$$$$$$$ [ERROR]ModBus Redis无此设备信息，deviceId：{}",deviceId);
        //     return null;
        // }
        // JSONObject moduleModelJson = JSONObject.parseObject(String.valueOf(IotRedisUtil.hGet("modelThingModuleInfo:" + device.getModelCode(), moduleMark)));
        // if (null==moduleModelJson){
        //     LOGGER.info("$$$$$$$$$$$ [ERROR]ModBus Redis无此模块模型信息, moduleMark:{}", moduleMark);
        //     return null;
        // }
        // JSONObject deviceConnectJson = JSONObject.parseObject(device.getDeviceConnectInfo());

        String messageType = deviceConnectJson.getString(ProtocolConstant.MESSAGE_TYPE);
        ModBusEntity responseEntity = contentHex2ModBusEntity(response, messageType);
        // 异常返回
        if (HexUtils.hexString2Int(responseEntity.getFunctionCode()) > 128){
            JSONObject exceptionJson = new JSONObject();
            exceptionJson.put("code", responseEntity.getData());
            exceptionJson.put("description", ModBusExceptionEnum.getByCode(responseEntity.getData()).getDescription());
            DFDJson.put("exception", exceptionJson);
            LOGGER.info("$$$$$$$$$$ [ModBus]解析接口响应报文 出参DFDJson={}", DFDJson);
            return DFDJson;
        }

        // 封装数据
        JSONObject dataDFDJson = new JSONObject(new LinkedHashMap<>());
        String responseFunctionCode = responseEntity.getFunctionCode().toUpperCase();
        switch (responseFunctionCode){
            case ProtocolConstant.MODBUS_READ_COILS:
            case ProtocolConstant.MODBUS_READ_DISCRETE_INPUTS:
                // 数据16进制数组
                String[] hexArr = HexUtils.formatterHex(responseEntity.getData().substring(2)).split(" ");

                String boolResult = "";
                for (int i = 0; i < hexArr.length; i++) {
                    StringBuilder reverse = new StringBuilder(HexUtils.hexString2binaryString(hexArr[i])).reverse();
                    boolResult += reverse.toString();
                }
                LOGGER.info("$$$$$$$$$$ [ModBus]Boolean Result: {}", boolResult);

                //ModBusEntity requestEntity = contentHex2ModBusEntity(request, messageType);
                //String startId = requestEntity.getData().substring(0, 4);
                JSONArray dataInterfaceJson = paramsInterfaceJson.getJSONArray(ProtocolConstant.DATA);

                JSONArray functions = moduleModelJson.getJSONArray(ProtocolConstant.FUNCTIONS);
                int idx = getFunctionIndexByStartId(functions, startId);
                if (idx==-1){
                    LOGGER.info("$$$$$$$$$$ [ModBus]起始地址:{} 的功能点不存在, 无法解析",startId);
                    return null;
                }

                for (int i = 0 ; i < dataInterfaceJson.size(); i++) {
                    JSONObject function = (JSONObject)functions.get(i+idx);
                    dataDFDJson.put(function.getString(ProtocolConstant.FUNCTION_MARK), boolResult.charAt(i));
                }
                break;

            case ProtocolConstant.MODBUS_READ_HOLDING_REGISTERS:
            case ProtocolConstant.MODBUS_READ_INPUT_REGISTERS:
                String[] hexArr3 = HexUtils.formatterHex(responseEntity.getData().substring(2)).split(" ");

                // ModBusEntity requestEntity3 = contentHex2ModBusEntity(request, messageType);
                // String startId3 = requestEntity3.getData().substring(0, 4);
                JSONArray dataInterfaceJson3 = paramsInterfaceJson.getJSONArray(ProtocolConstant.DATA);

                JSONArray functions3 = moduleModelJson.getJSONArray(ProtocolConstant.FUNCTIONS);
                int idx3 = getFunctionIndexByStartId(functions3, startId);
                if (idx3 == -1){
                    LOGGER.info("$$$$$$$$$$ [ModBus]起始地址:{} 的功能点不存在, 无法解析",startId);
                }

                int offset = 0;
                for (int i = 0; i < dataInterfaceJson3.size() && offset < hexArr3.length; i++) {
                    JSONObject function = (JSONObject)functions3.get(i+idx3);
                    String dataType = function.getString(ProtocolConstant.DATA_TYPE);
                    String functionMark = function.getString(ProtocolConstant.FUNCTION_MARK);

                    switch (dataType){
                        case ProtocolConstant.DATA_TYPE_ENUM:
                            String enumHex = hexArr3[offset] + hexArr3[offset+1];
                            int enumValue = Integer.valueOf(enumHex, 16);

                            JSONArray dataInfo = function.getJSONArray(ProtocolConstant.DATA_INFO);
                            for (int k = 0; k < dataInfo.size(); k++) {
                                JSONObject enumJson = (JSONObject)dataInfo.get(k);
                                Integer enumIntValue = Integer.valueOf(enumJson.getString("value"));
                                if (enumIntValue==enumValue){
                                    dataDFDJson.put(functionMark, enumValue);
                                    break;
                                }
                            }
                            offset += 2;
                            break;

                        case ProtocolConstant.DATA_TYPE_INT16:
                            String int16Hex = hexArr3[offset] + hexArr3[offset+1];
                            dataDFDJson.put(functionMark, HexUtils.hexStringToShort(int16Hex));
                            offset += 2;
                            break;

                        case ProtocolConstant.DATA_TYPE_UINT16:
                            String uint16Hex = hexArr3[offset] + hexArr3[offset+1];
                            dataDFDJson.put(functionMark, Integer.parseInt(uint16Hex, 16));
                            offset += 2;
                            break;

                        case ProtocolConstant.DATA_TYPE_INT32:
                            String int32Hex = "";
                            for (int j = 0; j < 4; j++) {
                                int32Hex += hexArr3[offset+j];
                            }
                            dataDFDJson.put(functionMark, hexString2Int(int32Hex));
                            offset += 4;
                            break;

                        case ProtocolConstant.DATA_TYPE_UINT32:
                            String uint32Hex = "";
                            for (int j = 0; j < 4; j++) {
                                uint32Hex += hexArr3[offset+j];
                            }
                            dataDFDJson.put(functionMark, Long.parseLong(uint32Hex, 16));
                            offset += 4;
                            break;

                        case ProtocolConstant.DATA_TYPE_FLOAT:
                            String floatHex = "";
                            for (int j = 3; j >= 0; j--) {
                                floatHex += hexArr3[offset+j];
                            }
                            dataDFDJson.put(functionMark, HexUtils.hexString2Float(floatHex));
                            offset += 4;
                            break;
                        default:
                            LOGGER.info("$$$$$$$$$$ [ModBus]Unsupported Data Type: {}", dataType);
                            break;
                    }
                }
                break;

            case ProtocolConstant.MODBUS_WRITE_SINGLE_COIL:
            case ProtocolConstant.MODBUS_WRITE_SINGLE_REGISTER:
            case ProtocolConstant.MODBUS_WRITE_MULTIPLE_COILS:
            case ProtocolConstant.MODBUS_WRITE_MULTIPLE_REGISTERS:
            //写成功后，原数据回复
            return interfaceJson;

            default:
                LOGGER.info("$$$$$$$$$$ [ModBus]Unsupported FunctionCode:{}", responseFunctionCode);
                break;
        }

        JSONObject paramDFDJson = new JSONObject();
        paramDFDJson.put(ProtocolConstant.CMD, ProtocolConstant.CMD_GET);
        paramDFDJson.put(ProtocolConstant.MODULE_MARK, moduleMark);
        paramDFDJson.put(ProtocolConstant.DATA, dataDFDJson);
        DFDJson.put(ProtocolConstant.PARAMS, paramDFDJson);

        DFDJson.put(ProtocolConstant.PROFILE, profileInterface);
        DFDJson.put(ProtocolConstant.METHOD, interfaceJson.getString(ProtocolConstant.METHOD));
        DFDJson.put(ProtocolConstant.MESSAGE_ID, interfaceJson.getString(ProtocolConstant.MESSAGE_ID));
        DFDJson.put(ProtocolConstant.VERSION, "1.0");
        DFDJson.put(ProtocolConstant.TIME, interfaceJson.getIntValue(ProtocolConstant.TIME));

        LOGGER.info("$$$$$$$$$$ [ModBus]解析接口响应报文 出参DFDJson={}", DFDJson);
        return DFDJson;
    }

    /**--------------------------
     * 根据单个功能点模型获取下发16进制
     * @param function
     * @param value
     */
    private static String getHexByFunctionModel(JSONObject function, Number value){
        String dataType = function.getString(ProtocolConstant.DATA_TYPE);

        switch (dataType){
            case ProtocolConstant.DATA_TYPE_ENUM:
            case ProtocolConstant.DATA_TYPE_UINT16:
                return HexUtils.numberToHex(value.intValue(), 2);

            case ProtocolConstant.DATA_TYPE_INT16:
                return HexUtils.shortToHexString(value.shortValue());

            case ProtocolConstant.DATA_TYPE_INT32:
                return HexUtils.numberToHex(value.intValue(), 4);

            case ProtocolConstant.DATA_TYPE_UINT32:
                return HexUtils.longToHex(value.longValue(), 4);

            case ProtocolConstant.DATA_TYPE_INT64:
            case ProtocolConstant.DATA_TYPE_UINT64:
                return HexUtils.longToHex(value.longValue(), 8);

            case ProtocolConstant.DATA_TYPE_FLOAT:
                String result = float2HexString(value.floatValue());
                String byteOrder = function.getString(ProtocolConstant.BYTE_ORDER);
                switch (byteOrder){
                    case ProtocolConstant.BYTE_ORDER_ABCD:
                        result = HexUtils.swapOrder(result);
                        break;
                    case ProtocolConstant.BYTE_ORDER_CDAB:
                        break;
                }
                return result;

            case ProtocolConstant.DATA_TYPE_DOUBLE:
                break;

            default:
                LOGGER.info("$$$$$$$$$$ [ModBus]Unsupported Data Type:{}", dataType);
                return null;
        }
        return null;
    }

    /**-------------------------
     * 根据起始Id获取功能点List索引
     */
    private static int getFunctionIndexByStartId(JSONArray functions, String startId){
        for (int i = 0; i < functions.size(); i++) {
            JSONObject function = (JSONObject)functions.get(i);
            if (startId.equals(function.getString(ProtocolConstant.FUNCTION_OFFSET))){
                return i;
            }
        }
        return -1;
    }

    /**-----------------------
     * 获取指定数量的functions字长
     * @param functions
     * @param total 计算数量
     * @param idx 起始索引
     * @return
     */
    public static int getFunctionWordLength(JSONArray functions, int total, int idx){
        int wordLength = 0;

        for (int i = 0; i < total; i++) {
            JSONObject function = (JSONObject)functions.get(i + idx);
            String dataType = function.getString(ProtocolConstant.DATA_TYPE);

            switch (dataType){
                case ProtocolConstant.DATA_TYPE_ENUM:
                case ProtocolConstant.DATA_TYPE_INT16:
                case ProtocolConstant.DATA_TYPE_UINT16:
                    wordLength += 1;
                    break;

                case ProtocolConstant.DATA_TYPE_INT32:
                case ProtocolConstant.DATA_TYPE_UINT32:
                case ProtocolConstant.DATA_TYPE_FLOAT:
                    wordLength += 2;
                    break;

                case ProtocolConstant.DATA_TYPE_DOUBLE:
                    wordLength +=4;
                    break;
            }
        }
        return wordLength;
    }

    /**---------------------
     * 解析响应读报文格式
     * @param contentHex
     * @param messageType
     */
    private static ModBusEntity contentHex2ModBusEntity(String contentHex, String messageType){
        ModBusEntity modBusEntity = new ModBusEntity();
        if (messageType.equals(ProtocolConstant.TCP)){
            modBusEntity.setTransactionId(contentHex.substring(0, 4));
            modBusEntity.setProtocolMark(contentHex.substring(4, 8));
            modBusEntity.setLength(contentHex.substring(8, 12));
            modBusEntity.setUnitId(contentHex.substring(12, 14));
            modBusEntity.setFunctionCode(contentHex.substring(14, 16));
            modBusEntity.setData(contentHex.substring(16));
        }else {
            modBusEntity.setUnitId(contentHex.substring(0, 2));
            modBusEntity.setFunctionCode(contentHex.substring(2, 4));
            modBusEntity.setData(contentHex.substring(4, contentHex.length()-4));
            modBusEntity.setCrc(contentHex.substring(contentHex.length()-4));
        }
        return modBusEntity;
    }


    /**
     * 生成写入报文 单个写入，多个写入，- 该方法仅针对下发写
     *
     * @param DFDJson
     * @return
     */
    private static String DFDJson2NettyJsonForSet(JSONObject deviceConnectJson, JSONObject DFDJson, JSONObject moduleModelJson) {
        ModBusMessageDTO modBusMessageDTO = new ModBusMessageDTO();
        JSONObject params = DFDJson.getJSONObject("params");
        //数据
        JSONObject data = params.getJSONObject("data");
        String startId = DFDJson.getString("startId");
        //下发的functionMark列表集合
        List<String> functionMarkList = DFDJson.getJSONArray("functionMarkList").toJavaList(String.class);
        //物模型功能点有序列表
        List<JSONObject> functions = moduleModelJson.getJSONArray("functions").toJavaList(JSONObject.class);
        //模块类型 0-输出线圈，4-保持寄存器
        String moduleType = moduleModelJson.getString("moduleType");
        //报文类型
        String messageType = deviceConnectJson.getString("messageType");

        //报文类型
        modBusMessageDTO.setMessageType(messageType.toLowerCase());
        //从站地址
        modBusMessageDTO.setSlaveAddr(HexUtils.shortToHexStringForSlaveAddr(deviceConnectJson.getShort("slaveAddr")));
        //下发的起始地址
        modBusMessageDTO.setStartId(startId);
        //线圈
        if ("0".equals(moduleType)) {
            if (data.size() == 1) {
                //写单个输出线圈
                modBusMessageDTO.setFunctionType("05");
                if (1 == data.getInteger(functionMarkList.get(0))) {
                    //填入数据
                    modBusMessageDTO.setData("ff00");
                } else if (0 == data.getInteger(functionMarkList.get(0))) {
                    //填入数据
                    modBusMessageDTO.setData("0000");
                }
            } else {
                //写多个输出线圈
                modBusMessageDTO.setFunctionType("0F");
                JSONObject dataForList = getDataForList(startId, moduleType, data, functions);
                //生成bool的数据报文
                String dataForBool = getDataForBool(new StringBuffer(dataForList.getString("dataMessage")));
                //线圈数量
                modBusMessageDTO.setRegisters(HexUtils.numberToHex(dataForList.getInteger("registerSum"), 2));
                //字节数
                modBusMessageDTO.setDataLength(HexUtils.shortToHexStringForSlaveAddr((short) ((dataForBool.length()) / 2)));
                //填入数据-16进制
                modBusMessageDTO.setData(dataForBool);
            }
        } else if ("4".equals(moduleType)) {
            //寄存器
            JSONObject dataForList = getDataForList(startId, moduleType, data, functions);
            //生成寄存器的数据报文
            String dataMessage = dataForList.getString("dataMessage");
            modBusMessageDTO.setData(dataMessage);
            if (data.size() == 1) {
                //写单个保持寄存器-功能码
                modBusMessageDTO.setFunctionType("06");
            } else {
                //写多个保持输出线圈-功能码
                modBusMessageDTO.setFunctionType("10");
                //数据字节数
                modBusMessageDTO.setDataLength(HexUtils.shortToHexStringForSlaveAddr((short) ((dataMessage.length()) / 2)));
                //线圈数量
                modBusMessageDTO.setRegisters(HexUtils.numberToHex(dataForList.getInteger("registerSum"), 2));
            }
        }
        //根据报文类型选择生成报文类型
        if ("TCP".equalsIgnoreCase(messageType)) {
            //事务元标识符
            modBusMessageDTO.setNumberNum(HexUtils.getNumberRandom(4, "ffff"));
            //协议标识符
        }
        //返回生成的报文
        return modBusMessageDTO.getMessageForSet();
    }

    private static JSONObject getDataForList(String startId, String moduleType, JSONObject data, List<JSONObject> functions) {
        JSONObject dataJson = new JSONObject();
        boolean flag = false;
        StringBuffer stringBuffer = new StringBuffer();
        int registerSum = 0;
        for (int i = 0; i < functions.size(); i++) {
            JSONObject functionMslJson = functions.get(i);
            if (startId.equals(functionMslJson.getString("functionOffset"))) {
                flag = true;
            }
            if (flag) {
                String value = data.getString(functionMslJson.getString("functionMark"));
                if (StringUtils.isNotEmpty(value)) {
                    if ("0".equals(moduleType)) {
                        stringBuffer.append(value);
                        registerSum++;
                    } else if ("4".equals(moduleType)) {
                        JSONObject hexByDataType = getHexByDataType(value, functionMslJson);
                        stringBuffer.append(hexByDataType.getString("value"));
                        registerSum += hexByDataType.getInteger("length");
                    }
                } else {
                    break;
                }
            }
        }
        //数据报文
        dataJson.put("dataMessage", stringBuffer.toString());
        //线圈/寄存器数量
        dataJson.put("registerSum", registerSum);
        return dataJson;
    }


    /**
     * 该方法可将 011011/01101100/01/01010101010101111001100100，等长度不为1的二进制码转化为的转化为16进制码，-仅针对于modbus
     *
     * @param binaryString
     * @return
     */
    private static String getDataForBool(StringBuffer binaryString) {
        if (binaryString.length() <= 8) {
            String string = HexUtils.binaryString2HexString(binaryString.reverse().toString());
            return getForBool(string);
        } else {
            int i = 0;
            StringBuffer buffer = new StringBuffer();
            while (((i + 1) * 8) <= binaryString.length()) {
                buffer.append(getForBool(HexUtils.binaryString2HexString(new StringBuffer(binaryString.substring(i * 8, (i + 1) * 8)).reverse().toString())));
                i++;
            }
            if (0 != binaryString.length() % 8) {
                String string = HexUtils.binaryString2HexString(new StringBuffer(binaryString.substring(i * 8, binaryString.length())).reverse().toString());
                buffer.append(getForBool(string));
            }
            return buffer.toString();
        }
    }

    //用于补0
    private static String getForBool(String string) {
        if ("0".equals(string)) {
            return "00";
        } else {
            if (string.length() < 2) {
                return "0" + string;
            }
            return string;
        }
    }

    /**
     * 跟据不同的modbus寄存器数据类型转换出对应的16进制码和所占用的寄存器数量
     *
     * @param value
     * @param functionMslJson
     * @return
     */
    private static JSONObject getHexByDataType(String value, JSONObject functionMslJson) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("length", 2);
        switch (functionMslJson.getString("dataType")) {
            case "int32":
                jsonObject.put("value", HexUtils.numberToHex(Integer.valueOf(value), 4));
                break;
            case "uint32":
            case "float":
                jsonObject.put("value", HexUtils.longToHex(Long.valueOf(value), 4));
                break;
            default:
                jsonObject.put("value", HexUtils.numberToHex(Integer.valueOf(value), 2));
                jsonObject.put("length", 1);
                break;
        }
        return jsonObject;
    }
}
