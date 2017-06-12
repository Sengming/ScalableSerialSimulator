package sessionPresentationLayer;

import java.util.LinkedList;
import packets.CRCCalculations;
import packets.DataPacket;

public class PumpToLinkSession implements DataPacket
{
    // Using hypothetical values. Constructor:
    public PumpToLinkSession(byte [] data)
    {
        m_data = data;
        m_frameId = 0x70;
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 3L;

    @Override
    public LinkedList<Byte> serialize() 
    {
        
        LinkedList<Byte> retVal = new LinkedList<Byte>();
        retVal.add(m_frameId);
        
        for (byte b : m_data)
        {
            retVal.add(b);
        }
        return retVal;
    }

    @Override
    public void deserialize(LinkedList<Byte> listIn) 
    {
        m_frameId = listIn.getFirst();
        
        // Take the rest of the bytes and stuff it into the byte array
        for (int i = 0; i < (listIn.size()-1); ++i)
        {
            m_data[i] = listIn.get(i+1);
        }
    }

    @Override
    public byte calculateCRC8Field() 
    {
        return (byte)0xff;
    }

    @Override
    public short calculateCRC16Field() 
    {
        short retVal;
        byte[] buffer = new byte[getLength()];
        buffer[0] = m_frameId;
        for (int i = 1; i < getLength(); ++i)
        {
            buffer[i] = m_data[i-1];
        }
        
        retVal = (short)CRCCalculations.calculateCrc16(buffer);
        return retVal;
    }

    @Override
    public int getLength() 
    {
        return (m_data.length + 1);
    }
    
    private byte [] m_data;
    private byte m_frameId;

}
