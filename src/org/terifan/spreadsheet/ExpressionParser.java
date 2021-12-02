package org.terifan.spreadsheet;

import java.util.ArrayList;


public class ExpressionParser
{
	public ExpressionParser()
	{
	}


	public Expression compile(String aInput)
	{
		ArrayList<ExpressionStatement> expressions = new ArrayList<>();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < aInput.length(); i++)
		{
			char t = 0xffff;

			for (; i < aInput.length(); i++)
			{
				char c = aInput.charAt(i);
				if (c == t)
				{
					sb.append(c);
					break;
				}
				if (t != 0xffff)
				{
					sb.append(c);
					continue;
				}
				if (c == '\'' || c == '\"' || c == '[')
				{
					t = c == '[' ? ']' : c;
				}
				else if (!(c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '.' || c == ':'  || c == '$'))
				{
					String statement = sb.toString().trim();

					if (c != '(' || !statement.matches("[a-zA-Z]+"))
					{
						sb.setLength(0);
						if (statement.length() > 0)
						{
							expressions.add(new ExpressionStatement(statement));
						}
					}
					sb.append(c);
					break;
				}
				sb.append(c);
			}

			String statement = sb.toString().trim();
			sb.setLength(0);

			if (statement.length() > 0)
			{
				expressions.add(new ExpressionStatement(statement));
			}
		}

		return new Expression(expressions);
	}


	public static void main(String ... args)
	{
		try
		{
//			System.out.println(new ExpressionParser().compile("a1").dump());
//			System.out.println();
//			System.out.println(new ExpressionParser().compile("[sheet1]!a1").dump());
//			System.out.println();
//			System.out.println(new ExpressionParser().compile("[book][sheet1]!a1").dump());
//			System.out.println();
//			System.out.println(new ExpressionParser().compile("[book][sheet1]!a1:b3").dump());

			DataProvider data1 = new DataProvider()
			{
				@Override
				public CellValue getComputeValueAt(String aDataSource, int aRow, int aColumn, long aTimeCode)
				{
					if (aDataSource == null) return new CellValue(1);
					if (aDataSource.equals("[book],[sheet1]")) return new CellValue(1);
					if (aDataSource.equals("[book],[sheet2]")) return new CellValue(100);
					throw new IllegalArgumentException(aDataSource);
				}


				@Override
				public SpreadSheetFunctions getFunctions()
				{
					return null;
				}
			};

//			System.out.println(new ExpressionParser().compile("sum(a1:b3)").execute(data1, 0));
//			System.out.println(new ExpressionParser().compile("sum(a1:b3)+sum(a1:b3)").execute(data1, 0));
//			System.out.println(new ExpressionParser().compile("sum([book][sheet1]!a1:b3)+sum([book][sheet2]!a1:b3)").execute(data1, 0));
//			System.out.println(new ExpressionParser().compile("[book][sheet1]!a1+[book][sheet2]!a1").execute(data1, 0));
//			System.out.println(new ExpressionParser().compile("vlookup([data.csv][sheet1],m2,b2,e24)").execute(data1, 0));
//			System.out.println(new ExpressionParser().compile("vlookup([data.csv][sheet1],$e24,$f$2,$m$2)").dump());
			System.out.println(new ExpressionParser().compile("sum(0,'test')").execute(data1, 0));
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}
}
