package org.terifan.spreadsheet.ui;

import org.terifan.spreadsheet.SpreadSheet;


public interface CellSelectionListener
{
	void changeSelection(WorkBook aWorkBook, SpreadSheet aSpreadSheet, int aRow, int aColumn, boolean aToggle, boolean aExtend);
}
