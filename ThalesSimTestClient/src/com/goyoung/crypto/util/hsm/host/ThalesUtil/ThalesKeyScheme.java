package com.goyoung.crypto.util.hsm.host.ThalesUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class ThalesKeyScheme {

	public static int getKeyLength(char b) throws Exception {
		int ret = ((Integer) keySchemes.get(b)).intValue();
		return ret;
	}
	
	public static char U = 'U';
	public static char Z = 'Z';
	public static char X = 'X';
	public static char Y = 'Y';

	public static char T = 'T';

	static Map<Character, Integer> keySchemes = new LinkedHashMap<Character, Integer>();

	static {
		keySchemes.put(Z, 16);
		keySchemes.put(U, 32);
		keySchemes.put(X, 32);
		keySchemes.put(Y, 48);
		keySchemes.put(T, 48);

	}

}
