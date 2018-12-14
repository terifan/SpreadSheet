package org.terifan.spreadsheet.functions;

import java.util.concurrent.atomic.AtomicReference;
import org.terifan.spreadsheet.CellValue;
import org.terifan.spreadsheet.Formula;
import org.terifan.spreadsheet.NumberValue;
import org.terifan.spreadsheet.Range;
import org.terifan.spreadsheet.SpreadSheet;



public class Sum implements Formula
{
	private Range mRange;
	private NumberValue mValue;
	private long mTimeCode;


	public Sum(Range aRange)
	{
		mRange = aRange;
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

			AtomicReference<NumberValue> value = new AtomicReference<>(new NumberValue());

			mRange.forEach(t->{
				CellValue v = aSpreadSheet.getComputed(t, aTimeCode);

				if (v instanceof NumberValue)
				{
					value.set(value.get().add((NumberValue)v));
				}
			});

			mValue = value.get();
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
	public Sum clone()
	{
		try
		{
			return (Sum)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
