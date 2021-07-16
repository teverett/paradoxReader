package com.khubla.pdxreader;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.mb.*;

/**
 * @author tom
 */
public class Database {
	private class DatabaseIOFileFilterImpl implements IOFileFilter {
		private final String filter;

		public DatabaseIOFileFilterImpl(String filter) {
			this.filter = filter;
		}

		public boolean accept(File file) {
			if (FilenameUtils.getExtension(file.getName()).toLowerCase().compareTo(filter) == 0) {
				return true;
			}
			return false;
		}

		public boolean accept(File dir, String name) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	/**
	 * db files
	 */
	private final List<File> dbFiles = new ArrayList<File>();
	/**
	 * mb files
	 */
	private final List<File> mbFiles = new ArrayList<File>();
	/**
	 * px files
	 */
	private final List<File> pxFiles = new ArrayList<File>();
	/**
	 * val files
	 */
	private final List<File> valFiles = new ArrayList<File>();

	/**
	 * ctor
	 *
	 * @throws PDXReaderException
	 */
	public Database(String dbPath) throws PDXReaderException {
		load(dbPath);
	}

	public List<File> getDbfiles() {
		return dbFiles;
	}

	public List<File> getMbfiles() {
		return mbFiles;
	}

	public List<File> getPxfiles() {
		return pxFiles;
	}

	public List<File> getValfiles() {
		return valFiles;
	}

	private void load(String dbPath) throws PDXReaderException {
		try {
			final File dbDir = new File(dbPath);
			if (dbDir.exists()) {
				/**
				 * find db files
				 */
				final Iterator<File> dbIter = FileUtils.iterateFiles(dbDir, new DatabaseIOFileFilterImpl("db"), null);
				while (dbIter.hasNext()) {
					dbFiles.add(dbIter.next());
				}
				/**
				 * find mb files
				 */
				final Iterator<File> mbIter = FileUtils.iterateFiles(dbDir, new DatabaseIOFileFilterImpl("mb"), null);
				while (mbIter.hasNext()) {
					mbFiles.add(mbIter.next());
				}
				/**
				 * px files
				 */
				final Iterator<File> pxIter = FileUtils.iterateFiles(dbDir, new DatabaseIOFileFilterImpl("px"), null);
				while (pxIter.hasNext()) {
					pxFiles.add(pxIter.next());
				}
				/**
				 * val files
				 */
				final Iterator<File> valIter = FileUtils.iterateFiles(dbDir, new DatabaseIOFileFilterImpl("val"), null);
				while (valIter.hasNext()) {
					valFiles.add(valIter.next());
				}
			} else {
				throw new Exception("Unable to find '" + dbPath + "'");
			}
		} catch (final Exception e) {
			throw new PDXReaderException("Error reading '" + dbPath + "'", e);
		}
	}

	public void readBlobs() throws PDXReaderException, FileNotFoundException {
		for (final File file : mbFiles) {
			final MBTableFile mbTableFile = new MBTableFile();
			mbTableFile.read(file);
		}
	}

	public void readPrimaryIndices() throws FileNotFoundException, PDXReaderException {
		// for (final File file : pxFiles) {
		// new PXFile();
		// }
	}

	public void readTables(PDXTableListener pdxReaderListener) throws FileNotFoundException, PDXReaderException {
		// for (final File file : dbFiles) {
		// final DBTableFile dbTableFile = new DBTableFile();
		// dbTableFile.read(file, pdxReaderListener);
		// }
	}
}
