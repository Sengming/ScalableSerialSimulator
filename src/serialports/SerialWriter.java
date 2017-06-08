package serialports;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

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
        try
        {
            while(true)
            {
                m_outputStream.write(m_outputBuffer.dequeue().getBytes());
                System.out.println("Trying to remove and write one");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } 
        
    }

}
