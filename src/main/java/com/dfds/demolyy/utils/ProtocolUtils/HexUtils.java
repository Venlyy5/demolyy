package com.dfds.demolyy.utils.ProtocolUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author LiYangYang
 * @date 2022/11/18
 */
public class HexUtils {

    /**---------------------
     * HexStr -> uShort
     */
    public static int hexStr2uShort(String hexStr){
        return Integer.parseInt(hexStr, 16);
    }
    /**-------------------
     * HexStr -> uint
     */
    public static long hexStr2uInt(String hexStr){
        return Long.parseLong(hexStr,16);
    }

    /**---------------------
     * HexStr -> Short 有符号
     */
    public static short hexStr2Short(String hexStr){
        return Integer.valueOf(hexStr,16).shortValue();
        //return new Integer(Integer.parseUnsignedInt(hexStr,16)).shortValue();
    }

    /**--------------------
     * HexStr -> Int 有符号
     */
    public static int hexStr2Int(String hexStr){
        return Integer.parseUnsignedInt(hexStr,16);
    }

    /**---------------------
     * HexStr -> Long 有符号
     */
    public static long hexStr2Long(String hexStr){
        return Long.parseUnsignedLong(hexStr,16);
    }

    /**----------------------
     * short -> HexStr 有符号
     */
    public static String short2HexStr(short shortValue){
        String shortHex = Integer.toHexString(shortValue & 0xffff);
        int n = 4 - shortHex.length();
        if (n>0){
            StringBuilder zero = new StringBuilder();
            for (int i = 1; i <=n ; i++) {
                zero.append("0");
            }
            return zero + shortHex;
        }
        return shortHex;
    }

