package org.terifan.spreadsheet;


public class EnumValue implements CellValue
{
	private Enum mValue;


	public EnumValue()
	{
	}


	public EnumValue(Enum aValue)
	{
		mValue = aValue;
	}


	public Enum getValue()
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
		if (aOther instanceof EnumValue)
		{
			return ((EnumValue)aOther).mValue.equals(mValue);
		}

		return false;
	}


	@Override
	public EnumValue clone()
	{
		try
		{
			return (EnumValue)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
