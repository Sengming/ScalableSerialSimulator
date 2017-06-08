/**
 * 
 */
package pumpmain;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import gnu.io.*;
import serialports.InputBufferReader;
import serialports.SerialPortMain;
import serialports.SerialReader;
import serialports.SerialWriter;
import serialports.ThreadSafeListWrapper;
/**
 * @author root
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
	
		// Code to test (Remove later):
		
		// Instantiate Writer:
		LinkedList<String> testList= new LinkedList<String>();
        Semaphore outputSem = new Semaphore(0);
		ThreadSafeListWrapper threadSafeList = new ThreadSafeListWrapper(testList, outputSem);
		threadSafeList.enqueue("Hello");
        threadSafeList.enqueue("This");
        threadSafeList.enqueue("Is");
        threadSafeList.enqueue("A");
        threadSafeList.enqueue("Test");
		int baudRate = 115200;
		int dataBits = SerialPort.DATABITS_8;
		int stopBits = SerialPort.STOPBITS_1;
		int parityBits = SerialPort.PARITY_NONE;
		
	    // Instantiate Reader:
		LinkedList<String> inputList = new LinkedList<String>();
		Semaphore inputSem = new Semaphore(0);
		ThreadSafeListWrapper threadSafeInputList = new ThreadSafeListWrapper(inputList, inputSem);		
		
//		InputBufferReader bufferReader = new InputBufferReader(inputSem, threadSafeInputList);
		
		SerialPortMain mainSerialPort = new SerialPortMain((CommPortIdentifier)(commPortArray[selectedPortNumber]), "Pump", baudRate, dataBits, stopBits, parityBits);
		try
		{
    		try {
                mainSerialPort.connect();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
    		
    		SerialWriter serialWriter = new SerialWriter(mainSerialPort.m_outputStream, threadSafeList);
            SerialReader serialReader = new SerialReader(mainSerialPort.m_inputStream, threadSafeInputList);
    		Thread outputThread = new Thread(serialWriter, "Writer");
    		Thread inputThread = new Thread(serialReader, "Reader");
    		
    		// Start both threads:
    		inputThread.start();
            outputThread.start();
    //		mainSerialPort.m_outputStream.write(testList.getFirst().getBytes());
		
//            Thread printThread = new Thread(bufferReader, "Printer");
//            printThread.start();
		}
		finally
		{
		    try {
//                mainSerialPort.disconnect();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
		}
	
	}
	

}
