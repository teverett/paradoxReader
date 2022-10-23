package com.khubla.pdxreader;

import java.io.*;

import org.apache.commons.cli.*;

import com.khubla.pdxreader.api.*;
import com.khubla.pdxreader.db.*;
import com.khubla.pdxreader.listener.*;

/**
 * @author tom
 */
public class PDXReader {
	/**
	 * file option
	 */
	private static final String FILE_OPTION = "file";
	/**
	 * dir option
	 */
	private static final String DIR_OPTION = "dir";

	public static void main(String[] args) {
		try {
			System.out.println("khubla.com Paradox DB reader");
			/*
			 * options
			 */
			final Options options = new Options();
			Option oo = Option.builder().argName(FILE_OPTION).longOpt(FILE_OPTION).type(String.class).hasArg().required(false).desc("file to read").build();
			options.addOption(oo);
			oo = Option.builder().argName(DIR_OPTION).longOpt(DIR_OPTION).type(String.class).hasArg().required(false).desc("dir to read").build();
			options.addOption(oo);
			/*
			 * parse
			 */
			final CommandLineParser parser = new DefaultParser();
			CommandLine cmd = null;
			try {
				cmd = parser.parse(options, args);
				/*
				 * get file
				 */
				final String filename = cmd.getOptionValue(FILE_OPTION);
				if (null != filename) {
					final File inputFile = new File(filename);
					if (inputFile.exists()) {
						final DBTableFile pdxFile = new DBTableFile();
						final PDXTableListener pdxReaderListener = new PDXTableReaderCSVListenerImpl();
						pdxFile.read(inputFile, pdxReaderListener);
					}
				}
				/*
				 * get dir
				 */
				final String dirname = cmd.getOptionValue(DIR_OPTION);
				if (null != dirname) {
					final Database database = new Database(dirname);
					final PDXTableListener pdxReaderListener = new PDXTableReaderCSVListenerImpl();
					database.readTables(pdxReaderListener);
				}
				System.out.println("done");
			} catch (final Exception e) {
				e.printStackTrace();
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("posix", options);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
