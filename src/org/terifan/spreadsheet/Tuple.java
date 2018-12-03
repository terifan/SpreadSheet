package org.terifan.spreadsheet;


public class Tuple
{
	int mRow;
	int mCol;


	public Tuple(int aRow, int aCol)
	{
		mRow = aRow;
		mCol = aCol;
	}


	public int getCol()
	{
		return mCol;
	}


	public int getRow()
	{
		return mRow;
	}
}
