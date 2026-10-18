package com.khubla.pdxreader.util;

import java.nio.*;

/**
 * Decodes Paradox numeric fields.
 * <p>
 * Paradox stores numbers big-endian with the sign bit inverted, so that the raw bytes of a field sort in numeric order. A positive value has the top bit set; clearing it gives the two's complement (or IEEE
 * 754) value. A negative integer has the top bit clear; setting it gives the two's complement value. A negative double has every byte inverted. A field whose bytes are all zero is null, which is distinct
 * from zero: zero itself is stored with only the top bit set.
 */
public class ParadoxNumber {
	/**
	 * Decode a 2, 4 or 8 byte integer field (S, I, D, Auto). Returns null for a null field.
	 */
	public static Long decodeInteger(byte[] data) {
		if (isNull(data)) {
			return null;
		}
		final byte[] b = data.clone();
		b[0] = (byte) (b[0] ^ 0x80);
		final ByteBuffer buffer = ByteBuffer.wrap(b).order(ByteOrder.BIG_ENDIAN);
		switch (b.length) {
			case 2:
				return (long) buffer.getShort();
			case 4:
				return (long) buffer.getInt();
			case 8:
				return buffer.getLong();
			default:
				throw new IllegalArgumentException("Unsupported integer field length " + b.length);
		}
	}

	/**
	 * Decode an 8 byte floating point field (N, C, TS). Returns null for a null field.
	 */
	public static Double decodeDouble(byte[] data) {
		if (isNull(data)) {
			return null;
		}
		final byte[] b = data.clone();
		if ((b[0] & 0x80) != 0) {
			b[0] = (byte) (b[0] & 0x7f);
		} else {
			for (int i = 0; i < b.length; i++) {
				b[i] = (byte) ~b[i];
			}
		}
		return ByteBuffer.wrap(b).order(ByteOrder.BIG_ENDIAN).getDouble();
	}

	private static boolean isNull(byte[] data) {
		for (final byte d : data) {
			if (d != 0) {
				return false;
			}
		}
		return true;
	}
}
