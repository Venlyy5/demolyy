

@Data
public class ModBusEntity {

    String transactionId = "";
    String protocolMark = "";
    String length = "";

    String unitId = "";
    String functionCode = "";
    String data = "";

    String crc = "";


    public String toHexString() {
        return transactionId + protocolMark + length + unitId + functionCode + data + crc;
    }
}