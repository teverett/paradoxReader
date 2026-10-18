package test.com.khubla.pdxreader;

import java.util.*;

import org.junit.*;

import com.khubla.pdxreader.db.*;

/**
 * A block need not be full. MTDEMO/FILMS.DB holds 36 records in blocks of 11, 11, 6, 8 and 0, and its free space still holds the bytes of records that were deleted or moved.
 */
public class TestPartialBlocks {
	@Test
	public void testEveryRecordReadOnce() throws Exception {
		final List<String> films = new ArrayList<String>();
		for (final Map<String, String> row : TestNumericFields.read("src/test/resources/examples/MTDEMO/FILMS.DB")) {
			films.add(row.get("Film #"));
		}
		Assert.assertEquals(36, films.size());
		Assert.assertEquals(36, new HashSet<String>(films).size());
		Assert.assertEquals(Arrays.asList("A1001", "A1002", "A1003"), films.subList(0, 3));
		Assert.assertEquals("C1211", films.get(35));
	}

	/**
	 * FREEBLOCK/FILMS.DB is MTDEMO/FILMS.DB with its last data block and the empty block after it swapped, and the block chain relinked, so a free block sits between used ones. Four blocks are in
	 * use, but the fourth used block is the fifth in the file.
	 */
	@Test
	public void testUsedBlockAfterFreeBlock() throws Exception {
		final Set<String> films = new HashSet<String>();
		for (final Map<String, String> row : TestNumericFields.read("src/test/resources/examples/FREEBLOCK/FILMS.DB")) {
			films.add(row.get("Film #"));
		}
		Assert.assertEquals(36, films.size());
		Assert.assertTrue(films.contains("C1211"));
	}

	@Test
	public void testRecordsInBlock() {
		Assert.assertEquals(1, DBTableBlock.recordsInBlock(0, 100));
		Assert.assertEquals(11, DBTableBlock.recordsInBlock(1000, 100));
		/*
		 * an empty block stores minus the record size, as an unsigned short once read
		 */
		Assert.assertEquals(0, DBTableBlock.recordsInBlock(0x10000 - 100, 100));
	}
}
