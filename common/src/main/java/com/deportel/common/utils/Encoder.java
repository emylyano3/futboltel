package com.deportel.common.utils;

import org.apache.commons.codec.binary.*;

public class Encoder
{
	public static String encode (String str)
	{
		Base64 encoder = new Base64();
		return encoder.encodeToString(str.getBytes());
	}

	public static String decode (String str)
	{
		Base64 decoder = new Base64();
		return new String(decoder.decode(str));
	}

	public static byte[] encode (byte[] data)
	{
		Base64 encoder = new Base64();
		return encoder.encode(data);
	}
}
