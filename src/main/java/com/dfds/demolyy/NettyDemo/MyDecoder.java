package com.dfds.demolyy.NettyDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * 功能描述: 自定义接收消息格式
 */
public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //创建字节数组,buffer.readableBytes可读字节长度
        byte[] b = new byte[byteBuf.readableBytes()];
        //复制内容到字节数组b
        byteBuf.readBytes(b);
        //字节数组转字符串
        String str = new String(b);

        list.add(bytesToHexString(b));
    }

    public String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
