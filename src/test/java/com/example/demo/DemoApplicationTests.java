package com.example.demo;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.CharsetUtil;
import com.dfds.demolyy.utils.ProtocolUtils.HexUtils;
import com.dfds.demolyy.utils.iot_communicationUtils.FloatUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.*;

import static com.dfds.demolyy.utils.ProtocolUtils.HexUtils.*;
import static com.sun.xml.internal.fastinfoset.util.ValueArray.MAXIMUM_CAPACITY;
import static java.lang.Math.pow;

//@SpringBootTest
class DemoApplicationTests {

    @Test
    void test6(){

    }

    @Test
    void test5(){
        // 基本类型  栈
        int a = 5;
        int b = 5;
        System.out.println(a==b);

        // 包装类型  堆
        Integer integerA = new Integer(4);
        Integer integerB = new Integer(4);
        System.out.println(integerA == integerB);
        System.out.println(integerA.equals(integerB));
    }

    /**-----------------------------
     * 反射测试
     * 在运行状态中，对任意一个类，都能知道这个类的所有属性和方法，对任意一个对象，都能 调用它的任意一个方法和属性。这种能动态获取信息及动态调用对象方法的功能称为 java 语言的反射机制。
     * 反射的作用：开发过程中，经常会遇到某个类的某个成员变量、方法或属性是私有的，或只 对系统应用开放，这里就可以利用 java 的反射机制通过反射来获取所需的私有成员或是方 法。
     */
    @Test
    void reflectionTest() throws Exception {
        // 1.获取类的 Class 对象实例
        Class<?> clz = Class.forName("com/example/demo/ULongTest.java");
        // 2.根据Class对象实例获取 Constructor 对象
        Constructor appConstructor = clz.getConstructor();
        // 3.使用 Constructor 对象的 newInstance 方法获取反射类对象
        Object appleObj = appConstructor.newInstance();
        // 4.获取方法的Method对象
        Method method = clz.getMethod("MaxAndMin"); // 若有参数，还要顺序列出参数类型

        // 5.利用invoke方法调用方法
        method.invoke(appleObj);// 若有参数，还要顺序填入参数

        //通过 getFields()可以获取 Class 类的属性，但无法获取私有属性，没有 Declared 修饰的只能用来反射公有的方法
        Field[] fields = clz.getFields();
        System.out.println(Arrays.toString(fields));
        //而 getDeclaredFields()可 以获取到包括私有属性在内的所有属性。带有 Declared 修饰的方法可以反射到私有的方法
        Field[] declaredFields = clz.getDeclaredFields();
        System.out.println(Arrays.toString(declaredFields));
    }

    /**
     * 创建线程测试
     */
    @Test
    void threadTest(){
        System.out.println(Thread.currentThread().getId());

        // 1.匿名内部类方式
        new Thread(){
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getId());
            }
        }.start();

        // 2.线程池的实现(java.util.concurrent.Executor接口)
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getId());
            }
        });
        executorService.shutdown();
    }
    /**---------------------
     * 寻找Array.class文件
     * 类加载器的findResources(name)方法会遍历其负责加载的所有 jar 包，找到 jar 包中名称为 name 的资源文件，这里的资源可以是任何文件，甚至是.class 文件
     */
    @Test
    void getSources() throws IOException {
        // 运行后可以得到如下结果：$JAVA_HOME/jre/lib/rt.jar!/java/sql/Array.class
        String name = "java/sql/Array.class"; // Array.class的完整路径
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(name);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            System.out.println(url.toString());
        }
    }

    /**------------------------------------
     * float精度丢失问题, double, 16进制, 字节 转换测试
     */
    @Test
    void floatCoverTest(){
        //float floatValue = 4.22354584F;
        float floatValue = 282734.22354584F;  //c70d8a48
        System.out.println("把一个超过float精度的数给floatValue时, 精度就丢失了, 并非在转换时丢失: "+ floatValue);

        NumberFormat instance = NumberFormat.getInstance();
        instance.setGroupingUsed(false);
        System.out.println("NumberFormat: "+ instance.format(floatValue));

        DecimalFormat decimalFormat = new DecimalFormat("##########.#####");
        System.out.println("DecimalFormat: "+ decimalFormat.format(floatValue));

        BigDecimal bigDecimal = new BigDecimal(floatValue);
        System.out.println("BigDecimal.toPlainString: "+ bigDecimal.toPlainString());

        byte[] bytes = float2bytes(floatValue);
        System.out.println("float -> byte[]: " + Arrays.toString(bytes));
        String hexStr = bytes2HexStr(bytes); //C70D8A48
        System.out.println("byte[] -> HexStr: " + hexStr);
        System.out.println("HexStr -> float: "+ hexStr2Float(hexStr));

        // 转换方式2
        byte[] bytes2 = FloatUtil.toByteArray(floatValue, true);
        System.out.println("Float -> byte3[]: "+ Arrays.toString(bytes2));

        //==================  Double测试
        byte[] doubleBytes = double2Bytes(282734.22354584D);
        System.out.println("Double -> byte[]: "+ Arrays.toString(doubleBytes));
        System.out.println("byte[] -> Double: "+ FloatUtil.toFloat64(doubleBytes,0,true));

        System.out.println("Double -> HEX: "+ double2HexStr(282734.22354584D)); //411141b8e4e93360
        System.out.println("HEX -> Double: "+ hexStr2Double("411141b8e4e93360"));
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
        System.out.println("byte[] -> HexString: "+ bytes2HexStr(bytes));

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
        System.out.println("int -> HexString: "+ HexUtils.int2HexStr(-767, 2)); // "fffffd01"
        System.out.println("int -> HexString: "+ Integer.toUnsignedString(-767, 16)); // "fffffd01"
        System.out.println("HexString -> int: "+ Integer.parseUnsignedInt("FFFFFD01", 16)); //-767
        // 3.number 8字节, -9223372036854775808 ~ 9223372036854775807
        System.out.println("long -> HexString: "+ HexUtils.long2HexStr(-9223372036854775808L,8)); //"8000000000000000"
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
