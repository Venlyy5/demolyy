package com.dfds.demolyy.utils.ProtocolUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.serotonin.modbus4j.base.ModbusUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;

/**-------------------
 * 16进制处理工具
 * @author LiYangYang
 * @date 2022/11/18
 */
public class HexUtils {
    /**-----------------
     * HexString -> Int
     */
    public static int hexString2Int(String numberHex){
        return Integer.parseUnsignedInt(numberHex,16);
    }

    /**-----------------
     * HexString -> Long
     */
    public static long hexString2Long(String numberHex){
        return Long.parseUnsignedLong(numberHex,16);
    }

    /**---------------------
     * HexString -> Short
     */
    public static short hexStringToShort(String hexString){
        return Integer.valueOf(hexString,16).shortValue();
        //return new Integer(Integer.parseUnsignedInt(hexString,16)).shortValue();
    }

    /**-------------------
     * short -> HexString
     */
    public static String shortToHexString(short num){
        String numberHex = Integer.toHexString(num & 0xffff);
        int n = 4 - numberHex.length();
        if (n>0){
            StringBuilder zero = new StringBuilder();
            for (int i = 1; i <=n ; i++) {
                zero.append("0");
            }
            return zero + numberHex;
        }
        return numberHex;
    }

    /**-----------------------------
     * int -> HexString标准格式
     * @param byteSize 该数字的字节长度
     */
    public static String numberToHex(Integer number, int byteSize){
        String numberHex = Integer.toHexString(number);
        int n = byteSize*2 - numberHex.length();

        if (n > 0){
            StringBuilder zero = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                zero.append("0");
            }
            return zero + numberHex;
        }
        return numberHex;
    }

    /**--------------------------
     * Long -> HexString标准格式
     */
    public static String longToHex(Long number, int byteSize){
        String numberHex = Long.toHexString(number);
        int n = byteSize*2 - numberHex.length();

        if (n > 0){
            StringBuilder zero = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                zero.append("0");
            }
            return zero + numberHex;
        }
        return numberHex;
    }

    /**-------------------
     * HexString -> byte[]
     */
    public static byte[] hexStringToByteArray(String str){
        int l = str.length()/2;
        byte[] byteArray = new byte[l];
        for (int i = 0; i < l; i++) {
            // 截取str每两位转为byte
            byteArray[i] = Integer.valueOf(str.substring(i*2,i*2+2),16).byteValue();
        }
        return byteArray;
    }

    /**---------------------
     * byte[] -> HexString
     */
    public static String byteArray2HexString(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(byte2HexString(b[i]));
        }
        return buffer.toString();
    }

    /**--------------------
     * byte -> HexString
     */
    public static String byte2HexString(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }

    /**--------------------------------------
     * 编码：TextString+Encoding -> HexString
     * @param textStr 待转文本
     * @param encoding 编码 ASCII,GB2312,GBK,Unicode...
     */
    public static String textStr2HexStr(String textStr, String encoding){
        try {
            byte[] bytes = StringUtils.getBytes(textStr, encoding);
            return byteArray2HexString(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**--------------------------------------
     * 解码：HexString+Encoding -> TextString
     * @param hexString 待转16进制
     * @param encoding 编码
     */
    public static String hexStr2TestStr(String hexString, String encoding){
        try {
            // 去除多余的空字符方式1：
            return new String(hexStringToByteArray(hexString), encoding).replace("\u0000","");

            // 去除多余的空字符方式2：
            // byte[] bytes = hexStringToByteArray(hexString);
            // int length = 0;
            // for (int i = 0; i < bytes.length; i++) {
            //     if (bytes[i] == 0){
            //         length = i;
            //         break;
            //     }
            // }
            // return new String(bytes,0, length, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**--------------------
     * float -> byte[]
     */
    public static byte[] float2byte(float f) {
        int fbit = Float.floatToIntBits(f);
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }
        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }

    /**
     * float -> HEX
     */
    public static String float2HexString(float f){
        return byteArray2HexString(float2byte(f));
    }

    /**-------------------------
     * byte[] -> float
     * @param b 字节（至少4个字节）
     * @param index 开始位置
     */
    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }
    /**
     * HEX -> float
     */
    public static float hexString2Float(String hexString){
        return byte2float(hexStringToByteArray(hexString), 0);
    }

    /**------------------
     * Double -> HEX
     * @param doubleValue
     * @return HEX
     */
    public static String double2Hex(Double doubleValue){
        return Long.toHexString(Double.doubleToLongBits(doubleValue));
    }

    /**--------------
     * Hex -> Double
     * @param hex
     * @return Double
     */
    public static Double hex2Double(String hex){
        return Double.longBitsToDouble(Long.parseLong(hex, 16));
    }

    /**----------------
     * double -> byte[]
     */
    public static byte[] double2Bytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }

    /**------------------------------------------
     * 给字符串补位 用指定字符，指定长度，指定前补后补
     * @param str 原始字符串
     * @param value 补位的字符串 必须是单个字符, 默认"0"
     * @param strLength 最终的字符串长度
     * @param position false 左补；true：右补， 默认左补
     */
    public static String fillCharForString(String str, String value, int strLength, boolean position) {
        if (StringUtils.isBlank(str)){
            str = "";
        }
        int strLen = str.length();
        if (StringUtils.isBlank(value)){
            value = "0";
        }
        while (strLen < strLength) {
            StringBuffer sb = new StringBuffer(str);
            if (position){
                sb.append(value);//右补
            }else{
                sb.insert(0, value);// 左补
            }
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }

    /**-------------------------------------------------
     * 十六进制字符串反序: AB CD EF GH ->GH EF CD AB
     */
    private static String swapOrder(String hexString) {
        // 转为16进制字符数组
        String[] array = formatterHex(hexString).split(" ");
        String[] swapOrder = swapOrder(array);
        StringBuffer result = new StringBuffer();
        for (String string : swapOrder) {
            result.append(string);
        }
        return result.toString();
    }
    /**
     * 字符数组反序: {"AB", "CD", "EF", "GH"} -> {"GH", "EF", "CD", "AB"}
     */
    public static String[] swapOrder(String[] arr){
        int length = arr.length;
        //第一个和最后一个交换，第二个和倒数第二个交换
        for(int i=0;i<length/2;i++){
            String temp = arr[i];
            arr[i] = arr[length-1-i];
            arr[length-1-i] = temp;
        }
        return arr;
    }

    /**
     * short -> byte[]
     * @param num
     * @param len
     * @return
     */
    private static byte[] getBytes(short num, int len) {
        if (len == 1) {
            return new byte[]{(byte) (num & 0xFF)};
        } else {
            return new byte[]{(byte) (num & 0xFF), (byte) ((num >> Byte.SIZE) & 0xFF)};
        }
    }

    /**
     * unsigned short -> byte[]
     * @param bytes
     * @return
     */
    public static int getUShort(byte... bytes) {
        int num = bytes[0] & 0xFF;
        if (bytes.length > 1) {
            num |= (bytes[1] & 0xFF) << Byte.SIZE;
        }
        return num;
    }

    /**-----------------------------------------------------------
     * byte[] -> Long (有符号)
     */
    public static int LEN_LONG = Long.SIZE / Byte.SIZE; //8字节long
    public static long getLong(byte[] bytes, int idx) {
        long res = bytes[idx + LEN_LONG - 1];
        for (int i = idx + LEN_LONG - 2; i >= idx; i--) {
            res = (res << Byte.SIZE) | ((long) bytes[i] & 0xFFL);
        }
        return res;
    }

    /**
     * byte[] -> unsigned long
     */
    public static BigDecimal getULong(byte[] bytes, int idx) {
        // 7 * 8 = 2的56次方, 将ULong型数的最高位字节，从第一个字节还原所需的移动的位数
        final BigDecimal towOf56 = BigDecimal.valueOf((long) 1 << ((LEN_LONG - 1) * Byte.SIZE));

        // 1. 保存最高位
        byte high = bytes[idx + LEN_LONG - 1];
        // 2. 将最高位置0
        bytes[idx + LEN_LONG - 1] = 0;
        // 3. 将除最高字节以外的byte数组转为有符号long
        long tmp = getLong(bytes, idx);
        BigDecimal num = BigDecimal.valueOf(tmp);

        // 4.将最高位字节转成对应的大小的数
        BigDecimal highNum = BigDecimal.valueOf(Byte.toUnsignedLong(high));
        // 乘以2的56次方，等同与将这个数表示的最高字节向左移动7个字节
        highNum = highNum.multiply(towOf56);

        return num.add(highNum);
    }

    /**-----------------------------
     * HexString -> BinaryString
     */
    public static String hexString2binaryString(String hexString) {
        String binaryString = Integer.toBinaryString(Integer.valueOf(hexString, 16));
        return fillCharForString(binaryString, "0", 8, false);
    }

    /**
     * BinaryString -> HexString
     */
    public static String binaryString2HexString(String binaryString){
        return Integer.toHexString(Integer.valueOf(binaryString, 2));
    }

    /**-----------------
     * 16进制字符串 格式化
     */
    public static String formatterHex(String strHex){
        return strHex.replaceAll("(.{2})", "$1 ");
    }

    /**-----------
     * Json 格式化
     */
    public static String jsonFormat(JSONObject jsonObject){
        return JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }


    /**--------------------
     * boolean[] -> byte[]
     */
    public static byte[] convertToBytes(boolean[] bdata) {
        int byteCount = (bdata.length + 7) / 8;
        byte[] data = new byte[byteCount];
        for(int i = 0; i < bdata.length; ++i) {
            data[i / 8] = (byte)(data[i / 8] | (bdata[i] ? 1 : 0) << i % 8);
        }
        return data;
    }

    /**
     * short[] -> byte[]
     */
    public static byte[] convertToBytes(short[] sdata) {
        int byteCount = sdata.length * 2;
        byte[] data = new byte[byteCount];
        for(int i = 0; i < sdata.length; ++i) {
            data[i * 2] = (byte)(255 & sdata[i] >> 8);
            data[i * 2 + 1] = (byte)(255 & sdata[i]);
        }
        return data;
    }

    /**---------------------
     * byte[] -> boolean[]
     */
    public static boolean[] convertToBooleans(byte[] data) {
        boolean[] bdata = new boolean[data.length * 8];
        for(int i = 0; i < bdata.length; ++i) {
            bdata[i] = (data[i / 8] >> i % 8 & 1) == 1;
        }
        return bdata;
    }

    /**--------------------
     * byte[] -> short[]
     */
    public static short[] convertToShorts(byte[] data) {
        short[] sdata = new short[data.length / 2];
        for(int i = 0; i < sdata.length; ++i) {
            sdata[i] = (short)(data[i * 2] << 8 | data[i * 2 + 1] & 255);
        }
        return sdata;
    }
}


