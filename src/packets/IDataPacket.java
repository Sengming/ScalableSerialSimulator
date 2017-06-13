package packets;

import java.util.LinkedList;

public interface IDataPacket
{
    public LinkedList<Byte> serialize();
    public void deserialize(LinkedList<Byte> listIn);
    public int getLength();
}
