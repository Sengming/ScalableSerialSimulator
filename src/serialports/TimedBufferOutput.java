package serialports;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.activity.InvalidActivityException;

import packets.IDataPacket;

public class TimedBufferOutput 
{
    public TimedBufferOutput(LinkedList<IDataPacket> packetList, ThreadSafeListWrapper outputBuffer,
            int timeout, boolean recurring)
    {
        m_timeout = timeout;
        m_recur = recurring;
        m_outputBuffer = outputBuffer;
        m_packetList = packetList;
        s_staticIterationIndex = 0;
        s_isTimerStarted = false;
    }
    
    
    public TimedBufferOutput(LinkedList<IDataPacket> packetList, ThreadSafeListWrapper outputBuffer)
    {
        m_outputBuffer = outputBuffer;
        m_packetList = packetList;        
        s_staticIterationIndex = 0;
        s_isTimerStarted = false;
    }
    
    // Warning: High cyclomatic complexity
    public void startTimer() throws InvalidActivityException
    {
        if (s_isTimerStarted == false)
        {
            s_isTimerStarted = true;
            Timer timer = new Timer();
            
            TimerTask taskToRun = new TimerTask() 
            {
                @Override
                public void run() 
                {
                    if (s_staticIterationIndex < m_packetList.size())
                    {
                        LinkedList<Byte> serializedData = m_packetList.get(s_staticIterationIndex).serialize();
                        for (Byte b : serializedData)
                        {
                            m_outputBuffer.enqueue(b);
                        }
                        ++s_staticIterationIndex;
                    }
                    else
                    {
                        // Either reset the timer and resend the list if we're recurring
                        if(m_recur)
                        {
                           s_staticIterationIndex = 0; 
                        }
                        // Or terminate timer
                        else
                        {
                            timer.cancel();
                        }
                    }
                }
            };
            
            timer.scheduleAtFixedRate(taskToRun, m_timeout, m_timeout);
        }
        else
        {
            throw new InvalidActivityException("Timer already created/started!");
        }
    }
    
    public void startTimer(int timeout, boolean recur)
    {
        
    }
    
    
    
    // Members:
    
    // Recur refers to if you want it to iterate through the list once or do it multiple times:
    protected boolean m_recur;
    // Timeout is in milliseconds.
    protected int m_timeout;
    protected ThreadSafeListWrapper m_outputBuffer;
    protected LinkedList<IDataPacket> m_packetList;
    protected static int s_staticIterationIndex = 0;
    protected static boolean s_isTimerStarted = false;
}
