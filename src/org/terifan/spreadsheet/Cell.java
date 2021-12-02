package org.terifan.spreadsheet;

import static org.terifan.spreadsheet.Convert.isLong;


public class Cell
{
	private final SpreadSheet mSpreadSheet;
	private String mValue;
	private CellValue mDisplayValue;
	private CellStyle mStyle;
	private Expression mExpression;
	private long mTimeCode;


	Cell(SpreadSheet aSpreadSheet)
	{
		mSpreadSheet = aSpreadSheet;
	}


	public CellValue getDisplayValue(long aTimeCode)
	{
		if (mTimeCode != aTimeCode)
		{
			if (mTimeCode == Long.MIN_VALUE)
			{
				throw new IllegalStateException("Recursion detected");
			}

			mTimeCode = Long.MIN_VALUE;

			mDisplayValue = computeValue(aTimeCode);
			if (mDisplayValue == null)
			{
				mDisplayValue = new CellValue("");
			}

			mTimeCode = aTimeCode;
		}

		return mDisplayValue;
	}


	CellValue computeValue(long aTimeCode)
	{
		try
		{
			Object value = mValue;

			if (mValue != null && mValue.startsWith("="))
			{
				if (mExpression == null)
				{
					mExpression = new ExpressionParser().compile(mValue.substring(1));
				}

				value = mExpression.execute(mSpreadSheet, aTimeCode);
			}

			if (isLong(value))
			{
				return new CellValue(Convert.toLong(value));
			}

			return new CellValue(value);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);

			return new CellValue("ERROR");
		}
	}


	public String getValue()
	{
		return mValue;
	}


	public Cell setValue(String aValue)
	{
		mExpression = null;
		mValue = aValue;
		return this;
	}


	public CellStyle getStyle()
	{
		return mStyle;
	}


	public Cell setStyle(CellStyle aStyle)
	{
		mStyle = aStyle;
		return this;
	}


	@Override
	public String toString()
	{
		return mValue;
	}
}
