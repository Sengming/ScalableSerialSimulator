package serialports;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ThreadSafeListWrapper 
{
    // Constructor:
    public ThreadSafeListWrapper(LinkedList<Byte> listToWrap, Semaphore signalSem)
    {
        m_list = listToWrap;
        m_signalSem = signalSem;
        m_lockSem = new Semaphore(1);
    }
    
    // Members:
    protected LinkedList<Byte> m_list;
    protected Semaphore m_lockSem;
    protected Semaphore m_signalSem;
    
    
    // Public Interface Methods:
    
    public void enqueue(Byte item)
    {
        try 
        {
            m_lockSem.acquire();
            m_list.addLast(item);
//            System.out.println("Adding Permits: "+m_signalSem.availablePermits());
            m_lockSem.release();
            m_signalSem.release();
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
    
    public Byte dequeue()
    {
        Byte retVal = null;
        try
        {
            m_signalSem.acquire();
            m_lockSem.acquire();       
//            System.out.println("Taking Permits: "+m_signalSem.availablePermits());
            retVal = m_list.removeFirst();
            m_lockSem.release();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        return retVal;
    }
    
}
