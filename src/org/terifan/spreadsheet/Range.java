package org.terifan.spreadsheet;


public class Range
{
	private Tuple mStart;
	private Tuple mEnd;


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
}
