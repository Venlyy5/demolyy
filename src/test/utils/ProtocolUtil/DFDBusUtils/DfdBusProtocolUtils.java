package com.dfd.platform.utils.dfdbus;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dfd.common.assembly.redis.IotRedisUtil;
import com.dfd.platform.model.IotMstDevice;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Random;
import static com.dfd.platform.utils.dfdbus.HexUtils.*;


/**--------------------------------------------
 * DFDBus协议工具
 * @author LiYangYang
 * @date 2022/11/11
 *
 * NettyJson    <------>    [DFDJson]
 * 设备上报登录：direct.connect
 * 设备上报登录-平台响应： [direct.login.success/fail]
 * 设备上报心跳：direct.report
 * 设备上报心跳-平台响应： [direct.pong]
 * 设备上报数据：direct.report
 * 设备上报数据-平台响应： [direct.report_reply]
 * 平台下发-读：[direct.read]
 * 平台下发-读-设备响应：direct.read_reply
 * 平台下发-写：[direct.set]
 * 平台下发-写-设备响应：direct.set_reply
 */
public class DfdBusProtocolUtils {
    // 报文-交互命令
    public static final String INTERACTIVE_COMMAND_DEVICE_REPORT = "00";
    public static final String INTERACTIVE_COMMAND_PLATFORM_DOWN = "01";
    public static final String INTERACTIVE_COMMAND_DEVICE_REPORT_REPLY = "02";
    // 报文-指令代码
    public static final String FUNCTION_CODE_HEARTBEAT = "00";
    public static final String FUNCTION_CODE_LOGIN = "01";
    public static final String FUNCTION_CODE_POST = "02";
    public static final String FUNCTION_CODE_SET = "03";
    public static final String FUNCTION_CODE_GET = "04";

    // JSON-交互命令
    public static final String METHOD_DEVICE_REPORT = "device.report";
    public static final String METHOD_PLATFORM_DOWN = "platform.down";
    public static final String METHOD_DEVICE_REPORT_REPLY = "device.report.reply";
    // JSON-指令代码
    public static final String CMD_LOGIN = "login";
    public static final String CMD_POST = "post";
    public static final String CMD_SET = "set";
    public static final String CMD_GET = "get";


    /**----------------------
     * 校验DFDBus报文CRC16
     * @param dfdBusContent
     */
    public static boolean checkCRC(String dfdBusContent){
        int length = dfdBusContent.length();
        String contentCRC = dfdBusContent.substring(length-8, length-4);
        String crc = CRC16Utils.getCRC(dfdBusContent.substring(8, length - 8));
        if (contentCRC.equalsIgnoreCase(crc)){
            return true;
        }
        return false;
    }

    /**--------------------
     * 校验DFDBus报文数据长度
     * @param dfdBusContent
     */
    public static boolean checkDataLength(String dfdBusContent){
        String contentLength = dfdBusContent.substring(4, 8);
        String contentData = dfdBusContent.substring(8, dfdBusContent.length()-4);

        if (Integer.valueOf(contentLength, 16) == contentData.length()/2){
            return true;
        }
        return false;
    }

