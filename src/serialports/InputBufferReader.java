package serialports;

import java.util.concurrent.Semaphore;

public class InputBufferReader implements Runnable
{
    public InputBufferReader(Semaphore signalSemaphore, ThreadSafeListWrapper threadSafeList)
    {
        m_signalSemaphore = signalSemaphore;
        m_list = threadSafeList;
    }

    protected Semaphore m_signalSemaphore;
    protected ThreadSafeListWrapper m_list;
    
    @Override
    public void run() 
    {
        try 
        {
            while(true)
            {
                System.out.print(m_list.dequeue() + " ");
//                System.out.println("Signal Semaphore acquired!");
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
    }
}