    /**----------------------------------------------
     * int -> HexStr标准格式 (可转无符号short, 有符号int)
     * @param byteSize 该数字的字节长度
     */
    public static String int2HexStr(Integer value, int byteSize){
        String numberHex = Integer.toHexString(value);
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

    /**--------------------------------------------------
     * Long -> HexString标准格式 (可转无符号int , 有符号long)
     * @param byteSize 该数字的字节长度
     */
    public static String long2HexStr(Long value, int byteSize){
        String numberHex = Long.toHexString(value);
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

    /**----------------
     * HexStr -> byte[]
     */
    public static byte[] hexStr2Bytes(String hexStr){
        int l = hexStr.length()/2;
        byte[] byteArray = new byte[l];
        for (int i = 0; i < l; i++) {
            // 截取str每两位转为byte
            byteArray[i] = Integer.valueOf(hexStr.substring(i*2,i*2+2),16).byteValue();
        }
        return byteArray;
    }
    /**-----------------
     * byte[] -> HexStr
     */
    public static String bytes2HexStr(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(byte2HexStr(b[i]));
        }
        return buffer.toString();
    }


    /**---------------
     * byte -> HexStr
     */
    public static String byte2HexStr(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }

    /**--------------------------------
     * 解码：HexStr+Encoding -> TextStr
     * @param hexStr 待转16进制
     * @param encoding 编码
     * @return 根据编码转换后的文本字符串
     */
    public static String hexStr2TextStr(String hexStr, String encoding){
        try {
            // 去除多余的空字符方式1：
            return new String(hexStr2Bytes(hexStr), encoding).replace("\u0000","");

            // 去除多余的空字符方式2：
            // byte[] bytes = hexStringToByteArray(hexStr);
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

    /**--------------------------------------
     * 编码：TextStr+Encoding -> HexStr
     * @param textStr 待转文本
     * @param encoding 编码 ASCII,GB2312,GBK,Unicode...
     */
    public static String textStr2HexStr(String textStr, String encoding){
        try {
            byte[] bytes = StringUtils.getBytes(textStr, encoding);
            return bytes2HexStr(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**----------------
     * float -> byte[]
     */
    public static byte[] float2bytes(float floatValue) {
        int fbit = Float.floatToIntBits(floatValue);
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

    /**---------------
     * float -> HexStr
     */
    public static String float2HexStr(float f){
        return bytes2HexStr(float2bytes(f));
    }

    /**-------------------------
     * byte[] -> float
     * @param b 字节（至少4个字节）
     * @param index 开始位置,默认填0
     */
    public static float bytes2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);

        // int accum = 0;
        // accum = accum|(b[0] & 0xff) << 0;
        // accum = accum|(b[1] & 0xff) << 8;
        // accum = accum|(b[2] & 0xff) << 16;
        // accum = accum|(b[3] & 0xff) << 24;
        return Float.intBitsToFloat(l);
    }
    /**-------------
     * HexStr -> float
     */
    public static float hexStr2Float(String hexStr){
        return bytes2float(hexStr2Bytes(hexStr), 0);
    }

    /**-------------------------
     * ByteBuf数组转16进制浮点数
     * @param buf
     * @param scale 需要保留的小数位个数
     * @return
     */
    public static Double hexBuf2Float(ByteBuf buf, int scale) {
        short[] b = new short[4];
        b[2] = buf.isReadable() ? buf.readUnsignedByte() : 0;
        b[3] = buf.isReadable() ? buf.readUnsignedByte() : 0;
        b[0] = buf.isReadable() ? buf.readUnsignedByte() : 0;
        b[1] = buf.isReadable() ? buf.readUnsignedByte() : 0;
        StringBuilder buffer = new StringBuilder();
        for (Short s : b) {
            buffer.append(Integer.toHexString(s.intValue()));
        }
        long l = Long.parseLong(buffer.toString(), 16);
        float f = Float.intBitsToFloat((int) l);
        BigDecimal decimal = new BigDecimal(f);
        return decimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**------------------
     * Double -> HexStr
     */
    public static String double2HexStr(Double doubleValue){
        return Long.toHexString(Double.doubleToLongBits(doubleValue));
    }

    /**-----------------
     * HexStr -> Double
     */
    public static Double hexStr2Double(String hexStr){
        return Double.longBitsToDouble(Long.parseLong(hexStr, 16));
    }

    /**----------------
     * double -> byte[]
     */
    public static byte[] double2Bytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return bytes;
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

    /**-------------------------------------------
     * 十六进制字符串反序: AB CD EF GH ->GH EF CD AB
     */
    private static String swapOrder(String hexStr) {
        // 转为16进制字符数组
        String[] array = formatterHex(hexStr).split(" ");
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
    public static String[] swapOrder(String[] strArr){
        int length = strArr.length;
        //第一个和最后一个交换，第二个和倒数第二个交换
        for(int i=0;i<length/2;i++){
            String temp = strArr[i];
            strArr[i] = strArr[length-1-i];
            strArr[length-1-i] = temp;
        }
        return strArr;
    }

    /**----------------
     * short -> byte[]
     * @param shortValue
     * @param len 字节数 1或2
     * @return
     */
    private static byte[] short2Bytes(short shortValue, int len) {
        if (len == 1) {
            return new byte[]{(byte) (shortValue & 0xFF)};
        } else {
            return new byte[]{(byte) (shortValue & 0xFF), (byte) ((shortValue >> Byte.SIZE) & 0xFF)};
        }
    }

    /**----------------
     * uShort -> byte[]
     */
    public static int uShort2bytes(byte... bytes) {
        int num = bytes[0] & 0xFF;
        if (bytes.length > 1) {
            num |= (bytes[1] & 0xFF) << Byte.SIZE;
        }
        return num;
    }

    /**--------------
     * byte[] -> Long
     */
    public static int LEN_LONG = Long.SIZE / Byte.SIZE; //8字节long
    public static long bytes2Long(byte[] bytes, int idx) {
        long res = bytes[idx + LEN_LONG - 1];
        for (int i = idx + LEN_LONG - 2; i >= idx; i--) {
            res = (res << Byte.SIZE) | ((long) bytes[i] & 0xFFL);
        }
        return res;
    }

    /**----------------
     * byte[] -> uLong
     */
    public static BigDecimal bytes2uLong(byte[] bytes, int idx) {
        // 7 * 8 = 2的56次方, 将ULong型数的最高位字节，从第一个字节还原所需的移动的位数
        final BigDecimal towOf56 = BigDecimal.valueOf((long) 1 << ((LEN_LONG - 1) * Byte.SIZE));

        // 1. 保存最高位
        byte high = bytes[idx + LEN_LONG - 1];
        // 2. 将最高位置0
        bytes[idx + LEN_LONG - 1] = 0;
        // 3. 将除最高字节以外的byte数组转为有符号long
        long tmp = bytes2Long(bytes, idx);
        BigDecimal num = BigDecimal.valueOf(tmp);

        // 4.将最高位字节转成对应的大小的数
        BigDecimal highNum = BigDecimal.valueOf(Byte.toUnsignedLong(high));
        // 乘以2的56次方，等同与将这个数表示的最高字节向左移动7个字节
        highNum = highNum.multiply(towOf56);

        return num.add(highNum);
    }

    /**-----------------
     * HexStr -> BinStr
     */
    public static String hexStr2binStr(String hexStr) {
        String binStr = Integer.toBinaryString(Integer.valueOf(hexStr, 16));
        return fillCharForString(binStr, "0", 8, false);
    }

    /**-------------------
     * BinStr -> HexStr
     */
    public static String binStr2HexStr(String binStr){
        return Integer.toHexString(Integer.valueOf(binStr, 2));
    }

    /**-------------
     * HexStr 格式化
     */
    public static String formatterHex(String hexStr){
        return hexStr.replaceAll("(.{2})", "$1 ");
    }

    /**-----------
     * Json 格式化
     */
    public static String formatterJson(JSONObject jsonObject){
        return JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
    }

    /**--------------------
     * boolean[] -> byte[]
     * 将 boolean[] booleans = {false,true,true,false,true,false,true,true, true,false,false,false,true,false,true,false, true}
     * 转为  byte[] = {-42, 81, 1} (HexStr: D65101)
     */
    public static byte[] booleans2bytes(boolean[] bdata) {
        int byteCount = (bdata.length + 7) / 8;
        byte[] data = new byte[byteCount];
        for(int i = 0; i < bdata.length; ++i) {
            data[i / 8] = (byte)(data[i / 8] | (bdata[i] ? 1 : 0) << i % 8);
        }
        return data;
    }

    /**---------------------
     * byte[] -> boolean[]
     */
    public static boolean[] bytes2Booleans(byte[] data) {
        boolean[] bdata = new boolean[data.length * 8];
        for(int i = 0; i < bdata.length; ++i) {
            bdata[i] = (data[i / 8] >> i % 8 & 1) == 1;
        }
        return bdata;
    }

    //---
    /**-------------------------------
     * short[] -> byte[] (Big-Endian)
     */
    public static byte[] shorts2BytesBE(short[] sdata) {
        int byteCount = sdata.length * 2;
        byte[] data = new byte[byteCount];
        for(int i = 0; i < sdata.length; ++i) {
            data[i * 2] = (byte)(255 & sdata[i] >> 8);
            data[i * 2 + 1] = (byte)(255 & sdata[i]);
        }
        return data;
    }

    /**------------------------------
     * byte[] -> short[] (Big-Endian)
     */
    public static short[] bytes2ShortsBE(byte[] data) {
        short[] sdata = new short[data.length / 2];
        for(int i = 0; i < sdata.length; ++i) {
            sdata[i] = (short)(data[i * 2] << 8 | data[i * 2 + 1] & 255);
        }
        return sdata;
    }

    /**-----------------------------
     * short -> byte[] (Big-Endian)
     */
    public static byte[] short2BytesBE(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**-----------------------------
     * byte[] -> short (Big-Endian)
     */
    public static short bytes2ShortBE(byte[] b) {
        return (short) (((b[0] << 8) | b[1] & 0xff));
    }

    /**-------------------------------
     * short -> byte[] (Little-Endian)
     */
    public static byte[] short2BytesLE(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**-------------------------------
     * byte[] -> short (Little-Endian)
     */
    public static short bytes2ShortLE(byte[] b) {
        return (short) (((b[1] << 8) | b[0] & 0xff));
    }

    /**---------------------------
     * int -> byte[] (Big-Endian)
     */
    public static byte[] int2BytesBE(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**-------------------------------
     * int -> byte[] (Little-Endian)
     */
    public static byte[] int2BytesLE(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**-----------------------------
     * byte[] -> int (Little-Endian)
     */
    public static int bytes2IntLE(byte[] bytes) {
        int int1 = bytes[0] & 0xff;
        int int2 = (bytes[1] & 0xff) << 8;
        int int3 = (bytes[2] & 0xff) << 16;
        int int4 = (bytes[3] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }

    /**---------------------------
     * byte[] -> int (Big-Endian)
     */
    public static int bytes2IntBE(byte[] bytes) {
        int int1 = bytes[3] & 0xff;
        int int2 = (bytes[2] & 0xff) << 8;
        int int3 = (bytes[1] & 0xff) << 16;
        int int4 = (bytes[0] & 0xff) << 24;
        return int1 | int2 | int3 | int4;

        // int num = bytes[3] & 0xFF;
        // num |= ((bytes[2] << 8) & 0xFF00);
        // num |= ((bytes[1] << 16) & 0xFF0000);
        // num |= ((bytes[0] << 24) & 0xFF0000);
        // return num;
    }

    /**----------------------------
     * Long -> byte[] (Big-Endian)
     */
    public static byte[] long2BytesBE(long n) {
        byte[] b = new byte[8];
        b[7] = (byte) (n & 0xff);
        b[6] = (byte) (n >> 8 & 0xff);
        b[5] = (byte) (n >> 16 & 0xff);
        b[4] = (byte) (n >> 24 & 0xff);
        b[3] = (byte) (n >> 32 & 0xff);
        b[2] = (byte) (n >> 40 & 0xff);
        b[1] = (byte) (n >> 48 & 0xff);
        b[0] = (byte) (n >> 56 & 0xff);
        return b;
    }

    /**-------------------------------
     * Long -> byte[] (Little-Endian)
     */
    public static byte[] long2BytesLE(long n) {
        byte[] b = new byte[8];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        b[4] = (byte) (n >> 32 & 0xff);
        b[5] = (byte) (n >> 40 & 0xff);
        b[6] = (byte) (n >> 48 & 0xff);
        b[7] = (byte) (n >> 56 & 0xff);
        return b;
    }

    /**------------------------------
     * byte[] -> Long (Little-Endian)
     */
    public static long bytes2LongLE(byte[] array) {
        return ((((long) array[0] & 0xff) << 0)
                | (((long) array[1] & 0xff) << 8)
                | (((long) array[2] & 0xff) << 16)
                | (((long) array[3] & 0xff) << 24)
                | (((long) array[4] & 0xff) << 32)
                | (((long) array[5] & 0xff) << 40)
                | (((long) array[6] & 0xff) << 48)
                | (((long) array[7] & 0xff) << 56));
    }

    /**----------------------------
     * byte[] -> Long (Big-Endian)
     */
    public static long bytes2LongBE(byte[] array) {
        return ((((long) array[0] & 0xff) << 56)
                | (((long) array[1] & 0xff) << 48)
                | (((long) array[2] & 0xff) << 40)
                | (((long) array[3] & 0xff) << 32)
                | (((long) array[4] & 0xff) << 24)
                | (((long) array[5] & 0xff) << 16)
                | (((long) array[6] & 0xff) << 8)
                | (((long) array[7] & 0xff) << 0));
    }
}