    /**-----------------------------------------------
     * 设备上报-解析报文(contentHexString -> contentJson)
     * @param content
     */
    public static JSONObject analyzingContentHex(String content){
        // 校验CRC
        if (! checkCRC(content)){
            return null;
        }
        String formatter = formatterHex(content);
        String[] strArray = formatter.split(" ");
        int length = strArray.length;

        // 报文基础帧
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        jsonObject.put("head", strArray[0] + strArray[1]);
        jsonObject.put("dataLength", strArray[2] + strArray[3]);
        jsonObject.put("interactiveCommand", strArray[4]);
        jsonObject.put("functionCode", strArray[5]);
        jsonObject.put("crc", strArray[length-4] + strArray[length-3]);
        jsonObject.put("end", strArray[length-2] + strArray[length-1]);

        // 根据交互与命令解析报文
        String str = strArray[4] + strArray[5];
        switch (str){
            // 设备上报 && 登录验证
            case "0001":
                StringBuilder deviceId = new StringBuilder();
                for (int i = 6; i < 22; i++) {
                    deviceId.append(strArray[i]);
                }
                jsonObject.put("deviceId", deviceId.toString());

                StringBuilder deviceToken = new StringBuilder();
                for (int i = 22; i < 38; i++) {
                    deviceToken.append(strArray[i]);
                }
                jsonObject.put("deviceToken", deviceToken.toString());
                break;

            // 设备上报 && 数据
            case "0002":
            // 设备上报 && 响应写
            case "0003":
            // 设备上报 && 响应读
            case "0004":
                StringBuilder messageId = new StringBuilder();
                for (int i = 6; i < 10; i++) {
                    messageId.append(strArray[i]);
                }
                jsonObject.put("messageId", messageId.toString());

                StringBuilder functionStartId = new StringBuilder();
                for (int i = 10; i < 12; i++) {
                    functionStartId.append(strArray[i]);
                }
                jsonObject.put("functionStartId", functionStartId.toString());

                // 功能点1~n数据
                jsonObject.put("functionData", content.substring(24, content.length()-8));
                break;

            // 设备上报 && 心跳 "0000"
            default:
                break;
        }
        return jsonObject;
    }

    /**--------------------------------------------------------------
     * 设备上报-解析功能点数据(functionDataHexString -> functionDataJson)
     * @param functionData 功能点数据16进制报文
     * @param moduleModel 模块的模型
     * @return 功能点数据Json：{"funMark1":"value1", "funMark2":"value2", "funMark3":"value3"}
     */
    public static JSONObject analyzingFunctionDataHex(String functionData, JSONObject moduleModel) {
        JSONArray functionList = moduleModel.getJSONArray("functions");
        String[] strList = formatterHex(functionData).split(" ");

        // 如果上报数据字节数 > 模块模型的字节数， 不进行解析
        int  moduleModelLength = Integer.valueOf(moduleModel.getString("moduleEndId"), 16) - Integer.valueOf(moduleModel.getString("moduleStartId"), 16);
        if (strList.length > moduleModelLength){
            return null;
        }

        JSONObject dataJson = new JSONObject(new LinkedHashMap<>());
        int offset = 0;
        for (int i = 0; i < functionList.size() && offset <= strList.length; i++) {
            JSONObject function = (JSONObject)functionList.get(i);
            String dataType = function.getString("dataType");
            String functionMark = function.getString("functionMark");

            switch (dataType){
                case "number":
                    int numberLength = function.getIntValue("length");
                    StringBuilder sbNumber = new StringBuilder();
                    for (int j = 0; j < numberLength; j++) {
                        sbNumber.append(strList[offset+j]);
                    }

                    BigDecimal bigDecimal = new BigDecimal(0);
                    // 0:无符号， 1:有符号
                    if ("0".equals(function.getString("numberSign"))){
                        if (numberLength==2){
                            int unsignedValue2 = Integer.parseInt(sbNumber.toString(), 16);
                            bigDecimal = new BigDecimal(unsignedValue2);
                        }else {
                            long unsignedValue48 = Long.parseLong(sbNumber.toString(), 16);
                            bigDecimal = new BigDecimal(unsignedValue48);
                        }
                    }else{
                        if (numberLength==2){
                            short shortValue = hexStringToShort(sbNumber.toString());
                            bigDecimal = new BigDecimal(shortValue);
                        }else if(numberLength==4){
                            int intValue = hexString2Int(sbNumber.toString());
                            bigDecimal = new BigDecimal(intValue);
                        }else {
                            long longValue = hexString2Long(sbNumber.toString());
                            bigDecimal = new BigDecimal(longValue);
                        }
                    }

                    // 如果小数位>0，左移
                    int decimals = function.getIntValue("decimals");
                    if ( decimals > 0){
                        bigDecimal = bigDecimal.movePointLeft(decimals);
                    }
                    //function.put("dataInfo", bigDecimal);
                    dataJson.put(functionMark, bigDecimal);
                    offset += numberLength;
                    break;

                case "bool":

                case "enum":
                    String strValue = strList[offset] + strList[offset+1];
                    int value = Integer.valueOf(strValue, 16);

                    //function.put("dataInfo", value);
                    dataJson.put(functionMark, value);
                    offset += 2;
                    break;

                case "text":
                    int textLength = function.getIntValue("length");
                    String textEncoding = function.getString("textEncoding");

                    StringBuilder sbText = new StringBuilder();
                    for (int j = 0; j < textLength; j++) {
                        sbText.append(strList[offset+j]);
                    }

                    //function.put("dataInfo", hexStr2TestStr(sbText.toString(), textEncoding));
                    dataJson.put(functionMark, hexStr2TestStr(sbText.toString(), textEncoding));
                    offset += textLength;
                    break;

                default:
                    break;
            }
        }
        return dataJson;
        //return moduleModel; 返回完整模型数据
    }

