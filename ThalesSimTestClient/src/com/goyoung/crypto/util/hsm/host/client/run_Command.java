package com.goyoung.crypto.util.hsm.host.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class run_Command {

	public static void main(String[] args) throws UnknownHostException, IOException {
	
	//Generate BDK	
	String result = Test_Host_CMD.SendCommand("pki.gordonjyoung.com", 9998,"----BI;;X");
	//System.out.println("generate and wrap a key");
	//String result = Test_Host_CMD.SendCommand("192.168.5.50", 1501,"----A00002U"); //generate a TPK/TMK/PVK
	System.out.println(result);
	
	//Test_Host_CMD.SendCommand("pki.gordonjyoung.com", 7000, "<00#020035#0101##>");//send Atalla a command
	//Generate TPK Component Test_Host_CMD.SendCommand("pki.gordonjyoung.com", 9998,"----A270DU");
	//FutureX Exscrypt API send an Echo command
	//Test_Host_CMD.SendCommand("pki.gordonjyoung.com", 9998, "[AOECHO;AGVerify that comm lines are working;]");
		
	}
}


