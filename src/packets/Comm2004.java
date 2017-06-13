package packets;

import java.util.LinkedList;


public class Comm2004 implements packets.IApplicationLayerDataPacket 
{
    public Comm2004()
    {
        // Empty for now, if we're to use injection and a factory use overload ctor
    }
    
    public Comm2004(IDataPacket data, byte frameStart, byte frameType, byte frameNumber, 
            byte frameAck)
    {
        m_data = data;
        m_frameSize = 0;
        m_frameStart = frameStart;
        m_frameNumber = frameNumber;
        m_frameType = frameType;
        m_frameAck = frameAck;
    }

    // This method converts this object into a linked list of bytes
    @Override
    public LinkedList<Byte> serialize() 
    {
        LinkedList<Byte> retVal = new LinkedList<Byte>();
        retVal.add(m_frameStart);
        retVal.add(m_frameType);
        retVal.add(m_frameNumber);
        retVal.add(m_frameAck);
        m_frameSize = (byte)m_data.getLength();
        retVal.add(m_frameSize);
        
        retVal.add(calculateCRC8Field());
        
        for (byte b : m_data.serialize())
        {
            retVal.add(b);
        }
        
        return retVal;
    }

    @Override
    public void deserialize(LinkedList<Byte> listIn) 
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public byte calculateCRC8Field() 
    {
        byte retVal;
        // 4 chosen based on number of bytes to calculate for:
        byte[] bytesToGenerateCrcFor = new byte[4];
        bytesToGenerateCrcFor[0] = m_frameType;
        bytesToGenerateCrcFor[1] = m_frameNumber;
        bytesToGenerateCrcFor[2] = m_frameAck;
        bytesToGenerateCrcFor[3] = m_frameSize;
        
        retVal = CRCCalculations.calculateCrc8(bytesToGenerateCrcFor);
        
        return retVal;
    }
    
    // Returns size in bytes
    @Override
    public int getLength() 
    {
        // 7 for number of bytes in packet composition, plus size of ID, data and Crc16
        return (7 + m_data.getLength());
    }

    public void setData(IDataPacket data)
    {
        m_data = data;
    }
    
    
    // Comm2004 packet composition:
    private byte m_frameStart;
    private byte m_frameType;
    private byte m_frameNumber;
    private byte m_frameAck;
    private byte m_frameSize;
    private IDataPacket m_data;
    
}
