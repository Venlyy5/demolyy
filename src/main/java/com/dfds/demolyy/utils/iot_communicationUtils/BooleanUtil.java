package com.dfds.demolyy.utils.iot_communicationUtils;

import com.dfds.demolyy.utils.ProtocolUtils.HexUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BooleanUtil {

    private BooleanUtil() {
        // NOOP
    }

    /**
     * 获取0或1的字节
     * @param data
     * @return
     */
    public static byte toByte(boolean data) {
        return setBit((byte) 0x00, 0, data);
    }

    /**
     * 对字节0的指定位设置1或0
     *
     * @param bit 位数
     * @param res true：1，false：0
     * @return 新的字节
     */
    public static byte setBit(int bit, boolean res) {
        return setBit((byte) 0x00, bit, res);
    }

    /**
     * 对指定字节的指定位设置1或0
     *
     * @param data 字节数据
     * @param bit  位数
     * @param res  true：1，false：0
     * @return 新的字节
     */
    public static byte setBit(byte data, int bit, boolean res) {
        if (bit > 7 || bit < 0) {
            throw new IndexOutOfBoundsException("0<=bit<=7");
        }
        return res ? (byte) (((data & 0xFF) | (1 << bit)) & 0xFF) : (byte) ((data & 0xFF) & ~(1 << bit) & 0xFF);
    }

    /**
     * 获取字节指定位的状态
     *
     * @param data 字节数据
     * @param bit  位数 0-7
     * @return 结果状态，true，false
     */
    public static boolean getValue(byte data, int bit) {
        if (bit > 7 || bit < 0) {
            throw new IndexOutOfBoundsException("0<=bit<=7");
        }
        return (((data & 0xFF) & (1 << bit)) != 0);
    }

    /**
     * 提取指定字节数组,指定数量的boolean值
     * {118, 14} -> [false, true, true, false, true, true, true, false, false, false, true, true, true, true, true, true]
     * @param quantity 读取的bool数量
     * @param bytes  字节数组数据
     * @return boolean列表
     */
    public static List<Boolean> bytes2BooleanList(int quantity, byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes");
        }
        if (quantity > bytes.length * 8) {
            throw new IllegalArgumentException("读取的位数超过字节");
        }
        int count = 1;
        List<Boolean> res = new ArrayList<>();
        for (byte data : bytes) {
            for (int j = 0; j < 8; j++) {
                if (count <= quantity) {
                    res.add(BooleanUtil.getValue(data, j));
                    count++;
                }
            }
        }
        return res;
    }

    /**
     * 将boolean列表转换为字节数组
     * booleanList最好为8的倍数, 否则不满8的位置默认给false
     * [false, true, true, false, true, true, true, false, false, false, true, true, true, true, true, true] -> {118, -4}
     * @param list boolean列表
     * @return 字节数组
     */
    public static byte[] booleanList2Bytes(List<Boolean> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list为空");
        }
        int index = 0;
        // 如果boolList数量不是8的倍数,需要多加一个字节
        int a = list.size() % 8 == 0 ? 0 : 1;
        byte[] values = new byte[list.size() / 8 + a];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < 8; j++) {
                if (index < list.size()) {
                    values[i] = BooleanUtil.setBit(values[i], j, list.get(index));
                    index++;
                }
            }
        }
        return values;
    }
}
