package org.terifan.spreadsheet;


public class BooleanValue implements CellValue
{
	private Boolean mValue;


	public BooleanValue()
	{
	}


	public BooleanValue(Boolean aValue)
	{
		mValue = aValue;
	}


	public Boolean getValue()
	{
		return mValue;
	}


	@Override
	public String toString()
	{
		return "" + mValue;
	}


	@Override
	public boolean equals(Object aOther)
	{
		if (aOther instanceof BooleanValue)
		{
			return ((BooleanValue)aOther).mValue.equals(mValue);
		}

		return false;
	}


	@Override
	public BooleanValue clone()
	{
		try
		{
			return (BooleanValue)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
