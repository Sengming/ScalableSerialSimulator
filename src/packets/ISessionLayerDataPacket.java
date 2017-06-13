package packets;

public interface ISessionLayerDataPacket extends IDataPacket
{
    public short calculateCRC16Field();
}
