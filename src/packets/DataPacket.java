package packets;

import java.io.Serializable;


public class DataPacket implements Serializable
{
    // Constructor:
    public DataPacket(String data)
    {
        m_packetData = data;
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected String m_packetData;
    
    public byte[] serialize()
    {
        return m_packetData.getBytes();
    }
}
