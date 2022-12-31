package com.dfds.demolyy.Modbus_ModBusMasterTCP;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import cn.hutool.core.util.HexUtil;
import com.digitalpetri.modbus.codec.Modbus;
import com.digitalpetri.modbus.master.ModbusTcpMaster;
import com.digitalpetri.modbus.master.ModbusTcpMasterConfig;
import com.digitalpetri.modbus.requests.ReadHoldingRegistersRequest;
import com.digitalpetri.modbus.responses.ReadHoldingRegistersResponse;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.dfds.demolyy.utils.ProtocolUtils.HexUtils.byte2HexString;

public class MBMaster {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final List<ModbusTcpMaster> masters = new CopyOnWriteArrayList<>();
    private volatile boolean started = false;
    private final int nMasters;
    private final int nRequests;

    public MBMaster(int nMasters, int nRequests) {
        if (nMasters < 1) {
            nMasters = 1;
        }
        if (nRequests < 1) {
            nMasters = 1;
        }
        this.nMasters = nMasters;
        this.nRequests = nRequests;
    }

    //启动
    public void start() {
        started = true;
        ModbusTcpMasterConfig config = new ModbusTcpMasterConfig.Builder("127.0.0.1")
                .setPort(502)
                .setInstanceId("S-001")
                .build();

        new Thread(() -> {
            while (started) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                double mean = 0.0;
                int mcounter = 0;

                for (ModbusTcpMaster master : masters) {
                    mean += master.getResponseTimer().getMeanRate();
                    mcounter += master.getResponseTimer().getCount();
                }

                logger.info("Mean Rate={}, counter={}", mean, mcounter);
            }
        }).start();

        for (int i = 0; i < nMasters; i++) {
            ModbusTcpMaster master = new ModbusTcpMaster(config);
            master.connect();
            masters.add(master);
            for (int j = 0; j < nRequests; j++) {
                sendAndReceive(master);
            }
        }
    }

    //发送请求
    private void sendAndReceive(ModbusTcpMaster master) {
        if (!started) return;

        CompletableFuture<ReadHoldingRegistersResponse> future =
                master.sendRequest(new ReadHoldingRegistersRequest(0, 10), 1);

        //响应处理
        future.whenCompleteAsync((response, ex) -> {
            if (response != null) {
                byte[] bytes = ByteBufUtil.getBytes(response.getRegisters());
                System.out.println("Response byte[]=" + Arrays.toString(bytes));

                System.out.println("Response HEX=" + ByteBufUtil.hexDump(response.getRegisters()));
                System.out.println(ByteBufUtil.prettyHexDump(response.getRegisters()));

                //返回的报文中在0到f这15个位置中，有需要的业务数据，具体获取哪个位置，取决于Slave设备的设置
                System.out.println("address=1寄存器地址(2个字节:byte[2],byte[3]) HEX:" + byte2HexString(bytes[2]) + " " + byte2HexString(bytes[3]) + " ---> DEC:" + HexUtil.hexToInt(byte2HexString(bytes[2]) + byte2HexString(bytes[3])));
                ReferenceCountUtil.release(response);
            } else {
                logger.error("Error Msg ={}", ex.getMessage(), ex);
            }
            scheduler.schedule(() -> sendAndReceive(master), 3, TimeUnit.SECONDS);
        }, Modbus.sharedExecutor());
    }

    public void stop() {
        started = false;
        masters.forEach(ModbusTcpMaster::disconnect);
        masters.clear();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //启动Client进行数据交互
        new MBMaster(1, 1).start();
    }
}