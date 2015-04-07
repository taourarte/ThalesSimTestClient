package com.goyoung.crypto.util.hsm.host.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class run_Command {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Test_Host_CMD.SendCommand("pki.gordonjyoung.com", 9998,"GJY*CAU1750CDFB0757D3B3994430636DBB281BUA787EF2F6595A8158EECE42B1170228912BFB87728013AD7610101234567812345");
		//Test_Host_CMD.SendCommand("pki.gordonjyoung.com", 9998, "[AOECHO;AGVerify that comm lines are working;]");
		// [AOECHO;AGVerify that comm lines are working;]
	}

}
//