package com.dfds.demolyy.utils.otherUtils;

import java.math.BigDecimal;

/**
 * double, float运算工具
 */
public class BigDecimalUtils {
    public static BigDecimal doubleAdd(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }
    public static BigDecimal floatAdd(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.add(b2);
    }
    public static BigDecimal doubleSub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }
    public static BigDecimal floatSub(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.subtract(b2);
    }

    public static BigDecimal doubleMul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }
    public static BigDecimal floatMul(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.multiply(b2);
    }

    public static BigDecimal doubleDiv(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        // 保留小数点后两位 ROUND_HALF_UP = 四舍五入
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
    }
    public static BigDecimal floatDiv(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        // 保留小数点后两位 ROUND_HALF_UP = 四舍五入
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 比较v1 v2大小
     * @param v1
     * @param v2
     * @return v1>v2 return 1  v1=v2 return 0 v1<v2 return -1
     */
    public static int doubleCompareTo(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return  b1.compareTo(b2);
    }
    public static int floatCompareTo(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return  b1.compareTo(b2);
    }

    /**--------------------------------
     * 求 double 的 exponent次幂
     */
    public static double doublePower(double base, int exponent){
        //如果底数等于0并且指数小于0
        if(doubleEqual(base,0.0) && exponent<0){
            return 0.0;
        }

        int absexponent = exponent;
        //如果指数小于0，将指数转正
        if (exponent<0){
            absexponent = -exponent;
        }
        //getPower方法求出base的exponent次方。
        double res = getPower(base, absexponent);

        //如果指数小于0，所得结果为上面求的结果的倒数
        if (exponent<0){
            res = 1.0/res;
        }

        return res;
    }

    /**----------
     * 求b的e次方
     */
    public static double getPower(double b,int e){
        // 如果指数为0，返回1
        if(e==0){
            return 1.0;
        }
        // 如果指数为1，返回b
        if(e==1){
            return b;
        }

        // e>>1相等于e/2,这里就是求a^n=(a^n/2)*(a^n/2)
        double result = getPower(b,e>>1);
        result *= result;
        // 如果指数n为奇数，则要再乘一次底数base
        if((e&1)==1){
            result *= b;
        }

        return result;
    }

    /**-------------------
     * 比较两个double是否相等
     */
    public static boolean doubleEqual(double num1, double num2){
        if(num1-num2>-0.000001 && num1-num2<0.000001)
            return true;
        else
            return false;
    }

    public static void main(String[] args) {
        System.out.println(doublePower(5.1, 3));
        System.out.println(Math.pow(5.1, 3));
    }
}