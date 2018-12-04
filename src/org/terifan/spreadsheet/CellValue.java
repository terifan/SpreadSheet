package org.terifan.spreadsheet;


public interface CellValue<T extends CellValue> extends Cloneable
{
	T clone();
}
