package org.terifan.spreadsheet;


public class NumberValue implements CellValue
{
	private boolean mFloatingPoint;
	private double mValue;
	private boolean mUndefined;


	public NumberValue()
	{
		mUndefined = true;
	}


	public NumberValue(double aValue)
	{
		mValue = aValue;
		mFloatingPoint = true;
	}


	public NumberValue(long aValue)
	{
		mValue = aValue;
	}


	public NumberValue(Double aValue, boolean aFloatingPoint)
	{
		mValue = aValue;
		mFloatingPoint = aFloatingPoint;
	}


	public NumberValue add(NumberValue aNumber)
	{
		return new NumberValue(mValue + aNumber.mValue, mFloatingPoint | aNumber.mFloatingPoint);
	}


	public NumberValue add(double aValue)
	{
		return new NumberValue(mValue + aValue, true);
	}


	public NumberValue add(long aValue)
	{
		return new NumberValue(mValue + aValue, mFloatingPoint);
	}


	public NumberValue scale(NumberValue aNumber)
	{
		return new NumberValue(mValue * aNumber.mValue, mFloatingPoint | aNumber.mFloatingPoint);
	}


	public NumberValue scale(double aScale)
	{
		return new NumberValue(mValue * aScale, true);
	}


	public NumberValue scale(long aScale)
	{
		return new NumberValue(mValue * aScale, mFloatingPoint);
	}


	public double doubleValue()
	{
		return mValue;
	}


	public long longValue()
	{
		return (long)mValue;
	}


	public boolean isFloatingPoint()
	{
		return mFloatingPoint;
	}


	public boolean isUndefined()
	{
		return mUndefined;
	}


	@Override
	public String toString()
	{
		return mUndefined ? "" : mFloatingPoint ? "" + mValue : "" + (long)mValue;
	}


	@Override
	public boolean equals(Object aOther)
	{
		if (aOther instanceof NumberValue)
		{
			return ((NumberValue)aOther).mValue == mValue;
		}

		return false;
	}


	@Override
	public NumberValue clone()
	{
		try
		{
			return (NumberValue)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
