package com.khubla.pdxreader.api;

import com.khubla.pdxreader.px.*;
import com.khubla.pdxreader.px.block.*;

/**
 * @author tom
 */
public interface PDXIndexListener {
	void finish();

	void header(PXFileHeader pxFileHeader);

	void record(PXIndexBlockRecord pxIndexBlockRecord);

	void start();
}
