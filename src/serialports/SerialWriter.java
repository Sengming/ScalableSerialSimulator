package serialports;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// Functions to write anything placed into the output buffer out to the output stream
public class SerialWriter implements Runnable
{
    // Constructor:
    public SerialWriter(OutputStream outputStream, ThreadSafeListWrapper outputBuffer)
    {
        m_outputStream = outputStream;
        m_outputBuffer = outputBuffer;
    }
    
    // Members:
    protected OutputStream m_outputStream;
    protected ThreadSafeListWrapper m_outputBuffer;
    
    @Override
    public void run() 
    {
        DataOutputStream dataout = new DataOutputStream(m_outputStream);
        try
        {
            while(true)
            {
                dataout.writeByte(m_outputBuffer.dequeue());
//                System.out.println("Trying to remove and write one");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } 
        
    }

}

