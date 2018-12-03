package org.terifan.spreadsheet;


public class Number implements CellValue
{
	private boolean mFloatingPoint;
	private double mDouble;
	private long mLong;
	private int mCount;


	public Number()
	{
	}


	public Number(double aDouble)
	{
		add(aDouble);
	}


	public Number(long aLong)
	{
		add(aLong);
	}


	public void add(Number aNumber)
	{
		if (mFloatingPoint)
		{
			if (aNumber.mFloatingPoint)
			{
				mDouble += aNumber.mDouble;
			}
			else
			{
				mDouble += aNumber.mLong;
			}
		}
		else
		{
			if (aNumber.mFloatingPoint)
			{
				mFloatingPoint = true;
				mDouble += mLong + aNumber.mDouble;
				mLong = 0;
			}
			else
			{
				mLong += aNumber.mLong;
			}
		}
		mCount += aNumber.mCount;
	}


	public void add(double aDouble)
	{
		if (!mFloatingPoint)
		{
			mFloatingPoint = true;
			mDouble = mLong;
			mLong = 0;
		}
		mDouble += aDouble;
		mCount++;
	}


	public void add(long aLong)
	{
		if (mFloatingPoint)
		{
			mDouble += aLong;
		}
		else
		{
			mLong += aLong;
		}
		mCount++;
	}


	public double getDouble()
	{
		return mDouble;
	}


	public long getLong()
	{
		return mLong;
	}


	public boolean isFloatingPoint()
	{
		return mFloatingPoint;
	}


	public int getCount()
	{
		return mCount;
	}


	@Override
	public String toString()
	{
		return mCount == 0 ? "undefined" : mFloatingPoint ? "" + mDouble : "" + mLong;
	}
}
