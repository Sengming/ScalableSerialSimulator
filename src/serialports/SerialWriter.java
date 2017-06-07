package serialports;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class SerialWriter implements Runnable
{
    // Constructor:
    public SerialWriter(OutputStream outputStream, LinkedList<String> outputBuffer, Semaphore outputSemaphore)
    {
        m_outputStream = outputStream;
        m_outputBuffer = outputBuffer;
        m_outputSemaphore = outputSemaphore;
    }
    
    // Members:
    protected OutputStream m_outputStream;
    protected LinkedList<String> m_outputBuffer;
    protected Semaphore m_outputSemaphore;
    
    @Override
    public void run() 
    {
        try
        {
            while(true)
            {
                m_outputSemaphore.acquire();
                m_outputStream.write(m_outputBuffer.removeFirst().getBytes());
                System.out.println("Trying to remove and write one");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
        
    }

}
