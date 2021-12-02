package org.terifan.spreadsheet;


public class Convert
{
	public static String formatColumnIndex(int aColumn)
	{
		if (aColumn < 0)
		{
			throw new IllegalArgumentException("aColumn=" + aColumn);
		}
		if (aColumn < 26)
		{
			return Character.toString((char)('A' + aColumn));
		}

		return formatColumnIndex(aColumn / 26 - 1) + formatColumnIndex(aColumn % 26);
	}


	public static int parseColumnIndex(String aColumn)
	{
		if (aColumn.length() == 1)
		{
			return Character.toUpperCase(aColumn.charAt(0)) - 'A';
		}

		return 26 * (1 + parseColumnIndex(aColumn.substring(0, aColumn.length() - 1))) + parseColumnIndex(aColumn.substring(aColumn.length() - 1));
	}


	public static double toDouble(Object aValue)
	{
		try
		{
			if (aValue == null)
			{
				return 0.0;
			}
			if (aValue instanceof CellValue)
			{
				aValue = ((CellValue)aValue).getValue();
			}
			if (aValue instanceof ExpressionStatement)
			{
				aValue = ((ExpressionStatement)aValue).getStatement();
			}
			if (aValue instanceof Number)
			{
				return ((Number)aValue).doubleValue();
			}
			if (aValue instanceof String)
			{
				String s = (String)aValue;
				if (s.isEmpty())
				{
					return 0.0;
				}
				int i = s.indexOf(',');
				int j = s.indexOf('.');
				if (i != -1 && j != -1)
				{
					return Double.parseDouble(i < j ? s.replace(",", "") : s.replace(".", "").replace(",", "."));
				}
				return Double.parseDouble(s.replace(",", "."));
			}
		}
		catch (Exception e)
		{
		}
		throw new IllegalStateException("value=" + aValue + " (" + (aValue == null ? "null" : aValue.getClass()) + ")");
	}


	public static long toLong(Object aValue)
	{
		try
		{
			if (aValue == null)
			{
				return 0;
			}
			if (aValue instanceof CellValue)
			{
				aValue = ((CellValue)aValue).getValue();
			}
			if (aValue instanceof ExpressionStatement)
			{
				aValue = ((ExpressionStatement)aValue).getStatement();
			}
			if (aValue instanceof Number)
			{
				return ((Number)aValue).longValue();
			}
			if (aValue instanceof String)
			{
				String s = (String)aValue;
				if (s.isEmpty())
				{
					return 0L;
				}
				return Long.parseLong(s.replace(".0", ""));
			}
		}
		catch (Exception e)
		{
		}
		throw new IllegalStateException("value=" + aValue + " (" + (aValue == null ? "null" : aValue.getClass()) + ")");
	}


	public static boolean isLong(Object aValue)
	{
		if (aValue == null)
		{
			return false;
		}
		if (aValue instanceof Long)
		{
			return true;
		}
		return aValue.toString().matches("[\\+\\-]?[0-9]+|[\\+\\-]?[0-9]+\\.0");
	}


	public static boolean isDouble(Object aValue)
	{
		if (aValue == null)
		{
			return false;
		}
		if (aValue instanceof Double)
		{
			return true;
		}
		return aValue.toString().matches("[\\+\\-]?[0-9\\,\\.]+");
	}


	public static boolean isNumber(Object aValue)
	{
		return aValue != null && isDouble(aValue);
	}


//	public static double parseDouble(String aValue)
//	{
//		return toDouble(aValue);
//	}


//	public static Number parseLong(String aValue)
//	{
//		return toLong(aValue);
//	}


//	public static Number parseNumber(String aValue)
//	{
//		return aValue.contains(".") || aValue.contains(",") ? parseDouble(aValue) : parseLong(aValue);
//	}
}
