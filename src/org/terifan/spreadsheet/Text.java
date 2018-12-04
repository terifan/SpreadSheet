package org.terifan.spreadsheet;


public class Text implements CellValue
{
	private String mText;


	public Text()
	{
	}


	public Text(String aValue)
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
		if (aOther instanceof Text)
		{
			return ((Text)aOther).mText.equals(mText);
		}

		return false;
	}


	@Override
	public Text clone()
	{
		try
		{
			return (Text)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