    /**-----------------------------------------
     * 设备上报(NettyJson -> DFDJson) 登录|心跳|数据
     */
    public static JSONObject nettyJson2DFDJson(JSONObject nettyJson){
        // 解析nettyJson
        JSONObject paramNettyJson = nettyJson.getJSONObject("param");
        JSONObject profileNettyJson = nettyJson.getJSONObject("profile");
        String content = paramNettyJson.getString("content");

        JSONObject contentJson = analyzingContentHex(content);
        if (null == contentJson){
            return null;
        }

        // 解析报文内容, 根据交互命令和功能码组装DFDJson
        JSONObject DFDJson = new JSONObject();
        JSONObject profileDFDJson = new JSONObject();
        JSONObject paramDFDJson = new JSONObject();

        String contentMethod = contentJson.getString("interactiveCommand") + contentJson.getString("functionCode");
        switch (contentMethod){
            // 设备上报 && 心跳
            case "0000":
                profileDFDJson.put("deviceId", profileNettyJson.getString("deviceId"));
                DFDJson.put("method", "direct.ping");
                break;

            // 设备上报 && 登录验证
            case "0001":
                profileDFDJson.put("deviceId", hexStr2TestStr(contentJson.getString("deviceId"), "ASCII"));
                profileDFDJson.put("token", hexStr2TestStr(contentJson.getString("deviceToken"), "ASCII"));
                profileDFDJson.put("ip", paramNettyJson.getString("ip"));
                DFDJson.put("method", "direct.login");
                break;

            // 设备上报 && 数据
            case "0002":
                // 从Redis获取设备信息,得到moduleCode TODO
                //IotMstDevice device = IotRedisUtil.getHash("deviceInfo", paramNettyJson.getString("deviceId"));
                // 从Redis获取模块模型 TODO
                //JSONObject moduleModelJson2 = JSONObject.parseObject(JSONObject.toJSONString(IotRedisUtil.hGet("modelThingModuleInfo:" + device.getModelCode(), contentJson.getString("functionStartId"))));
                //JSONObject moduleModelJson2 = Test_DFDBus.getModuleModel("002F"); // 测试模型1
                JSONObject moduleModelJson2 = Test_DFDBus.getModuleModel2(); // 测试模型2

                profileDFDJson.put("deviceId", profileNettyJson.getString("deviceId"));
                paramDFDJson.put("cmd", CMD_POST);
                paramDFDJson.put("moduleMark", moduleModelJson2.getString("moduleMark"));
                paramDFDJson.put("data", analyzingFunctionDataHex(contentJson.getString("functionData"), moduleModelJson2));
                DFDJson.put("params", paramDFDJson);
                DFDJson.put("method", "direct.report");
                break;

            // 设备上报 && 响应写
            case "0003":
                // 从Redis获取设备信息 TODO
                //IotRedisUtil.hGet("deviceInfo","deviceId");
                // 从Redis获取模块模型 TODO
                //JSONObject moduleModelJson3 = JSONObject.parseObject(JSONObject.toJSONString(IotRedisUtil.hGet("modelThingModuleInfo:" + "moduleCode", "moduleStartId")));
                JSONObject moduleModelJson3 = Test_DFDBus.getModuleModel("002F");

                profileDFDJson.put("deviceId", profileNettyJson.getString("deviceId"));
                paramDFDJson.put("cmd", CMD_POST);
                paramDFDJson.put("moduleMark", moduleModelJson3.getString("moduleMark"));
                paramDFDJson.put("data", analyzingFunctionDataHex(contentJson.getString("functionData"), moduleModelJson3));
                DFDJson.put("params", paramDFDJson);
                DFDJson.put("method", "direct.set_reply");
                break;

            // 设备上报 && 响应读
            case "0004":
                // 从Redis获取设备信息 TODO
                //IotRedisUtil.hGet("deviceInfo","deviceId");
                // 从Redis获取模块模型 TODO
                //JSONObject moduleModelJson4 = JSONObject.parseObject(JSONObject.toJSONString(IotRedisUtil.hGet("modelThingModuleInfo:" + "moduleCode", "moduleStartId")));
                JSONObject moduleModelJson4 = Test_DFDBus.getModuleModel("002F");

                profileDFDJson.put("deviceId", profileNettyJson.getString("deviceId"));
                paramDFDJson.put("cmd", CMD_POST);
                paramDFDJson.put("moduleMark", moduleModelJson4.getString("moduleMark"));
                paramDFDJson.put("data", analyzingFunctionDataHex(contentJson.getString("functionData"), moduleModelJson4));
                DFDJson.put("params", paramDFDJson);
                DFDJson.put("method", "direct.read_reply");
                break;

            default:
                return null;
        }

        DFDJson.put("profile", profileDFDJson);
        DFDJson.put("version", nettyJson.getString("version"));
        DFDJson.put("time", nettyJson.getString("time"));
        DFDJson.put("messageId", nettyJson.getString("messageId"));
        return DFDJson;
    }

