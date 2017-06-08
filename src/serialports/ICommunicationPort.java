package serialports;

public abstract interface ICommunicationPort 
{
    public abstract void connect () throws Exception;
    public abstract void disconnect() throws Exception;
}
