package com.example.demo;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.CharsetUtil;
import com.dfds.demolyy.utils.ProtocolUtils.HexUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteOrder;
import java.util.Arrays;
import static com.dfds.demolyy.utils.ProtocolUtils.HexUtils.*;

//@SpringBootTest
class DemoApplicationTests {

    /**
     * float,16进制,字节 转换测试
     */
    @Test
    void floatCoverTest(){
        byte[] bytes = float2byte(9.40646F);
        System.out.println("Float -> byte[]: "+ Arrays.toString(bytes));
        System.out.println("byte[] -> Float: "+ byte2float(bytes,0) +"\r\n");

        byte[] bytes2 = HexUtils.hexStringToByteArray("DC801641");
        System.out.println("HEX -> byte[]: "+ Arrays.toString(bytes2));
        System.out.println("byte[] -> HEX: "+ HexUtils.byteArray2HexString(bytes2));

        System.out.println("Double -> HEX: "+ double2Hex(Math.PI));
        System.out.println("HEX -> Double: "+ hex2Double("400921fb54442d18"));
    }

    /**--------------
     * BigDecimal测试
     */
    @Test
    void bidDecimalTest() {
        // 去除尾部多余的0, 再转为String(不用科学计数法)
        System.out.println(new BigDecimal("10.0500").movePointLeft(5).stripTrailingZeros().toPlainString());
    }

    /**
     * 数字,文本字转换测试, 无符号数字测试
     */
    @Test
    void numberTest() throws UnsupportedEncodingException {
        //============= Linux和Windows都是小端模式
        byte[] bytes = ByteUtil.intToBytes(8934, ByteOrder.LITTLE_ENDIAN);
        System.out.println("int -> byte[]:"+ Arrays.toString(bytes));
        System.out.println("byte[] -> int: "+ ByteUtil.bytesToInt(bytes));
        System.out.println("byte[] -> HexString: "+ byteArray2HexString(bytes));

        //============= 任何编码集: 必须兼容ASCII编码表; 英文和数字只占1个字节
        // GBK(中文)/GB2312(简体中文)/BIG5(繁体中文)：中文每个字符占用2个字节
        // Unicode(国际码表):每个字符占2个字节，Java中存储字符类型使用Unicode编码,基于Unicode字符集的编码方式有UTF-8、UTF-16及UTF-32
        // UTF-8(国际码表): 中文每个字符占用3个字节
        byte[] bytesStr = StringUtils.getBytes("你好啊, Hello World!", "GBK");
        System.out.println("文本 -> byte[]: "+ Arrays.toString(bytesStr));
        System.out.println("byte[] -> 文本: " + StringUtils.toEncodedString(bytesStr, CharsetUtil.CHARSET_GBK));

        //==============
        //如果为 无符号
        // 1.number 2字节, 0 ~ 65535, 使用更大一级的进行转换
        System.out.println("HexString -> uint16: "+ Integer.parseInt("FFFF", 16)); //65535
        // 2.number 4字节, 0 ~ 4294967295, 使用更大一级的进行转换
        System.out.println("HexString -> uint32: "+ Long.parseLong("FFFFFFFF", 16)); // 4294967295

        //如果为 有符号
        // 1.number 2字节, -32768 ~ 32767
        short a = -767;
        System.out.println("short -> HexString: "+ short2HexString(a)); //FD01
        System.out.println("HexString -> short: "+ Integer.valueOf("FD01", 16).shortValue()); // -767
        // 2.number 4字节, -2147483648 ~ 2147483647
        System.out.println("int -> HexString: "+ HexUtils.numberToHex(-767, 2)); // "fffffd01"
        System.out.println("int -> HexString: "+ Integer.toUnsignedString(-767, 16)); // "fffffd01"
        System.out.println("HexString -> int: "+ Integer.parseUnsignedInt("FFFFFD01", 16)); //-767
        // 3.number 8字节, -9223372036854775808 ~ 9223372036854775807
        System.out.println("long -> HexString: "+ HexUtils.longToHex(-9223372036854775808L,8)); //"8000000000000000"
        System.out.println("HexString -> long: "+ Long.parseUnsignedLong("8000000000000000",16)); //-9223372036854775808
    }
    /**
     * ===================================
     * 8bit unsigned integer －－－> short
     * 8bit signed integer －－－> byte
     * 16bit unsigned integer －－－> int
     * 16bit signed integer －－－> short
     * 32bit unsigned integer －－－> long
     * 32bit signed integer －－－> int
     */
    public static int getUnsignedByte(byte data) {      //将data字节型数据转换为0~255 (0xFF 即BYTE)。
        return data & 0x0FF;
    }

    public static int getUnsignedShort(short data) {      //将data字节型数据转换为0~65535 (0xFFFF 即 WORD)。
        return data & 0x0FFFF;
    }

    public static long getUnsignedInt(int data) {     //将int数据转换为0~4294967295 (0xFFFFFFFF即DWORD)。
        return data & 0x0FFFFFFFF;
    }


    /**---------------------
     * HexString -> short
     */
    public static short HexString2Short(String numberHex){
        return Integer.valueOf(numberHex,16).shortValue();
    }
    /**
     * ---------------------
     * short -> HexString
     */
    public static String short2HexString(short num) {
        return Integer.toHexString(num & 0xffff);
    }
}