    /**------------------------------------------
     *平台下发(DFDJson -> NettyJson) 登录|心跳|数据
     */
    public static JSONObject DFDJson2NettyJson(JSONObject DFDJson){
        JSONObject profileDFDJson = DFDJson.getJSONObject("profile");
        String deviceId = profileDFDJson.getString("deviceId");

        String method = DFDJson.getString("method");
        String time = DFDJson.getString("time");
        String version = DFDJson.getString("version");
        String messageId = DFDJson.getString("messageId");

        // 组装NettyJson
        JSONObject nettyJson = new JSONObject();
        JSONObject paramNettyJson = new JSONObject();
        JSONObject profileNettyJson = new JSONObject();
        // 组装报文
        DfdBusEntity dfdBusEntity = new DfdBusEntity();
        dfdBusEntity.setInteractiveCommand(INTERACTIVE_COMMAND_PLATFORM_DOWN);

        // 设备上报登录-平台响应
        if (method.contains("direct.login")){
            dfdBusEntity.setFunctionCode(FUNCTION_CODE_LOGIN);
            dfdBusEntity.setDeviceId(textStr2HexStr(deviceId, "ASCII"));
            String status = method.equals("direct.login.success") ? "01":"00";
            dfdBusEntity.setStatus(status);
        }

        // 设备上报心跳-平台响应
        if (method.equals("direct.pong")){
            dfdBusEntity.setFunctionCode(FUNCTION_CODE_HEARTBEAT);
        }

        // 设备上报数据-平台响应
        if (method.equals("direct.report_reply")){
            dfdBusEntity.setInteractiveCommand(INTERACTIVE_COMMAND_DEVICE_REPORT_REPLY);
            dfdBusEntity.setFunctionCode(FUNCTION_CODE_POST);

            // 在设备上报的原始报文中，需修改交互命令00->02 && 重新计算CRC
            String content = DFDJson.getString("content");
            JSONObject contentHex = analyzingContentHex(content);
            if (null == contentHex){
                return null;
            }
            dfdBusEntity.setMessageId(contentHex.getString("messageId"));
            dfdBusEntity.setFunctionStartId(contentHex.getString("functionStartId"));
            dfdBusEntity.setFunctionData(contentHex.getString("functionData"));
        }

        JSONObject paramsDFDJson = DFDJson.getJSONObject("params");
        // 平台下发-读
        if (method.equals("direct.read")){
            dfdBusEntity.setFunctionCode(FUNCTION_CODE_GET);
            // MessageId设置随机4字节整数
            dfdBusEntity.setMessageId(numberToHex(new Random().nextInt(),4));
            dfdBusEntity.setFunctionStartId(paramsDFDJson.getString("moduleMark"));
            // 需要读取的数据长度
            dfdBusEntity.setReadDataLength(numberToHex(Integer.valueOf(paramsDFDJson.getString("readDataLength")),2));
        }
        // 平台下发-写
        if (method.equals("direct.set")){
            dfdBusEntity.setFunctionCode(FUNCTION_CODE_SET);
            // MessageId设置随机4字节整数
            dfdBusEntity.setMessageId(numberToHex(new Random().nextInt(),4));
            dfdBusEntity.setFunctionStartId(paramsDFDJson.getString("moduleMark"));

            // 从Redis获取设备信息 TODO
            //IotRedisUtil.hGet("deviceInfo","deviceId");
            // 从Redis获取模块模型 TODO
            //JSONObject moduleModelJson = JSONObject.parseObject(JSONObject.toJSONString(IotRedisUtil.hGet("modelThingModuleInfo:" + "moduleCode", "moduleStartId")));
            //JSONObject moduleModelJson = Test_DFDBus.getModuleModel("002F"); // 测试模型1
            JSONObject moduleModelJson = Test_DFDBus.getModuleModel2(); // 测试模型2

            String dataHex = makeFunctionDataHex(paramsDFDJson.getJSONObject("data"), moduleModelJson);
            dfdBusEntity.setFunctionData(dataHex);
        }

        // 设置好后设置CRC
        dfdBusEntity.setCrc(CRC16Utils.getCRC(dfdBusEntity.getCRCContent()));
        // 设置好后设置数据长度
        dfdBusEntity.setDataLength(getDataLengthHex(dfdBusEntity));

        profileNettyJson.put("deviceId", deviceId);
        paramNettyJson.put("content", dfdBusEntity.toHexString());
        nettyJson.put("profile", profileNettyJson);
        if (method.equals("direct.pong")){
            nettyJson.put("method", "direct.report_reply");
        }else {
            nettyJson.put("method", method);
        }
        nettyJson.put("time", time);
        nettyJson.put("version", version);
        nettyJson.put("messageId", messageId);
        nettyJson.put("param", paramNettyJson);
        return nettyJson;
    }

