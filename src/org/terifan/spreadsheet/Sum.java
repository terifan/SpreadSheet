package org.terifan.spreadsheet;


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
			Number value = new Number();

			mRange.forEach(t->{
				CellValue v = aSpreadSheet.getComputed(t, aTimeCode);

				if (v instanceof Number)
				{
					value.add((Number)v);
				}
			});
			
			mValue = value;
			mTimeCode = aTimeCode;
		}

		return mValue;
	}


	@Override
	public CellValue get()
	{
		return mValue;
	}
}
