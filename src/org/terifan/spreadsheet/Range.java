package org.terifan.spreadsheet;

import java.util.Iterator;


public class Range implements Iterable<Tuple>
{
	private String mReference;
	private Tuple mStart;
	private Tuple mEnd;


	public Range(String aReference, String aExpression)
	{
		this(aExpression);

		mReference = aReference;
	}


	public Range(String aExpression)
	{
		if (aExpression.contains(":"))
		{
			mStart = new Tuple(aExpression.split(":")[0]);
			mEnd = new Tuple(aExpression.split(":")[1]);
		}
		else
		{
			mStart = mEnd = new Tuple(aExpression);
		}
	}


	public Range(Tuple aStartEnd)
	{
		mStart = aStartEnd;
		mEnd = aStartEnd;
	}


	public Range(Tuple aStart, Tuple aEnd)
	{
		mStart = aStart;
		mEnd = aEnd;
	}


	String getReference()
	{
		return mReference;
	}


	public Tuple getStart()
	{
		return mStart;
	}


	public Tuple getEnd()
	{
		return mEnd;
	}


	@Override
	public Iterator<Tuple> iterator()
	{
		return new Iterator<Tuple>()
		{
			int minRow = Math.min(mStart.getRow(), mEnd.getRow());
			int maxRow = Math.max(mStart.getRow(), mEnd.getRow());
			int minCol = Math.min(mStart.getCol(), mEnd.getCol());
			int maxCol = Math.max(mStart.getCol(), mEnd.getCol());
			int row = minRow;
			int col = minCol;


			@Override
			public boolean hasNext()
			{
				return row <= maxRow;
			}


			@Override
			public Tuple next()
			{
				Tuple t = new Tuple(row, col);
				col++;
				if (col > maxCol)
				{
					col = minCol;
					row++;
				}
				return t;
			}
		};
	}


	@Override
	public String toString()
	{
		return mStart.equals(mEnd) ? mStart.toString() : (mStart + ":" + mEnd);
	}
}
