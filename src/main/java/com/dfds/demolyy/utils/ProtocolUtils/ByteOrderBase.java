package com.dfds.demolyy.utils.ProtocolUtils;

import lombok.Data;

/**
 * 字节序
 */
@Data
public class ByteOrderBase {

    private EByteBuffFormat byteBuffFormat;

    public ByteOrderBase() {
        this(EByteBuffFormat.DC_BA);
    }

    public ByteOrderBase(EByteBuffFormat byteBuffFormat) {
        this.byteBuffFormat = byteBuffFormat;
    }

    /**
     * 4字节数据根据格式重新排序
     *
     * @param data 字节数组数据
     * @return 4字节数组重排结果
     */
    protected byte[] reorderByFormatIn4Bytes(byte[] data) {
        return this.reorderByFormatIn4Bytes(data, 0);
    }

    /**
     * 4字节数据根据格式重新排序
     *
     * @param data  字节数组数据
     * @param index 索引
     * @return 4字节数组重排结果
     */
    protected byte[] reorderByFormatIn4Bytes(byte[] data, int index) {
        byte[] res = new byte[4];
        switch (this.byteBuffFormat) {
            case AB_CD:
                res[0] = data[index + 3];
                res[1] = data[index + 2];
                res[2] = data[index + 1];
                res[3] = data[index + 0];
                break;
            case BA_DC:
                res[0] = data[index + 2];
                res[1] = data[index + 3];
                res[2] = data[index + 0];
                res[3] = data[index + 1];
                break;
            case CD_AB:
                res[0] = data[index + 1];
                res[1] = data[index + 0];
                res[2] = data[index + 3];
                res[3] = data[index + 2];
                break;
            case DC_BA:
                res[0] = data[index + 0];
                res[1] = data[index + 1];
                res[2] = data[index + 2];
                res[3] = data[index + 3];
                break;
            default:
                break;
        }
        return res;
    }

    /**
     * 8字节数据根据格式重新排序
     *
     * @param data 字节数组数据
     * @return 8字节数组重排结果
     */
    protected byte[] reorderByFormatIn8Bytes(byte[] data) {
        return this.reorderByFormatIn8Bytes(data, 0);
    }

    /**
     * 8字节数据根据格式重新排序
     *
     * @param data  字节数组数据
     * @param index 索引
     * @return 8字节数组重排结果
     */
    protected byte[] reorderByFormatIn8Bytes(byte[] data, int index) {
        byte[] res = new byte[8];
        switch (this.byteBuffFormat) {
            case AB_CD:
                res[0] = data[index + 7];
                res[1] = data[index + 6];
                res[2] = data[index + 5];
                res[3] = data[index + 4];
                res[4] = data[index + 3];
                res[5] = data[index + 2];
                res[6] = data[index + 1];
                res[7] = data[index + 0];
                break;
            case BA_DC:
                res[0] = data[index + 6];
                res[1] = data[index + 7];
                res[2] = data[index + 4];
                res[3] = data[index + 5];
                res[4] = data[index + 2];
                res[5] = data[index + 3];
                res[6] = data[index + 0];
                res[7] = data[index + 1];
                break;
            case CD_AB:
                res[0] = data[index + 1];
                res[1] = data[index + 0];
                res[2] = data[index + 3];
                res[3] = data[index + 2];
                res[4] = data[index + 5];
                res[5] = data[index + 4];
                res[6] = data[index + 7];
                res[7] = data[index + 6];
                break;
            case DC_BA:
                res[0] = data[index + 0];
                res[1] = data[index + 1];
                res[2] = data[index + 2];
                res[3] = data[index + 3];
                res[4] = data[index + 4];
                res[5] = data[index + 5];
                res[6] = data[index + 6];
                res[7] = data[index + 7];
                break;
            default:
                break;
        }
        return res;
    }

    public enum EByteBuffFormat {
        AB_CD,
        BA_DC, //按照单字节反转
        CD_AB, //按照双字节反转
        DC_BA  //按照倒序排列
    }
}
