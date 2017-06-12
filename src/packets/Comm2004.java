package packets;

import java.util.LinkedList;

import sessionPresentationLayer.PumpToLinkSession;

public class Comm2004 implements packets.DataPacket 
{
    public Comm2004()
    {
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 2L;

    // This method converts this object into a linked list of bytes
    @Override
    public LinkedList<Byte> serialize() 
    {
        LinkedList<Byte> retval = new LinkedList<Byte>();
        
        return null;
    }

    @Override
    public void deserialize(LinkedList<Byte> listIn) 
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void calculateCRC8Field() 
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void calculateCRC16Field() 
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getLength() 
    {
        // TODO Auto-generated method stub
        return 0;
    }

    // Comm2004 packet composition:
    private byte m_frameStart;
    private byte m_frameType;
    private byte m_frameNumber;
    private byte m_frameAck;
    private byte m_frameSize;
    private byte m_frameId;
    private byte m_crc8;
    private short m_crc16;
    private PumpToLinkSession m_pumpSession;
    
}
