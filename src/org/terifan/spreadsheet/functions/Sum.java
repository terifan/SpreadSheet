package org.terifan.spreadsheet.functions;

import java.util.concurrent.atomic.AtomicReference;
import org.terifan.spreadsheet.CellValue;
import org.terifan.spreadsheet.Formula;
import org.terifan.spreadsheet.Number;
import org.terifan.spreadsheet.Range;
import org.terifan.spreadsheet.SpreadSheet;



public class Sum implements Formula
{
	private Range mRange;
	private Number mValue;
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

			AtomicReference<Number> value = new AtomicReference<>(new Number());

			mRange.forEach(t->{
				CellValue v = aSpreadSheet.getComputed(t, aTimeCode);

				if (v instanceof Number)
				{
					value.set(value.get().add((Number)v));
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
