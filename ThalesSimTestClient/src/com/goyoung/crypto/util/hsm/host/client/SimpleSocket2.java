package com.goyoung.crypto.util.hsm.host.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SimpleSocket2 {

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
		
		  outToServer.writeBytes("GET /" + '\n' 
		  		+ "Host: www.google.com" + '\n'
		  		+ "Connection: keep-alive" + '\n'
		  		+ "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" + '\n'
		  	    + "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36" + '\n'
		  		+ "X-Client-Data: CJS2yQEIpbbJAQiptskBCMS2yQEI74jKAQiKksoB" + '\n'
		  		+ "Accept-Encoding: gzip, deflate, sdch" + '\n'
		  		+ "Accept-Language: en-US,en;q=0.8" + '\n'
		  		+ "Cookie: HSID=AFp-IzjhrbnQaCLml; APISID=Ea83k0-MFUaTf1CZ/AwHIUOpBAnNGqaJpt; PREF=ID=5999ae65b4b9217a:U=5c8bd7347e41deda:FF=0:LD=en:TM=1427206330:LM=1427375228:GM=1:S=9x6ovVqBMMQnxtRt; SID=DQAAAA8BAABQqtJcYiI3QK9txGbtqJ_U6Z0e726HPoPhCXi5gOi5W9I4uaLza_3SPCC8w3epHwqIF6k9Q2EGFJ-Vtz0_KzXjXNqIqjlCmG4yReo4pa5Q1K3EnRG7q6b9HBz-PFPqzB5L56U_MPMyDNI2jFzZZn5dcbAVcuIee6Hrzr7Im27n6KvCa59f4JL4Sa2Bp6zr_BkNepX4FB4zGye242j0MOybfGX1P_HsnSk0rTU8iC0ufFoRFj2JX8Kx_J6jFApr2u-xIBjRn7SLnQHj0kfPYPmVUao5iMYmatLZhSObrDFreY_-XsvoDyN_JRFFUFjVD5COtFQzs7WAtOSMByhBXnL6PRCFvXzjl2hf1Le03vgkuw; NID=67=PNLgq9ODYpdBpEYyMsp1yhgzq4H1FaeaHprDVcx8s4qdZTeAxJD52PNcsnnOVyu9CWrsKj1Q5N7P1uk9sjRz-S1abxnuq6aTNn72E7y2CWIK4hYMACW-RYFSqNLZWtgohNUtVXXBoTUJxKuosGPqCtbfFZwaMXNUwwvQYLKLF9mwhpTeBAj_VEVR4wpINbI5bHytRUDfCz831HJp43flzSHc8LhS");		
		  System.out.println("FROM SERVER: " + inFromServer.readLine());
		  String s_html;
		  
		  while((s_html=inFromServer.readLine())!= null){
	            System.out.println(s_html);
	            System.out.flush();
	        }
		  
		  clientSocket.close();
		
		
	}

}