    /**-----------------------------------
     * 设置好报文后，计算DFDBusEntity的数据长度
     * @param dfdBusEntity
     * @return
     */
    public static String getDataLengthHex(DfdBusEntity dfdBusEntity){
        return numberToHex(dfdBusEntity.getDataContent().length()/2, 2);
    }

    /**------------------------------------------------
     * 平台下发(functionDataJson -> functionDataHexSting)
     * @param dataJson 下发DFDJson-dataJson数据: {"funMark1":"value1", "funMark2":"value2", "funMark3":"value3"}
     * @param moduleModel 模块模型
     * @return 功能点数据(16进制)
     */
    public static String makeFunctionDataHex(JSONObject dataJson, JSONObject moduleModel){
        // 获取模块模型的功能点列表
        JSONArray functionList = moduleModel.getJSONArray("functions");
        // 如果DFDJson的功能点数量 > 模型的功能点数量， 不执行
        if (dataJson.size() > functionList.size()){
            return null;
        }

        StringBuilder sbFunctionDataHex = new StringBuilder();
        for (int i = 0; i < dataJson.size(); i++) {
            // 功能点模型
            JSONObject function = (JSONObject)functionList.get(i);
            // 对应的下发值
            String value = dataJson.getString(function.getString("functionMark"));

            // 根据功能点的类型转换数据
            String dataType = function.getString("dataType");
            switch (dataType){
                case "number":
                    int numberLength = function.getIntValue("length");
                    int decimals = function.getIntValue("decimals");
                    String numberSign = function.getString("numberSign");
                    // 小数点右移转为整数
                    BigDecimal bigDecimal = new BigDecimal(value);
                    if (decimals != 0){
                        bigDecimal = bigDecimal.movePointRight(decimals);
                    }

                    // 根据模型的字节，符号位，转为对应16进制, 0:无符号， 1：有符号
                    String numberHexString = "";
                    if ("0".equals(numberSign)){
                        if (numberLength==2){
                            numberHexString = numberToHex(bigDecimal.intValue(), 2);
                        }else if (numberLength==4){
                            numberHexString = longToHex(bigDecimal.longValue(),4);
                        }else {
                            numberHexString = longToHex(bigDecimal.longValue(), 8);
                        }
                    }else {
                        if (numberLength==2){
                            numberHexString = shortToHexString(bigDecimal.shortValue());
                        }else if (numberLength==4){
                            numberHexString = numberToHex(bigDecimal.intValue(), 4);
                        }else {
                            numberHexString = longToHex(bigDecimal.longValue(), 8);
                        }
                    }
                    sbFunctionDataHex.append(numberHexString);
                    // if (numberLength == 8){ sbFunctionDataHex.append(longToHex(bigDecimal.longValue())); }
                    // else { sbFunctionDataHex.append(numberToHex(bigDecimal.intValue(), numberLength)); }
                    break;

                case "bool":
                case "enum":
                    sbFunctionDataHex.append(numberToHex(Integer.valueOf(value),2));
                    break;

                case "text":
                    Integer textLength = Integer.valueOf(function.getString("length"));
                    // 根据编码将文本转为16进制字符串，并补齐长度
                    String hexStr = fillCharForString(textStr2HexStr(value, function.getString("textEncoding")), "0", textLength*2, true);
                    sbFunctionDataHex.append(hexStr);
                    break;
                default:
                    break;
            }
        }
        return sbFunctionDataHex.toString();
    }


