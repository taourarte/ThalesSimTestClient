package com.goyoung.crypto.util.hsm.host.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;


public class HSM_Test_Client {

	public static void main(String[] args) throws UnknownHostException, IOException {

		//SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault(); //with SSL
        //SSLSocket clientSocket = (SSLSocket) f.createSocket("10.236.82.166", 1501);
        //clientSocket.startHandshake();
       
        //With Plain Text
		Socket clientSocket = new Socket("192.168.5.50", 1500); //standard (API, Atalla listens on 1500, Thales listens on 1501
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		String s_API_Command = "[AOECHO;AGVerify BR549-echo-foxtrot;]";
		//String s_Atalla_Command = "<00#020035#0101##>";
		//String s_Thales_Command = "BI;;X"; //generate BDK // or gen TMK X.9 export "A00002X";
		
		outToServer.write(s_API_Command.getBytes()); //DataOutputStream works better writing 
														//HEX Bytes than string for whatever reason: (Encoding issue?, whatever..)
		
		char cbuf[] = new char[clientSocket.getReceiveBufferSize()];
		reader.read(cbuf); reader.close();
		clientSocket.close();
		
		System.out.print(new String(cbuf).trim());
		
	}
}
