package com.example.demo;
import com.dfds.demolyy.utils.ProtocolUtils.HexUtils;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * uint64测试
 */
public class ULongTest {
    // uLong最大值
    private static final String uLong_MAX_VALUE = "18446744073709551615";
    //private static final String uLong_MAX_VALUE = "9923372036854775807";
    private static final String uLong_MAX_VALUE_HEX = "FFFFFFFFFFFFFFFF";

    /**------------------
     * uLong与Hex互转测试
     * uLongStr -> Hex ;
     * Hex -> uLongStr
     */
    @Test
    void test1(){
        // uLongStr -> long
        long longValue = Long.parseUnsignedLong(uLong_MAX_VALUE);
        System.out.println(longValue);

        // long -> Hex
        String hexStr = HexUtils.long2HexStr(longValue, 8);
        System.out.println(hexStr);

        // Hex -> uLongStr
        String uLongStr = Long.toUnsignedString(longValue);
        System.out.println(uLongStr);

        // uLongStr -> BigDecimal
        BigDecimal bigDecimal = new BigDecimal(uLong_MAX_VALUE);
        System.out.println(bigDecimal);

        // Hex -> BigInteger
        BigInteger bigInteger = new BigInteger(uLong_MAX_VALUE_HEX, 16);
        System.out.println(bigInteger);
    }

    /**--------------------
     * 打印Long最大最小值
     */
    @Test
    public void MaxAndMin() {
        outLongInfo(Long.MAX_VALUE);
        outLongInfo(Long.MIN_VALUE);
    }

    @Test
    public void test3() {
        // 大于MAX，是无符号正数，但，还是64位的。
        outLongInfo(unsigned2Long1(uLong_MAX_VALUE));
        outLongInfo(unsigned2Long2(uLong_MAX_VALUE));

        System.out.println(longParseUnsigned(-216172773253120239L));
        System.out.println(longParseUnsigned(-1));
    }

    /**
     * 打印long的 value 二进制value 二进制value长度
     * @param aLong java long value
     */
    private void outLongInfo(long aLong) {
        System.out.println("              long value is \t" + aLong);
        System.out.println("       long binary value is \t" + Long.toBinaryString(aLong));
        System.out.println("long binary value length is \t" + Long.toBinaryString(aLong).length());
    }

    /**
     * 方法1：unsigned long 2 signed long
     * @param s unsigned long string
     */
    private long unsigned2Long1(String s) {
        return Long.parseUnsignedLong(s);
    }

    /**
     * 方法2：unsigned long 2 signed long
     * @param s unsigned long string
     */
    private long unsigned2Long2(String s) {
        return new BigDecimal(s).longValue();
    }

    /**
     * long转成无符号数
     */
    public static BigDecimal longParseUnsigned(long value) {
        if (value >= 0) {
            return new BigDecimal(value);
        }
        // 按位与操作，就是把负数给转成相应的正数，比如-10 转成 10
        long lowValue = value & Long.MAX_VALUE;
        // 然后再左移 1 位，然后在最低位 + 1 。跟下面的 dd() 一样的逻辑。
        return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE)).add(BigDecimal.valueOf(1));
    }

    @Test
    public void dd() {
        // 乘以2，相当于左移 1 位，最后一位是0，然后再加个1，就64个1了。
        BigDecimal multiply = new BigDecimal(Long.MAX_VALUE).multiply(new BigDecimal(2));
        System.out.println("Long.MAX_VALUE * 2 = " + multiply.toPlainString());
        BigDecimal subtract = multiply.add(new BigDecimal(1));
        System.out.println("Long.MAX_VALUE * 2 + 1 = " + subtract.toPlainString());
        System.out.println("64 个 1 的 无符号数 = " + uLong_MAX_VALUE);
    }
}