    public static void main(String[] args) {
        int numberLength = 4;
        int decimals = 0;
        String numberSign = "0";

        //============================ Number -> HexString
        // 小数点右移转为整数
        BigDecimal bigDecimal = new BigDecimal("4200000000");
        if (decimals != 0){
            bigDecimal = bigDecimal.movePointRight(decimals);
        }

        // 根据模型的字节，符号位，转为对应16进制, 0:无符号， 1：有符号
        String numberHexString = "";
        if ("0".equals(numberSign)){
            if (numberLength==2){
                numberHexString = numberToHex(bigDecimal.intValue(), 2);
            }else if (numberLength==4){
                numberHexString = longToHex(bigDecimal.longValue(),4);
            }else {
                numberHexString = longToHex(bigDecimal.longValue(), 8);
            }
        }else {
            if (numberLength==2){
                numberHexString = shortToHexString(bigDecimal.shortValue());
            }else if (numberLength==4){
                numberHexString = numberToHex(bigDecimal.intValue(), 4);
            }else {
                numberHexString = longToHex(bigDecimal.longValue(), 8);
            }
        }
        System.out.println(numberHexString);

        //============================== HexString -> Number
        /*BigDecimal bigDecimal = new BigDecimal(0);
        String hexString = "FFFFFFFF";
        // 0:无符号， 1:有符号
        if ("0".equals(numberSign)){
            if (numberLength==2){
                int unsignedValue2 = Integer.parseInt(hexString, 16);
                bigDecimal = new BigDecimal(unsignedValue2);
            }else {
                long unsignedValue48 = Long.parseLong(hexString, 16);
                bigDecimal = new BigDecimal(unsignedValue48);
            }
        }else{
            if (numberLength==2){
                short shortValue = hexStringToShort(hexString);
                bigDecimal = new BigDecimal(shortValue);
            }else if(numberLength==4){
                int intValue = hexString2Int(hexString);
                bigDecimal = new BigDecimal(intValue);
            }else {
                long longValue = hexString2Long(hexString);
                bigDecimal = new BigDecimal(longValue);
            }
        }
        // 如果小数位>0，左移
        if ( decimals > 0){
            bigDecimal = bigDecimal.movePointLeft(decimals);
        }
        System.out.println(bigDecimal);*/
    }
}
