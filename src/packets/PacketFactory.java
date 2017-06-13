package packets;

import sessionPresentationLayer.PumpToLinkSession;

public class PacketFactory 
{
    public enum PacketTypes_e
    {
        EMPTY_COMM_2004,
        SESSION_PACKET,
        STATUS_PACKET,
        EVENT_PACKET
    }
    
    public IDataPacket createPacket(PacketTypes_e type, byte[] data)
    {
        IDataPacket retVal = null;
        switch (type)
        {
            case SESSION_PACKET:
            {
                // Create session packet
                PumpToLinkSession sessionPacket = new PumpToLinkSession(data);
                
                // Create comm Packet and inject session packet
                byte startByte = (byte)0x1B;
                byte frameType = (byte)0xFF;
                byte frameNumber = (byte)0xFF;
                byte frameAck = (byte)0xFF;            
                Comm2004 commPacket = new Comm2004(sessionPacket, startByte, frameType, frameNumber, frameAck);
                retVal = commPacket;
            }
            break;
            case STATUS_PACKET:
            {
                // TODO: Populate after creating packet class.
            }
            break;
            case EVENT_PACKET:
            {
                // TODO: Populate after creating packet class.
            }
            break; 
            case EMPTY_COMM_2004:
            default:
            {
                retVal = new Comm2004();
            }
            break;
        }
               
        return retVal;
    }
}
