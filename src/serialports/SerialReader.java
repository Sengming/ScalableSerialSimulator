package serialports;

import java.io.IOException;
import java.io.InputStream;
/** */

public class SerialReader implements Runnable
{
    public SerialReader(InputStream inStream, ThreadSafeListWrapper buffer)
    {
        m_buffer = buffer;
        m_inputStream = inStream;
    }
    
    protected InputStream m_inputStream;
    protected ThreadSafeListWrapper m_buffer;
    
    @Override
    public void run() 
    {
        try
        {
            byte[] buffer = new byte[1024];
            
            int length = -1;
            while ((length = m_inputStream.read(buffer)) > -1 )
            {
//              System.out.println("Buffer: " + buffer);
              for (int i = 0; i < length; ++i)
              {
//                  System.out.println("Enqueueing b:" + buffer[i]);
                  m_buffer.enqueue(buffer[i]);
              }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        } 

    }

}
