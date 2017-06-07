package serialports;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class SerialReader implements Runnable
{
    public SerialReader(InputStream inStream, LinkedList<String> buffer, Semaphore bufferSemaphore)
    {
        m_buffer = buffer;
        m_inputSemaphore = bufferSemaphore;
        m_inputStream = inStream;
    }
    
    protected InputStream m_inputStream;
    protected LinkedList<String> m_buffer;
    protected Semaphore m_inputSemaphore;
    
    @Override
    public void run() 
    {
        byte[] localbuffer = new byte[50];
        try
        {
            while ((m_inputStream.read(localbuffer)) > -1)
            {
                String localString = new String(localbuffer, StandardCharsets.UTF_8);
                m_buffer.add(localString);
                m_inputSemaphore.release();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
