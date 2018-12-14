package org.terifan.spreadsheet;


public class TextValue implements CellValue
{
	private String mText;


	public TextValue()
	{
	}


	public TextValue(String aValue)
	{
		mText = aValue;
	}


	public String getValue()
	{
		return mText;
	}


	@Override
	public String toString()
	{
		return mText;
	}


	@Override
	public boolean equals(Object aOther)
	{
		if (aOther instanceof TextValue)
		{
			return ((TextValue)aOther).mText.equals(mText);
		}

		return false;
	}


	@Override
	public TextValue clone()
	{
		try
		{
			return (TextValue)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
