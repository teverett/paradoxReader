package test.com.khubla.pdxreader.util;

import org.junit.*;

import com.khubla.pdxreader.util.*;

/**
 * Byte patterns are as Paradox writes them: big-endian, sign bit inverted, all zeros for null.
 */
public class TestParadoxNumber {
	private static byte[] bytes(int... values) {
		final byte[] b = new byte[values.length];
		for (int i = 0; i < values.length; i++) {
			b[i] = (byte) values[i];
		}
		return b;
	}

	@Test
	public void testShort() {
		Assert.assertEquals(Long.valueOf(1), ParadoxNumber.decodeInteger(bytes(0x80, 0x01)));
		Assert.assertEquals(Long.valueOf(256), ParadoxNumber.decodeInteger(bytes(0x81, 0x00)));
		Assert.assertEquals(Long.valueOf(0), ParadoxNumber.decodeInteger(bytes(0x80, 0x00)));
		Assert.assertEquals(Long.valueOf(-1), ParadoxNumber.decodeInteger(bytes(0x7f, 0xff)));
		Assert.assertEquals(Long.valueOf(-256), ParadoxNumber.decodeInteger(bytes(0x7f, 0x00)));
		/*
		 * the encoding of Short.MIN_VALUE would be all zeros, which Paradox reserves for null
		 */
		Assert.assertNull(ParadoxNumber.decodeInteger(bytes(0x00, 0x00)));
	}

	@Test
	public void testLong() {
		Assert.assertEquals(Long.valueOf(1), ParadoxNumber.decodeInteger(bytes(0x80, 0x00, 0x00, 0x01)));
		Assert.assertEquals(Long.valueOf(725830), ParadoxNumber.decodeInteger(bytes(0x80, 0x0b, 0x13, 0x46)));
		Assert.assertEquals(Long.valueOf(-2), ParadoxNumber.decodeInteger(bytes(0x7f, 0xff, 0xff, 0xfe)));
		Assert.assertNull(ParadoxNumber.decodeInteger(bytes(0x00, 0x00, 0x00, 0x00)));
	}

	@Test
	public void testDouble() {
		Assert.assertEquals(Double.valueOf(1.0), ParadoxNumber.decodeDouble(bytes(0xbf, 0xf0, 0, 0, 0, 0, 0, 0)));
		Assert.assertEquals(Double.valueOf(1001.0), ParadoxNumber.decodeDouble(bytes(0xc0, 0x8f, 0x48, 0, 0, 0, 0, 0)));
		Assert.assertEquals(Double.valueOf(0.0), ParadoxNumber.decodeDouble(bytes(0x80, 0, 0, 0, 0, 0, 0, 0)));
		/*
		 * -1.0 is 0xbff0000000000000 in IEEE 754; Paradox stores the bitwise inverse
		 */
		Assert.assertEquals(Double.valueOf(-1.0), ParadoxNumber.decodeDouble(bytes(0x40, 0x0f, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff)));
		Assert.assertNull(ParadoxNumber.decodeDouble(bytes(0, 0, 0, 0, 0, 0, 0, 0)));
	}
}
