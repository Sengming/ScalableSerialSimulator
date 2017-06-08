package serialports;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ThreadSafeListWrapper 
{
    // Constructor:
    public ThreadSafeListWrapper(LinkedList<String> listToWrap, Semaphore signalSem)
    {
        m_list = listToWrap;
        m_signalSem = signalSem;
        m_lockSem = new Semaphore(1);
    }
    
    // Members:
    protected LinkedList<String> m_list;
    protected Semaphore m_lockSem;
    protected Semaphore m_signalSem;
    
    
    // Public Interface Methods:
    
    public void enqueue(String item)
    {
        try 
        {
            m_lockSem.acquire();
            m_list.addLast(item);
            m_signalSem.release();
            m_lockSem.release();
        } 
        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }
    
    public String dequeue()
    {
        String retVal = null;
        try
        {
            m_lockSem.acquire();
            m_signalSem.acquire();
            retVal = m_list.removeFirst();
            m_lockSem.release();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            retVal = "An Error has Occurred!";
        }
        
        return retVal;
    }
    
}
