package org.terifan.spreadsheet;


public class Tuple
{
	private boolean mLockedRow;
	private boolean mLockedCol;
	private int mRow;
	private int mCol;


	public Tuple(int aRow, int aCol)
	{
		mCol = aCol;
		mRow = aRow + 1;

		if (mCol < 0 || mRow <= 0)
		{
			throw new IllegalArgumentException("Illegal position: " + this);
		}
	}


	public Tuple(String aExpression)
	{
		mLockedCol = aExpression.startsWith("$");

		int i = mLockedCol ? 1 : 0;

		for (; i < aExpression.length() && !(aExpression.charAt(i) >= '0' && aExpression.charAt(i) <= '9'); i++)
		{
		}

		mLockedRow = aExpression.charAt(i - 1) == '$';

		mCol = Convert.parseColumnIndex(aExpression.substring((mLockedCol ? 1 : 0), (mLockedRow ? i-1 : i)));
		mRow = Integer.parseInt(aExpression.substring(i));

		if (mCol < 0 || mRow <= 0)
		{
			throw new IllegalArgumentException("Illegal position: " + this);
		}
	}


	public int getRow()
	{
		return mRow - 1;
	}


	public int getCol()
	{
		return mCol;
	}


	/**
	 * Create a Tuple relative to this Tuple accounting for locked column/row.
	 */
	public Tuple relative(int aRow, int aCol)
	{
		Tuple t = new Tuple(getRow() + (mLockedRow ? 0 : aRow), getCol() + (mLockedCol ? 0 : aCol));
		t.mLockedCol = mLockedCol;
		t.mLockedRow = mLockedRow;
		return t;
	}


	@Override
	public int hashCode()
	{
		return Integer.rotateLeft(this.mRow, 16) ^ this.mCol;
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
		return (mLockedCol ? "$" : "") + Convert.formatColumnIndex(mCol) + (mLockedRow ? "$" : "") + mRow;
	}


//	public static void main(String ... args)
//	{
//		try
//		{
//			System.out.println(new Tuple("A1").relative(1, 1));
//			System.out.println(new Tuple("$A1").relative(1, 1));
//			System.out.println(new Tuple("A$1").relative(1, 1));
//			System.out.println(new Tuple("$A$1").relative(1, 1));
//			System.out.println(new Tuple("A1").relative(-1, 0));
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace(System.out);
//		}
//	}
}
