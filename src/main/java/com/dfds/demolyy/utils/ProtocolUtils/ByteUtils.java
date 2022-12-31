package com.dfds.demolyy.utils.ProtocolUtils;

import java.nio.charset.Charset;

public class ByteUtils {
    /**
     * short -> byte[]
     */
    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    /**
     * char -> byte[]
     */
    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    /**
     * int -> byte[]
     */
    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    /**
     * long -> byte[]
     */
    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    /**
     * float -> byte[]
     */
    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    /**
     * double -> byte[]
     */
    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    /**
     * 文本 -> 指定编码byte[]
     * @param data 文本字符串
     * @param charsetName 编码
     */
    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    /**
     * byte[] -> short
     */
    public static short getShort(byte[] bytes) {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    /**
     * byte[] -> char
     */
    public static char getChar(byte[] bytes) {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    /**
     * byte[] -> int
     */
    public static int getInt(byte[] bytes) {
        return    (0xff & bytes[0])
                | (0xff00 & (bytes[1] << 8))
                | (0xff0000 & (bytes[2] << 16))
                | (0xff000000 & (bytes[3] << 24));
    }

    /**
     * byte[] -> long
     */
    public static long getLong(byte[] bytes) {
        return    (0xffL & (long)bytes[0])
                | (0xff00L & ((long)bytes[1] << 8))
                | (0xff0000L & ((long)bytes[2] << 16))
                | (0xff000000L & ((long)bytes[3] << 24))
                | (0xff00000000L & ((long)bytes[4] << 32))
                | (0xff0000000000L & ((long)bytes[5] << 40))
                | (0xff000000000000L & ((long)bytes[6] << 48))
                | (0xff00000000000000L & ((long)bytes[7] << 56));
    }

    /**
     * byte[] -> float
     */
    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    /**
     * byte[] -> double
     */
    public static double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        return Double.longBitsToDouble(l);
    }

    /**
     * byte[] -> 指定编码文本
     * @param bytes
     * @param charsetName 编码
     * @return 文本字符串
     */
    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static void main(String[] args) {
        System.out.println(getShort(getBytes((short)123)));
        System.out.println(getInt(getBytes(456789)));
        System.out.println(getLong(getBytes(987654321L)));
        System.out.println(getChar(getBytes('K')));
        System.out.println(getFloat(getBytes(123.123F)));
        System.out.println(getDouble(getBytes(456.456D)));
        System.out.println(getString(getBytes("GBK编码字符串", "GBK"), "GBK"));
    }
}
