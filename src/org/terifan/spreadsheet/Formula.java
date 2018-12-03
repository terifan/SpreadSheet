package org.terifan.spreadsheet;


public interface Formula extends CellValue
{
	CellValue compute(SpreadSheet aSpreadSheet, long aTimeCode);
	
	CellValue get();
}
