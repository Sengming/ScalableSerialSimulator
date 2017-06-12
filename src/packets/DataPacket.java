package packets;

import java.io.Serializable;
import java.util.LinkedList;


public interface DataPacket extends Serializable
{
    /**
     * 
     */
    static final long serialVersionUID = 1L; 
    public LinkedList<Byte> serialize();
    public void deserialize(LinkedList<Byte> listIn);
    public byte calculateCRC8Field();
    public short calculateCRC16Field();
    public int getLength();
}
