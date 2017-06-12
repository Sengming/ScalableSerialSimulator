package packets;

public abstract class CRCCalculations 
{
    public static int calculateCrc16(final byte[] buffer) 
    {
        int crc = 0xFFFF;

        for (int j = 0; j < buffer.length ; j++) {
            crc = ((crc  >>> 8) | (crc  << 8) )& 0xffff;
            crc ^= (buffer[j] & 0xff);//byte to int, trunc sign
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;

    }
    
    public static byte calculateCrc8(final byte[] data)   
    {
        short _register = 0;
        short bitMask = 0;
        short poly = 0;
        _register = data[0];
        
        for (int i=1; i<data.length; i++)  {
            _register = (short)((_register << 8) | (data[i] & 0x00ff));
            poly = (short)(0x0107 << 7);
            bitMask = (short)0x8000;

            while (bitMask != 0x0080)  {
                if ((_register & bitMask) != 0) {
                    _register ^= poly;
                }
                poly = (short) ((poly&0x0000ffff) >>> 1);
                bitMask = (short)((bitMask&0x0000ffff) >>> 1);
            }  //end while
        }  //end for
        return (byte)_register;
    }
}
