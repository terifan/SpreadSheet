package org.terifan.spreadsheet;


public class Tuple
{
	int mRow;
	int mCol;


	public Tuple(int aCol , int aRow)
	{
		mCol = aCol;
		mRow = aRow;
	}


	public int getCol()
	{
		return mCol;
	}


	public int getRow()
	{
		return mRow;
	}


	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 97 * hash + this.mRow;
		hash = 97 * hash + this.mCol;
		return hash;
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Tuple other = (Tuple)obj;
		if (this.mRow != other.mRow)
		{
			return false;
		}
		if (this.mCol != other.mCol)
		{
			return false;
		}
		return true;
	}


	@Override
	public String toString()
	{
		return "Tuple{" + "mRow=" + mRow + ", mCol=" + mCol + '}';
	}
}
