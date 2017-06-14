/**
 * 
 */
package pumpmain;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import gnu.io.*;
import packets.IDataPacket;
import packets.PacketFactory;
import packets.PacketFactory.PacketTypes_e;
import serialports.InputBufferReader;
import serialports.SerialPortMain;
import serialports.SerialReader;
import serialports.SerialWriter;
import serialports.ThreadSafeListWrapper;
import serialports.TimedBufferOutput;
/**
 * @author SengMing
 *
 */
public class PumpClass {
    
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		// First, generate list of available serial ports:
		HashSet<CommPortIdentifier> comPortSet = SerialPortMain.getAvailableSerialPorts();
		Object[] commPortArray = new CommPortIdentifier[20];
		commPortArray = comPortSet.toArray();
		
		for (Object e: commPortArray)
		{
		    System.out.println("Available Comm Port Name: " + ((CommPortIdentifier) e).getName());		    
		}
		System.out.println("Number of Comm Ports: " + commPortArray.length);
		
		Scanner scan = new Scanner(System.in);
		int selectedPortNumber = scan.nextInt();
		
		System.out.println("Your selection is: " + ((CommPortIdentifier)(commPortArray[selectedPortNumber])).getName());

		
		// Create our thread-safe signalled output buffer. Add to this list after SerialWriter thread has been started to output.
		LinkedList<Byte> outputList= new LinkedList<Byte>();
        Semaphore outputSem = new Semaphore(0);
		ThreadSafeListWrapper threadSafeOutputList = new ThreadSafeListWrapper(outputList, outputSem);
		
        // Create our thread-safe signalled input buffer. Dequeue from this list after SerialReader thread has been started to get values.
		LinkedList<Byte> inputList = new LinkedList<Byte>();
		Semaphore inputSem = new Semaphore(0);
		ThreadSafeListWrapper threadSafeInputList = new ThreadSafeListWrapper(inputList, inputSem);		
		
		// Create Input Buffer reader/printer. 
		InputBufferReader bufferReader = new InputBufferReader(inputSem, threadSafeInputList);
		
		// Instantiate SerialPort with these attributes:
        int baudRate = 115200;
        int dataBits = SerialPort.DATABITS_8;
        int stopBits = SerialPort.STOPBITS_1;
        int parityBits = SerialPort.PARITY_NONE;
        SerialPortMain mainSerialPort = new SerialPortMain((CommPortIdentifier)(commPortArray[selectedPortNumber]), "Pump", baudRate, dataBits, stopBits, parityBits);
		
        // Attempt to establish connection and open output and input streams to Comm port
		try 
		{
            mainSerialPort.connect();
        } 
		catch (Exception e1) 
		{
            e1.printStackTrace();
        }
		
		SerialWriter serialWriter = new SerialWriter(mainSerialPort.m_outputStream, threadSafeOutputList);
        SerialReader serialReader = new SerialReader(mainSerialPort.m_inputStream, threadSafeInputList);
		Thread outputThread = new Thread(serialWriter, "Writer");
		Thread inputThread = new Thread(serialReader, "Reader");
		
		// Start both input/output threads that read in/out to the threadSafeInputList and threadSafeOutputList
		inputThread.start();
        outputThread.start();
	
        // Start printer thread. This just grabs from the input list and displays to debug terminal
        Thread printThread = new Thread(bufferReader, "Printer");
        printThread.start();
        
        // Create a packet list that the TimedBufferOutput will enqueue to our thread safe output buffer
        LinkedList<IDataPacket> packetList = createPacketList();

        TimedBufferOutput timedOutput = new TimedBufferOutput(packetList, threadSafeOutputList, 1000, false);
        timedOutput.startTimer();
        
        // Closes the debug terminal input reader. Not related to serial port.
        scan.close();
	}
	
	// Helper function to create packet List that we will be putting into the serial output buffer
	public static LinkedList<IDataPacket> createPacketList()
	{
	    LinkedList <IDataPacket> retVal = new LinkedList<IDataPacket>();
        // Create the dummy packets and append to list:
        PacketFactory packetFactory = new PacketFactory();
	    byte [] sessionData0 = new byte[8];
        IDataPacket packetToOutput0 = packetFactory.createPacket(PacketTypes_e.SESSION_PACKET, sessionData0);
        byte [] sessionData1 = new byte[8];
        IDataPacket packetToOutput1 = packetFactory.createPacket(PacketTypes_e.SESSION_PACKET, sessionData1);
        byte [] sessionData2 = new byte[8];
        IDataPacket packetToOutput2 = packetFactory.createPacket(PacketTypes_e.SESSION_PACKET, sessionData2);
        
        retVal.add(packetToOutput0);
        retVal.add(packetToOutput1);
        retVal.add(packetToOutput2);
        
        return retVal;
	}

}
