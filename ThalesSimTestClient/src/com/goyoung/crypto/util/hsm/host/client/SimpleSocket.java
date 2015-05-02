package com.goyoung.crypto.util.hsm.host.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SimpleSocket {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub

		//with SSL
		//SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
        //SSLSocket clientSocket = (SSLSocket) f.createSocket("10.236.82.166", 1500);
        //clientSocket.startHandshake();
       
        //With Plain Text
		Socket clientSocket = new Socket("192.168.5.50", 1501);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		
		//FutureX Excrypt API Commands:
		outToServer.writeBytes("A00002X");
			//	"//[AOECHO;AGVerify that comm lines are working;]\r\n");
		
		DataInputStream in = new DataInputStream(clientSocket.getInputStream());

		byte len1 = in.readByte();
		byte len2 = in.readByte();
		short responseLength = (short) (len1 << 8 | len2);

		byte[] responseBytes = new byte[responseLength];
		for (int i = 0; i < responseLength; i++) {
			responseBytes[i] = in.readByte();
		}

		String response = new String(responseBytes, "UTF-8");
		clientSocket.close();
		
		System.out.println(response);

	}
}

