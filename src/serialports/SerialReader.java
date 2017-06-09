package serialports;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
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
//        try
//        {
//            BufferedReader br = null;
//            StringBuilder sb = new StringBuilder();
//            String line;
//        
//            while (true)
//            {
//                br = new BufferedReader(new InputStreamReader(m_inputStream));
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//                System.out.println("Adding "+line);
//                m_buffer.enqueue(line);
//
//            }
//        }
//        catch (IOException e)
//        {
//            System.out.println("Error with enqueueing local buffer.");
//            e.printStackTrace();
//        }
        
        byte[] buffer = new byte[1024];
        try
        {
//            BufferedReader br = new BufferedReader(new InputStreamReader(m_inputStream));
            while (true)
            {
                StringBuilder sb = new StringBuilder(1024);
//                String line;
                while ((m_inputStream.read(buffer)) > -1 )
                {
                  if (buffer[0] == 0x1B)
                  {
                      System.out.println("Found the ESC Character!");
                      break;
                  }
                  sb.append(buffer);
                  System.out.println("Adding "+sb.toString());
                }
                m_buffer.enqueue(sb.toString());
                System.out.println();
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        } 
    }

}
