package com.goyoung.crypto.util.hsm.host.ThalesUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.jdom.Element;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOFilter;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.QBeanSupport;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.space.TSpace;
import org.jpos.util.NameRegistrar;

public class ThalesAdaptor extends QBeanSupport implements Runnable,
		ThalesAdaptorMBean {

	public class Sender implements Runnable {

		long lastKA = new Date().getTime();

		public Sender() {
			super();
		}

		@Override
		@SuppressWarnings("unchecked")
		public void run() {
			Thread.currentThread().setName("channel-sender-" + in);
			while (running()) {
				try {
					checkConnection();
					if (!running())
						break;

					Object o = sp.in(in, delay);

					boolean space = false;

					TSpace<String, ThalesMsg> ts = new TSpace<String, ThalesMsg>();

					if (o instanceof TSpace) {

						ts = (TSpace<String, ThalesMsg>) o;

						ThalesMsg m = ts.in("Request");
						o = m;

						space = true;

					}

					if (o instanceof ThalesMsg) {

						ThalesMsg resp = channel.command((ThalesMsg) o);

						tx++;
						sp.rd(ready);

						rx++;
						lastTxn = System.currentTimeMillis();

						if (!space)
							sp.out(out, resp);
						else {
							ts.out("Response", resp);
						}

					} else if (keepAlive
							&& channel.isConnected()
							&& ((new Date().getTime() - lastKA) > keepAliveInterval)) {
						channel.sendKeepAlive();
						lastKA = new Date().getTime();

					}
				} catch (ISOFilter.VetoException e) {
					getLog().warn("channel-sender-" + in, e.getMessage());
				} catch (ISOException e) {
					getLog().warn("channel-sender-" + in, e.getMessage());
					if (!ignoreISOExceptions) {
						disconnect();
					}
					ISOUtil.sleep(1000); // slow down on errors
				} catch (Exception e) {
					getLog().warn("channel-sender-" + in, e.getMessage());
					disconnect();
					ISOUtil.sleep(1000);
				}
			}
		}
	}
	Space<String, Object> sp;
	ThalesCore channel;
	String in, out, ready, reconnect;
	long delay;
	long keepAliveInterval = 30000;
	String basePath;
	String host;

	int port;
	boolean keepAlive = false;
	boolean ignoreISOExceptions = false;
	int rx, tx, connects;

	long lastTxn = 0l;

	public ThalesAdaptor() {
		super();
		resetCounters();
	}

	private void append(StringBuffer sb, String name, int value) {
		sb.append(name);
		sb.append(value);
	}

	protected void checkConnection() {
		while (running() && sp.rdp(reconnect) != null) {

			getLog().warn("checkConnection HThalesAdaptor");

			ISOUtil.sleep(1000);
		}
		while (running() && !channel.isConnected()) {
			while (sp.inp(ready) != null)
				;
			try {
				getLog().warn("checkConnection HThalesAdaptor 2");

				channel.connect();
			} catch (IOException e) {
				getLog().warn("check-connection", e.getMessage());
			}
			if (!channel.isConnected())
				ISOUtil.sleep(delay);
			else
				connects++;
		}
		if (running() && (sp.rdp(ready) == null))
			sp.out(ready, new Date());
	}

	protected synchronized void disconnect() {
		try {
			while (sp.inp(ready) != null)
				;
			channel.disconnect();
		} catch (IOException e) {
			getLog().warn("disconnect", e);
		}
	}

	public void dump(PrintStream p, String indent) {
		p.println(indent + getCountersAsString());
	}

	@Override
	public int getConnectsCounter() {
		return connects;
	}

	@Override
	public String getCountersAsString() {
		StringBuffer sb = new StringBuffer();
		append(sb, "tx=", tx);
		append(sb, ", rx=", rx);
		append(sb, ", connects=", connects);
		sb.append(", last=");
		sb.append(lastTxn);
		if (lastTxn > 0) {
			sb.append(", idle=");
			sb.append(System.currentTimeMillis() - lastTxn);
			sb.append("ms");
		}
		return sb.toString();
	}

	/**
	 * @jmx:managed-attribute description="remote host address"
	 */
	@Override
	public String getHost() {
		return getProperty(getProperties("channel"), "host");
	}

	@Override
	public long getIdleTimeInMillis() {
		return lastTxn > 0L ? System.currentTimeMillis() - lastTxn : -1L;
	}

	/**
	 * @jmx:managed-attribute description="input queue"
	 */
	@Override
	public String getInQueue() {
		return in;
	}

	@Override
	public long getLastTxnTimestampInMillis() {
		return lastTxn;
	}

	@Override
	public String getOutQueue() {
		return out; 
	}

	/**
	 * @jmx:managed-attribute description="remote port"
	 */
	@Override
	public int getPort() {
		int port = 0;
		try {
			port = Integer.parseInt(getProperty(getProperties("channel"),
					"port"));
		} catch (NumberFormatException e) {
		}
		return port;
	}

	/**
	 * @jmx:managed-attribute description="get reconnect delay"
	 */
	@Override
	public long getReconnectDelay() {
		return delay;
	}

	@Override
	public int getRXCounter() {
		return rx;
	}

	/**
	 * @jmx:managed-attribute description="socket factory"
	 */
	@Override
	public String getSocketFactory() {
		return getProperty(getProperties("channel"), "socketFactory");
	}

	@Override
	public long getTickInterval() {
		return 0; 
	}

	@Override
	public int getTXCounter() {
		return tx;
	}

	@SuppressWarnings("unchecked")
	private Space<String, Object> grabSpace(Element e) {
		return SpaceFactory.getSpace(e != null ? e.getText() : "");
	}

	public void initChannel() throws ConfigurationException {
		Element persist = getPersist();
		sp = grabSpace(persist.getChild("space"));
		in = persist.getChildTextTrim("in");
		out = persist.getChildTextTrim("out");

		String s = persist.getChildTextTrim("reconnect-delay");
		delay = s != null ? Long.parseLong(s) : 10000; // reasonable default
		// channel = newChannel (e, getFactory());

		keepAlive = "yes".equalsIgnoreCase(persist
				.getChildTextTrim("keep-alive"));

		basePath = persist.getChildTextTrim("basepath");

		if (keepAlive) {
			try {
				keepAliveInterval = Integer.parseInt(persist
						.getChildTextTrim("keep-alive-interval"));

			} catch (Exception e) {
				keepAliveInterval = 30000;
			}

		}

		ignoreISOExceptions = "yes".equalsIgnoreCase(persist
				.getChildTextTrim("ignore-iso-exceptions"));

		ready = getName() + ".ready";
		reconnect = getName() + ".reconnect";
		NameRegistrar.register(getName(), this);

		host = persist.getChildTextTrim("host");
		port = Integer.parseInt(persist.getChildTextTrim("port"));
		basePath = persist.getChildTextTrim("basepath");

		channel = new ThalesCore(host, port, basePath, "base");

	}

	@Override
	public boolean isConnected() {
		return false; 
	}

	@Override
	public void resetCounters() {
		rx = tx = connects = 0;
		lastTxn = 0l;
	}

	@Override
	public void run() {

	}

	/**
	 * @jmx:managed-attribute description="remote host address"
	 */
	@Override
	public synchronized void setHost(String host) {
		setProperty(getProperties("channel"), "host", host);
		setModified(true);
	}

	/**
	 * @jmx:managed-attribute description="input queue"
	 */
	@Override
	public synchronized void setInQueue(String in) {
		String old = this.in;
		this.in = in;
		if (old != null)
			sp.out(old, new Object());

		getPersist().getChild("in").setText(in);
		setModified(true);
	}

	/**
	 * @jmx:managed-attribute description="output queue"
	 */
	@Override
	public synchronized void setOutQueue(String out) {
		this.out = out;
		getPersist().getChild("out").setText(out);
		setModified(true);
	}

	/**
	 * @jmx:managed-attribute description="remote port"
	 */
	@Override
	public synchronized void setPort(int port) {
		setProperty(getProperties("channel"), "port", Integer.toString(port));
		setModified(true);
	}

	/**
	 * @jmx:managed-attribute description="set reconnect delay"
	 */
	@Override
	public synchronized void setReconnectDelay(long delay) {
		getPersist().getChild("reconnect-delay").setText(Long.toString(delay));
		this.delay = delay;
		setModified(true);
	}

	/**
	 * @jmx:managed-attribute description="socket factory"
	 */
	@Override
	public synchronized void setSocketFactory(String sFac) {
		setProperty(getProperties("channel"), "socketFactory", sFac);
		setModified(true);
	}

	@Override
	public void setTickInterval(long tickInterval) {

	}

	@Override
	public void startService() {
		try {
			initChannel();
			new Thread(new Sender()).start();
		} catch (Exception e) {
			getLog().warn("error starting service", e);
		}
	}

}
