package com.goyoung.crypto.util.hsm.host.ThalesUtil;

import org.jdom.Element;

public interface ThalesAdaptorMBean extends org.jpos.q2.QBeanSupportMBean {

	public int getConnectsCounter();

	public String getCountersAsString();

	java.lang.String getHost();

	public long getIdleTimeInMillis();

	java.lang.String getInQueue();

	public long getLastTxnTimestampInMillis();

	java.lang.String getOutQueue();

	@Override
	public Element getPersist();

	int getPort();

	long getReconnectDelay();

	public int getRXCounter();

	java.lang.String getSocketFactory();

	public long getTickInterval();

	public int getTXCounter();

	public boolean isConnected();

	public void resetCounters();

	void setHost(java.lang.String host);

	void setInQueue(java.lang.String in);

	void setOutQueue(java.lang.String out);

	void setPort(int port);

	void setReconnectDelay(long delay);

	void setSocketFactory(java.lang.String sFac);

	public void setTickInterval(long tickInterval);
}
