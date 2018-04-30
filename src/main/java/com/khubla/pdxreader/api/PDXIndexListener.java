package com.khubla.pdxreader.api;

import com.khubla.pdxreader.px.PXFileHeader;
import com.khubla.pdxreader.px.block.PXIndexBlockRecord;

/**
 * @author tom
 */
public interface PDXIndexListener {
   void finish();

   void header(PXFileHeader pxFileHeader);

   void record(PXIndexBlockRecord pxIndexBlockRecord);

   void start();
}
