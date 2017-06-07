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
import serialports.SerialPortMain;
import serialports.SerialReader;
import serialports.SerialWriter;
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
		
		// Instantiate:
		LinkedList<String> testList= new LinkedList<String>();
		testList.add("hello\n");
		testList.add("This is a test\n");
		int baudRate = 115200;
		int dataBits = SerialPort.DATABITS_8;
		int stopBits = SerialPort.STOPBITS_1;
		int parityBits = SerialPort.PARITY_NONE;
		
		SerialPortMain mainSerialPort = new SerialPortMain((CommPortIdentifier)(commPortArray[selectedPortNumber]), "Pump", baudRate, dataBits, stopBits, parityBits);
		try {
            mainSerialPort.connect();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		
		Semaphore outputSem = new Semaphore(2);
		SerialWriter serialWriter = new SerialWriter(mainSerialPort.m_outputStream, testList, outputSem);
        Thread thread = new Thread(serialWriter, "Writer");
        thread.start();
//		mainSerialPort.m_outputStream.write(testList.getFirst().getBytes());
		
	
	}
	

}
