package com.khubla.pdxreader.util;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;

import com.google.common.io.*;

/**
 * @author tom
 */
public class StringUtil {
	/*
	 * String encoding
	 */
	/*
	 * https://en.wikipedia.org/wiki/Code_page_437
	 */
	private final static String DEFAULT_ENCODING = "IBM-437";
	/*
	 * max String length. Technically the max paradox string is 256
	 */
	private final static int MAX_STRING = 1024;

	public static String byteArrayToString(byte[] bytes) {
		final StringBuilder builder = new StringBuilder();
		for (final byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

	public static String readString(byte[] data) throws IOException {
		return readString(data, DEFAULT_ENCODING);
	}

	public static String readString(byte[] data, String encoding) throws IOException {
		final StringBuilder stringBuilder = new StringBuilder();
		final CharBuffer charBuffer = Charset.forName(encoding).decode(ByteBuffer.wrap(data));
		stringBuilder.append(charBuffer);
		return stringBuilder.toString();
	}

	/**
	 * read a fixed length string from a byte buffer
	 */
	public static String readString(LittleEndianDataInputStream littleEndianDataInputStream) throws IOException {
		return readString(littleEndianDataInputStream, DEFAULT_ENCODING);
	}

	/**
	 * read a null terminated string from a LittleEndianDataInputStream
	 */
	public static String readString(LittleEndianDataInputStream littleEndianDataInputStream, String encoding) throws IOException {
		final ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_STRING);
		Byte c = littleEndianDataInputStream.readByte();
		int len = 0;
		while (c != 0) {
			byteBuffer.put(c);
			c = littleEndianDataInputStream.readByte();
			len++;
		}
		byteBuffer.position(0);
		final byte[] data = new byte[len];
		byteBuffer.get(data, 0, len);
		return readString(data, encoding);
	}
}
