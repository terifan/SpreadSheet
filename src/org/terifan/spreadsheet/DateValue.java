package org.terifan.spreadsheet;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateValue implements CellValue
{
	private Date mDate;


	public DateValue()
	{
	}


	public DateValue(Date aValue)
	{
		mDate = aValue;
	}


	public Date getValue()
	{
		return mDate;
	}


	@Override
	public String toString()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(mDate);
	}


	@Override
	public boolean equals(Object aOther)
	{
		if (aOther instanceof DateValue)
		{
			return ((DateValue)aOther).mDate.equals(mDate);
		}

		return false;
	}


	@Override
	public DateValue clone()
	{
		try
		{
			return (DateValue)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
