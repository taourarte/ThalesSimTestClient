package com.goyoung.crypto.util.hsm.host.ThalesUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import org.jpos.iso.ISOException;
import org.jpos.iso.packager.DummyPackager;

public class ThalesCore {

	private ThalesChannel channel;
	String basePath;

	public ThalesCore(String host, int port, String basePath, String schema) {
		channel = new ThalesChannel();
		channel.setHost(host, port);
		channel.setPackager(new DummyPackager());
		this.basePath = basePath;

		channel.setBasePath(basePath);

		channel.setSchema(schema);

	}

	public synchronized ThalesMsg command(ThalesMsg request)
			throws ISOException, IOException, InterruptedException {

		StringBuffer sbuffer = new StringBuffer(request.get("command"));
		sbuffer.setCharAt(1, (char) (sbuffer.charAt(1) + 1));

		try {

			request.pack();

		} catch (Exception e) {
			System.out.println("Exception 1e" + e);
		}

		ThalesMsg resp;

		resp = createResponse(sbuffer.toString());

		ThalesISOMsg msg = new ThalesISOMsg(request);
		msg.setHeader(new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4 });

		channel.setBasePath(resp.getBasePath());

		channel.setSchema(resp.getBaseSchema());

		try {
			msg.pack();

		} catch (Exception e) {
			System.out.println("Exception 2e " + e);
		}

		channel.send(msg);

		ThalesISOMsg response = (ThalesISOMsg) channel.receive();

		resp.merge(response.getFSDMsg());

		return resp;
	}

	public void connect() throws IOException {
		channel.connect();
	}

	public ThalesMsg createRequest(String command) throws IOException {

		ThalesMsg req = new ThalesMsg("file:" + basePath);

		if (command != null) {
			req.set("command", command);
			File f = null;
			f = new File(
					new URL(req.getBasePath() + command + ".xml").getFile());

			if (!f.exists())
				throw new IOException("Schema File not defined");

		}

		return req;
	}

	public ThalesMsg createResponse(String response) {
		ThalesMsg resp = new ThalesMsg("file:" + basePath + "resp-");

		if (response != null)
			resp.set("response", response);

		return resp;
	}

	public ThalesMsg diagnostics() throws IOException {
		return createRequest("NC");
	}

	public void disconnect() throws IOException {

		channel.disconnect();

	}

	public ThalesMsg echoTest() throws IOException {

		ThalesMsg req = createRequest("B2");

		req.set("length", "8");

		Random rnd = new Random();
		int i = rnd.nextInt(99999999);

		req.set("data", Integer.toString(i));

		return req;

	}

	public ThalesMsg generateDoubleLengthKey() throws IOException {
		ThalesMsg req = createRequest("A0");
		req.set("mode", "0");
		req.set("key-type", "001");
		req.set("key-scheme-lmk", "X");

		return req;
	}

	public ThalesMsg importDoubleLengthKey(String zmk, String key)
			throws IOException {
		ThalesMsg req = createRequest("A6");
		req.set("key-type", "001");
		req.set("zmk", "X" + zmk);
		req.set("key-under-zmk", "X" + key);
		req.set("key-scheme", "X");

		return req;
	}

	public boolean isConnected() {

		return channel.isConnected();

	}

	public void sendKeepAlive() throws Exception {

		command(echoTest());

	}

}