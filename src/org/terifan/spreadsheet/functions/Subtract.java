package org.terifan.spreadsheet.functions;

import org.terifan.spreadsheet.CellValue;
import org.terifan.spreadsheet.Formula;
import org.terifan.spreadsheet.Number;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.Tuple;



public class Subtract implements Formula
{
	private Tuple mSource1;
	private Tuple mSource2;
	private Number mValue;
	private long mTimeCode;


	public Subtract(Tuple aSource1, Tuple aSource2)
	{
		mSource1 = aSource1;
		mSource2 = aSource2;
	}


	@Override
	public CellValue compute(SpreadSheet aSpreadSheet, long aTimeCode)
	{
		if (mTimeCode != aTimeCode)
		{
			if (mTimeCode == Long.MIN_VALUE)
			{
				throw new IllegalStateException("Recursion detected");
			}
			mTimeCode = Long.MIN_VALUE;

			Number v0 = aSpreadSheet.getComputedNumber(mSource1, aTimeCode);
			Number v1 = aSpreadSheet.getComputedNumber(mSource2, aTimeCode);

			if (v0 == null)
			{
				if (v1 == null)
				{
					return null;
				}
				mValue = v1.scale(-1);
			}
			else if (v1 != null)
			{
				mValue = v0.add(v1.scale(-1));
			}
			else
			{
				mValue = null;
			}
			mTimeCode = aTimeCode;
		}

		return mValue;
	}


	@Override
	public CellValue get()
	{
		return mValue;
	}


	@Override
	public Subtract clone()
	{
		try
		{
			return (Subtract)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
