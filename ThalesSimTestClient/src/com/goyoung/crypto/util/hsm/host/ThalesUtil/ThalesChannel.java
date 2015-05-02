package com.goyoung.crypto.util.hsm.host.ThalesUtil;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.FSDChannel;

public class ThalesChannel extends FSDChannel {
	String basePath;
	String schema;

	String baseResponsePath;

	@Override
	public ISOMsg createMsg() {
		if (basePath != null && schema != null)
			return new ThalesISOMsg(new ThalesMsg(basePath, schema));

		if (basePath != null)
			return new ThalesISOMsg(new ThalesMsg(basePath));

		return new ThalesISOMsg();
	}

	public String getBasePath() {
		return basePath;
	}

	public String getBaseResponsePath() {
		return baseResponsePath;
	}

	public String getSchema() {
		return schema;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public void setBaseResponsePath(String baseResponsePath) {
		this.baseResponsePath = baseResponsePath;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
}