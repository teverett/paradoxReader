package com.khubla.pdxreader;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.khubla.pdxreader.api.PDXReaderListener;
import com.khubla.pdxreader.db.DBTableFile;

/**
 * @author tom
 */
public class PDXReader {
   /**
    * file option
    */
   private static final String FILE_OPTION = "file";

   public static void main(String[] args) {
      try {
         System.out.println("khubla.com Paradox DB reader");
         /*
          * options
          */
         final Options options = new Options();
         OptionBuilder.withArgName(FILE_OPTION);
         OptionBuilder.isRequired();
         OptionBuilder.withType(String.class);
         OptionBuilder.hasArg();
         OptionBuilder.withDescription("file to compile");
         final Option fo = OptionBuilder.create(FILE_OPTION);
         options.addOption(fo);
         /*
          * parse
          */
         final CommandLineParser parser = new PosixParser();
         CommandLine cmd = null;
         try {
            cmd = parser.parse(options, args);
         } catch (final Exception e) {
            e.printStackTrace();
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("posix", options);
            System.exit(0);
         }
         /*
          * get file
          */
         final String filename = cmd.getOptionValue(FILE_OPTION);
         final File inputFile = new File(filename);
         if (inputFile.exists()) {
            final DBTableFile pdxFile = new DBTableFile();
            final PDXReaderListener pdxReaderListener = new PDXReaderCSVListenerImpl();
            pdxFile.read(new FileInputStream(inputFile), pdxReaderListener);
            System.out.println("done");
         }
      } catch (final Exception e) {
         e.printStackTrace();
      }
   }
}
