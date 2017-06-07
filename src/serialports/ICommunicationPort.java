package serialports;

public abstract interface ICommunicationPort 
{
    public abstract void connect () throws Exception;
    public abstract void sendData(byte[] data);
    public abstract byte[] receiveData();   
}
