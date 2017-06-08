package serialports;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashSet;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

public class SerialPortMain implements ICommunicationPort
{
    // Constructor:
    public SerialPortMain(CommPortIdentifier identifier, String ownerName, int baudRate,
            int numDataBits, int numStopBits, int parityBits)
    {
        m_identifier = identifier;
        m_portOwner = ownerName;
        m_openConnectionTimeout = 2000;
        m_baudRate = baudRate;
        m_numDataBits = numDataBits;
        m_numStopBits = numStopBits;
        m_parityBits = parityBits;
        
        m_serialPort = null;
        
        m_inputStream = null;
        m_outputStream = null;
    }
    
    
    // Static methods
      /**
     * @return    A HashSet containing the CommPortIdentifier for all serial ports that are not currently being used.
     */
     public static HashSet<CommPortIdentifier> getAvailableSerialPorts() 
     {
         HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
         Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
         while (thePorts.hasMoreElements()) 
         {
             CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
             switch (com.getPortType()) 
             {
             case CommPortIdentifier.PORT_SERIAL:
                 try 
                 {
                     CommPort thePort = com.open("CommUtil", 50);
                     thePort.close();
                     h.add(com);
                 } 
                 catch (PortInUseException e) 
                 {
                     System.out.println("Port, "  + com.getName() + ", is in use.");
                 } 
                 catch (Exception e) 
                 {
                     System.err.println("Failed to open port " +  com.getName());
                     e.printStackTrace();
                 }
             }
         }
         return h;
     }

     // Members:
     protected final CommPortIdentifier m_identifier;
     protected final String m_portOwner;
     protected final int m_openConnectionTimeout;
     protected final int m_baudRate;
     protected final int m_numDataBits;
     protected final int m_numStopBits;
     protected final int m_parityBits;
     protected SerialPort m_serialPort;
     
     public InputStream m_inputStream;
     public OutputStream m_outputStream;
     
     
     // Override methods:
     
    @Override
    public void connect() throws Exception 
    {
        if (m_identifier.isCurrentlyOwned())
        {
            System.out.println("Port is currently in use!");
            throw new IOException("Unable to open comm port.");
        }
        else
        {
            m_serialPort = (SerialPort)m_identifier.open(m_portOwner, m_openConnectionTimeout);
            m_serialPort.setSerialPortParams(m_baudRate, m_numDataBits, m_numStopBits, m_parityBits);
            
            m_inputStream = m_serialPort.getInputStream();
            m_outputStream = m_serialPort.getOutputStream();
            
        }
    }

    @Override
    public void disconnect() throws Exception
    {
        m_serialPort.close();      
    }
    
}
