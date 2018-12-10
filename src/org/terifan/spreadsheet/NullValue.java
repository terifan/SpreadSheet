package org.terifan.spreadsheet;


public class NullValue implements CellValue
{
	public NullValue()
	{
	}


	@Override
	public String toString()
	{
		return "null";
	}


	@Override
	public boolean equals(Object aOther)
	{
		return aOther instanceof NullValue;
	}


	@Override
	public NullValue clone()
	{
		try
		{
			return (NullValue)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
