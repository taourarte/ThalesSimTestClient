package com.goyoung.crypto.util.hsm.host.client;

import java.io.BufferedReader;
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
		SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket clientSocket = (SSLSocket) f.createSocket("google.com", 443);
        clientSocket.startHandshake();
       
        //With Plain Text
		// Socket clientSocket = new Socket("google.com", 80);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		  outToServer.writeBytes("GET /" + '\n');
		  
				  System.out.println("FROM SERVER: " + inFromServer.readLine());
		  String s_html;
		  
		  while((s_html=inFromServer.readLine())!= null){
	            System.out.println(s_html);
	            System.out.flush();
	        }
		  
		  clientSocket.close();
		
		
	}

}
