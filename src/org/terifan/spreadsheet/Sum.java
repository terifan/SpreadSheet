package org.terifan.spreadsheet;


public class Sum implements Formula
{
	private final Range mRange;


	public Sum(Range aRange)
	{
		mRange = aRange;
	}


	Object get()
	{
		return 0;
	}
}
