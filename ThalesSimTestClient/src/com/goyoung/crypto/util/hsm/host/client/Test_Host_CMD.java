package com.goyoung.crypto.util.hsm.host.client;

import org.apache.commons.codec.binary.Hex;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Test_Host_CMD {

	public static String SendCommand(String s_Host, int i_Port, String s_command)
			throws UnknownHostException, IOException {

		String s_Command = SwitchCase(s_command);
		String hexString = new String(Hex.encodeHex(s_Command.getBytes("UTF-8")));

		Socket socket = new Socket(s_Host, i_Port);

		byte[] ba_command = hexStringToByteArray(hexString);
		byte[] s_command_len = length2byte(ba_command.length);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(s_command_len);
		outputStream.write(ba_command);
		byte[] b_command = outputStream.toByteArray();

		OutputStream out = socket.getOutputStream();
		BufferedOutputStream bufferedOut = new BufferedOutputStream(out, b_command.length);
		bufferedOut.write(b_command);
		bufferedOut.flush();

		DataInputStream in = new DataInputStream(socket.getInputStream());

		byte len1 = in.readByte();
		byte len2 = in.readByte();
		short responseLength = (short) (len1 << 8 | len2);

		byte[] responseBytes = new byte[responseLength];
		for (int i = 0; i < responseLength; i++) {
			responseBytes[i] = in.readByte();
		}

		String response = new String(responseBytes, "UTF-8");
		socket.close();
		System.out.println(response);
		return response;
	}

	private static byte[] length2byte(int len) {
		byte[] b = new byte[2];
		b[0] = (byte) (len / 256);
		b[1] = (byte) (len % 256);
		return b;
	}

	static String SwitchCase(String inputVal) {
		// Switch to uppercase unless empty string
		if (inputVal.length() == 0)
			return "";
		return inputVal.toUpperCase();
	}

	public static byte[] hexStringToByteArray(String s)
			throws UnsupportedEncodingException {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
//
