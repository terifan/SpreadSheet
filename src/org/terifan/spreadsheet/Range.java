package org.terifan.spreadsheet;

import java.util.function.Consumer;

public class Range
{
	private Tuple mStart;
	private Tuple mEnd;


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


	public Tuple getStart()
	{
		return mStart;
	}


	public Tuple getEnd()
	{
		return mEnd;
	}
	
	
	public void forEach(Consumer<Tuple> aConsumer)
	{
		for (int row = Math.min(mStart.mRow,mEnd.mRow); row <= Math.max(mStart.mRow,mEnd.mRow); row++)
		{
			for (int col = Math.min(mStart.mCol,mEnd.mCol); col <= Math.max(mStart.mCol,mEnd.mCol); col++)
			{
				aConsumer.accept(new Tuple(col, row));
			}
		}
	}
}
