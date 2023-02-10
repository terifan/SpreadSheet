package org.terifan.spreadsheet;

import java.io.Serializable;


public class Tuple implements Serializable
{
	private final static long serialVersionUID = 1L;

	private boolean mLockedRow;
	private boolean mLockedCol;
	private int mRow;
	private int mCol;


	public Tuple(int aRow, int aCol)
	{
		if (aCol < 0 || aRow < 0)
		{
			throw new IllegalArgumentException("Illegal position: " + aRow + ", " + aCol);
		}

		mCol = aCol;
		mRow = aRow + 1;
	}


	/**
	 * Create a Tuple from an expression
	 * @param aExpression
	 *   the column and row e.g. "a1". Supports locking of either or both, e.g. "$a1" is locked at column A while row 1 is unlocked. This
	 *   locking mechanism effects how relative positions are computed.
	 */
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
	 * Create a Tuple relative to this Tuple accounting for locked column/row (i.e. if a row is locked then the row value provided is ignored).<br/>
	 * Relative position from a locked position:
	 * <pre>
	 * new Tuple("a1").relative(1,1) return a Tuple with column B and row 2
	 * new Tuple("$a1").relative(1,1) return a Tuple with column A and row 2
	 * new Tuple("a$1").relative(1,1) return a Tuple with column B and row 1
	 * new Tuple("$a$1").relative(1,1) return a Tuple with column A and row 1
	 * </pre>
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
