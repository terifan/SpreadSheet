package org.terifan.spreadsheet;


public interface DataProvider
{
	CellValue getComputeValueAt(String aReference, int aRow, int aColumn, long aTimeCode);

	SpreadSheetFunctions getFunctions();
}
