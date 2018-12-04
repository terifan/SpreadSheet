package org.terifan.spreadsheet;


public class Number implements CellValue
{
	private boolean mFloatingPoint;
	private double mValue;
	private boolean mUndefined;


	public Number()
	{
		mUndefined = true;
	}


	public Number(double aValue)
	{
		mValue = aValue;
		mFloatingPoint = true;
	}


	public Number(long aValue)
	{
		mValue = aValue;
	}


	public Number(Double aValue, boolean aFloatingPoint)
	{
		mValue = aValue;
		mFloatingPoint = aFloatingPoint;
	}


	public Number add(Number aNumber)
	{
		return new Number(mValue + aNumber.mValue, mFloatingPoint | aNumber.mFloatingPoint);
	}


	public Number add(double aValue)
	{
		return new Number(mValue + aValue, true);
	}


	public Number add(long aValue)
	{
		return new Number(mValue + aValue, mFloatingPoint);
	}


	public Number scale(Number aNumber)
	{
		return new Number(mValue * aNumber.mValue, mFloatingPoint | aNumber.mFloatingPoint);
	}


	public Number scale(double aScale)
	{
		return new Number(mValue * aScale, true);
	}


	public Number scale(long aScale)
	{
		return new Number(mValue * aScale, mFloatingPoint);
	}


	public double getValue()
	{
		return mValue;
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
		if (aOther instanceof Number)
		{
			return ((Number)aOther).mValue == mValue;
		}

		return false;
	}


	@Override
	public Number clone()
	{
		try
		{
			return (Number)super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
