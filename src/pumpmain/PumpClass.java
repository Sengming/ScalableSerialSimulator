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

		
		// Instantiate Writer:
		LinkedList<Byte> outputList= new LinkedList<Byte>();
        Semaphore outputSem = new Semaphore(0);
		ThreadSafeListWrapper threadSafeOutputList = new ThreadSafeListWrapper(outputList, outputSem);
		
	    // Instantiate Reader:
		LinkedList<Byte> inputList = new LinkedList<Byte>();
		Semaphore inputSem = new Semaphore(0);
		ThreadSafeListWrapper threadSafeInputList = new ThreadSafeListWrapper(inputList, inputSem);		
		
		// Create Input Buffer reader/printer
		InputBufferReader bufferReader = new InputBufferReader(inputSem, threadSafeInputList);
		
		// Instantiate SerialPort:
        int baudRate = 115200;
        int dataBits = SerialPort.DATABITS_8;
        int stopBits = SerialPort.STOPBITS_1;
        int parityBits = SerialPort.PARITY_NONE;
        SerialPortMain mainSerialPort = new SerialPortMain((CommPortIdentifier)(commPortArray[selectedPortNumber]), "Pump", baudRate, dataBits, stopBits, parityBits);
		
        // Attempt to establish connection and 
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
		
		// Start both input/output threads:
		inputThread.start();
        outputThread.start();
	
        // Start printer thread:
        Thread printThread = new Thread(bufferReader, "Printer");
        printThread.start();
        
        LinkedList<IDataPacket> packetList = createPacketList();

        TimedBufferOutput timedOutput = new TimedBufferOutput(packetList, threadSafeOutputList, 1000, false);
        timedOutput.startTimer();
        
        // Close scanner:
        scan.close();
	}
	
	// Helper function to create packet List to send:
	public static LinkedList<IDataPacket> createPacketList()
	{
	    LinkedList <IDataPacket> retVal = new LinkedList<IDataPacket>();
        // Create the packets and append to list:
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
