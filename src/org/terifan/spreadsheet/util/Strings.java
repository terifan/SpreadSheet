package org.terifan.spreadsheet.util;


public class Strings
{
	public static String nullToEmpty(Object aString)
	{
		return aString == null ? "" : aString.toString();
	}


	public static String toString(byte[] aString)
	{
		return aString == null ? "" : new String(aString);
	}
}
