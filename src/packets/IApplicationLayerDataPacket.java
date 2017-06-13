package packets;


public interface IApplicationLayerDataPacket extends IDataPacket
{
    public byte calculateCRC8Field();
}
