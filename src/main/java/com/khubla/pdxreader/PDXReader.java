package com.khubla.pdxreader;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

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
         final Option oo = Option.builder().argName(FILE_OPTION).longOpt(FILE_OPTION).type(String.class).hasArg().required(true).desc("file to read").build();
         options.addOption(oo);
         /*
          * parse
          */
         final CommandLineParser parser = new DefaultParser();
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
            pdxFile.read(inputFile, pdxReaderListener);
            System.out.println("done");
         }
      } catch (final Exception e) {
         e.printStackTrace();
      }
   }
}
