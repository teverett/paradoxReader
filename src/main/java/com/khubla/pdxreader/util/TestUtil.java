package com.khubla.pdxreader.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
   private static List<String> getTestFiles(String dir, List<String> files, String extension) throws Exception {
      final File file = new File(dir);
      final String[] list = file.list();
      for (int i = 0; i < list.length; i++) {
         {
            final String fileName = dir + list[i];
            final File f2 = new File(fileName);
            if (f2.isDirectory()) {
               getTestFiles(fileName + "/", files, extension);
            } else if (fileName.endsWith(extension)) {
               files.add(fileName);
            }
         }
      }
      return files;
   }

   public static List<String> getTestFiles(String dir, String extension) throws Exception {
      return getTestFiles(dir, new ArrayList<String>(), extension);
   }
}