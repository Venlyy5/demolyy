package com.dfds.demolyy.utils.ProtocolUtils;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class HexUtils2 {
    private static final int[] DEC = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15};
    private static final byte[] HEX = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final char[] hex = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private static final char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

    public HexUtils2() {
    }

    /**
     * 转换: 16进制字符0~F -> 10进制数字
     */
    public static int getDec(int index) {
        try {
            return DEC[index - 48];
        } catch (ArrayIndexOutOfBoundsException var2) {
            return -1;
        }
    }

    /**
     * 转换: 16进制数字0~15 -> 字节
     */
    public static byte getHex(int index) {
        return HEX[index];
    }

    public static void main(String[] args) {
        System.out.println(getHex(10));
    }

    /**
     * 转换 byte[] -> HexString
     */
    public static String toHexString(byte[] bytes) {
        if (null == bytes) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder(bytes.length << 1);

            for (int i = 0; i < bytes.length; ++i) {
                sb.append(hex[(bytes[i] & 240) >> 4]).append(hex[bytes[i] & 15]);
            }

            return sb.toString();
        }
    }

    /**
     * 转换 HexString -> byte[]
     * @param input
     * @return
     */
    public static byte[] fromHexString(String input) {
        if (input == null) {
            return null;
        } else if ((input.length() & 1) == 1) {
            // 肯定是偶数位，奇数位转换失败：一个byte对应两个字符
            throw new IllegalArgumentException("hexUtils.fromHex.oddDigits");
        } else {
            char[] inputChars = input.toCharArray();
            byte[] result = new byte[input.length() >> 1];

            for (int i = 0; i < result.length; ++i) {
                int upperNibble = getDec(inputChars[2 * i]);
                int lowerNibble = getDec(inputChars[2 * i + 1]);
                if (upperNibble < 0 || lowerNibble < 0) {
                    // 字符不存在
                    throw new IllegalArgumentException("hexUtils.fromHex.nonHex");
                }

                result[i] = (byte) ((upperNibble << 4) + lowerNibble);
            }

            return result;
        }
    }

    /**
     * byte数组 转换成 16进制小写字符串
     */
    public static String bytes2HexStr(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            hex.append(HEXES[(b >> 4) & 0x0F]);
            hex.append(HEXES[b & 0x0F]);
        }

        return hex.toString();
    }

    /**
     * byte[]数组转成字符串
     *
     * @param bytes
     * @return
     */
    public static String bytes2String(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str1 = "";
        StringBuilder sb = new StringBuilder(str1);
        for (byte element : bytes) {
            sb.append(String.valueOf(element));
        }

        return sb.toString();
    }

    /**
     * byte[]数组转成16进制大写字符串
     *
     * @param bytes
     * @return
     */
    public static String bytes2HexStrUpper(byte[] bytes) {
        String jiaoYan = bytes2String(bytes);
        BigInteger target = new BigInteger(jiaoYan);
        jiaoYan = Long.toHexString(target.longValue()).toUpperCase();
        return jiaoYan;
    }

    /**
     * byte[]数组转char[]数组
     *
     * @param bytes
     * @return
     */
    public static char[] bytes2Chars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

    /**
     * 16进制字符串 转换为对应的 byte数组
     */
    public static byte[] hex2Bytes(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length / 2];   // 如果 hex 中的字符不是偶数个, 则忽略最后一个

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
        }

        return bytes;
    }


    //十六进制字符串转字符串
    public static String fromHex(String hex) {
        return new String(decodeHex(hex.toCharArray()));
    }

    protected static byte[] decodeHex(char[] data) {
        int len = data.length;
        if ((len & 0x01) != 0) {
            throw new RuntimeException("字符个数应该为偶数");
        }
        byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f |= toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    protected static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    // /**
    //  * 计算校验和
    //  */
    // public static String sumCheck(byte[] b, int len) {
    //     int sum = 0;
    //     for (int i = 0; i < len; i++) {
    //         sum = sum + b[i];
    //     }
    //     // System.out.println(sum);
    //     /*
    //      * if(sum > 0xff){ //超过了255，使用补码（补码 = 原码取反 + 1） sum = ~sum; sum = sum +
    //  * 1; }
    //  */
    //     return Integer.toHexString((sum & 0x0ff)).toUpperCase();
    // }

    /**
     * 计算校验和
     */
    public static byte sumCheckByte(byte[] b) {
        int sum = 0;
        for (int i = 0; i < b.length; i++) {
            sum = sum + b[i];
        }
        return (byte) (sum & 0x0ff);
    }

    /**
     * 将int转为高字节在前，低字节在后的byte数组（大端）
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] intToByteBig(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组（小端）
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] intToByteLittle(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * byte数组到int的转换(小端)
     *
     * @param bytes
     * @return
     */
    public static int bytes2IntLittle(byte[] bytes) {
        int int1 = bytes[0] & 0xff;
        int int2 = (bytes[1] & 0xff) << 8;
        int int3 = (bytes[2] & 0xff) << 16;
        int int4 = (bytes[3] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }

    /**
     * byte数组到int的转换(大端)
     *
     * @param bytes
     * @return
     */
    public static int bytes2IntBig(byte[] bytes) {
        int int1 = bytes[3] & 0xff;
        int int2 = (bytes[2] & 0xff) << 8;
        int int3 = (bytes[1] & 0xff) << 16;
        int int4 = (bytes[0] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }

    /**
     * 将short转为高字节在前，低字节在后的byte数组（大端）
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] shortToByteBig(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 将short转为低字节在前，高字节在后的byte数组(小端)
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] shortToByteLittle(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 读取小端byte数组为short
     *
     * @param b
     * @return
     */
    public static short byteToShortLittle(byte[] b) {
        return (short) (((b[1] << 8) | b[0] & 0xff));
    }

    /**
     * 读取大端byte数组为short
     *
     * @param b
     * @return
     */
    public static short byteToShortBig(byte[] b) {
        return (short) (((b[0] << 8) | b[1] & 0xff));
    }

    /**
     * long类型转byte[] (大端)
     *
     * @param n
     * @return
     */
    public static byte[] longToBytesBig(long n) {
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

    /**
     * long类型转byte[] (小端)
     *
     * @param n
     * @return
     */
    public static byte[] longToBytesLittle(long n) {
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

    /**
     * byte[]转long类型(小端)
     *
     * @param array
     * @return
     */
    public static long bytesToLongLittle(byte[] array) {
        return ((((long) array[0] & 0xff) << 0)
                | (((long) array[1] & 0xff) << 8)
                | (((long) array[2] & 0xff) << 16)
                | (((long) array[3] & 0xff) << 24)
                | (((long) array[4] & 0xff) << 32)
                | (((long) array[5] & 0xff) << 40)
                | (((long) array[6] & 0xff) << 48)
                | (((long) array[7] & 0xff) << 56));
    }

    /**
     * byte[]转long类型(大端)
     *
     * @param array
     * @return
     */
    public static long bytesToLongBig(byte[] array) {
        return ((((long) array[0] & 0xff) << 56)
                | (((long) array[1] & 0xff) << 48)
                | (((long) array[2] & 0xff) << 40)
                | (((long) array[3] & 0xff) << 32)
                | (((long) array[4] & 0xff) << 24)
                | (((long) array[5] & 0xff) << 16)
                | (((long) array[6] & 0xff) << 8)
                | (((long) array[7] & 0xff) << 0));
    }

    /**
     * byte[]数组转short类型
     */
    public static short bytes2Short(byte[] bytes, int startIndex) {
        byte high = bytes[startIndex];
        byte low = bytes[startIndex + 1];
        short z = (short) (((high & 0xFF) << 8) | (0xFF & low));
        return z;
    }

    /**
     * byte[]数组转short类型，大端排序
     */
    public static short bytes2ShortBig(byte[] bytes) {
        byte high = bytes[0];
        byte low = bytes[1];
        short z = (short) (((high & 0xFF)) | (0xFF & low) >> 8);
        return z;
    }

    /**
     * short类型转byte[]数组，
     *
     * @param s
     * @return
     */
    public static byte[] unsignedShortToByte2(short s) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (0xFF & s);
        targets[1] = (byte) (0x00FF & (s >> 8));
        return targets;
    }

    /**
     * short类型转byte[]，高前 低后
     */
    public static byte[] shortToByte(short s) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (0xFF & s);
        targets[1] = (byte) (0x00FF & (s >> 8));
        return targets;
    }

    /**
     * byte[]数组转short类型，得到字节数组长度
     *
     * @param bytes
     * @param startIndex
     * @return
     */
    public static short bytes2ShortLen(byte[] bytes, int startIndex) {
        byte high = bytes[startIndex + 1];
        byte low = bytes[startIndex];
        short z = (short) (((high & 0xFF) << 8) | (0xFF & low));
        return z;
    }

    /**
     * int转byte[]
     *
     * @param i
     * @return
     */
    public static byte[] int2Bytes(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @return
     */
    public static int bytes2Int(byte[] bytes) {
        int num = bytes[3] & 0xFF;
        num |= ((bytes[2] << 8) & 0xFF00);
        num |= ((bytes[1] << 16) & 0xFF0000);
        num |= ((bytes[0] << 24) & 0xFF0000);
        return num;
    }
}

