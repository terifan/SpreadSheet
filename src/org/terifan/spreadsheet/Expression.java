package org.terifan.spreadsheet;

import java.util.ArrayList;
import static org.terifan.spreadsheet.ExpressionStatementType.FUNCTION;
import static org.terifan.spreadsheet.ExpressionStatementType.LPARAN;
import static org.terifan.spreadsheet.ExpressionStatementType.RPARAN;


public class Expression
{
	private final ArrayList<ExpressionStatement> mExpressions;


	Expression(ArrayList<ExpressionStatement> aExpressions)
	{
		mExpressions = aExpressions;
	}


	public CellValue execute(DataProvider aContext, long aTimeCode)
	{
		ExpressionRunner runtime = new ExpressionRunner(new ArrayList<>(mExpressions), aContext, aTimeCode);

		CellValue result = runtime.parseExpression();

		runtime.validate();

		return result;
	}


	public ArrayList<ExpressionStatement> getStatements()
	{
		return mExpressions;
	}


	public void replaceExpression(ExpressionStatement aExpression, Object aNewStatement)
	{
		int i = mExpressions.indexOf(aExpression);
		mExpressions.set(i, new ExpressionStatement(aExpression.getType(), aNewStatement));
	}


	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (ExpressionStatement e : mExpressions)
		{
			sb.append(e);
			if (e.getType() == FUNCTION)
			{
				sb.append("(");
			}
		}

		return sb.toString();
	}


	public String dump()
	{
		StringBuilder sb = new StringBuilder();

		String indent = "";
		for (ExpressionStatement e : mExpressions)
		{
			if (sb.length() > 0)
			{
				sb.append("\n");
			}
			if (e.getType() == RPARAN)
			{
				indent = indent.substring(0, indent.length() - 3);
			}
			sb.append(String.format("%9s : %s%s", e.getType(), indent, e.getStatement()));
			if (e.getType() == LPARAN || e.getType() == FUNCTION)
			{
				indent += "   ";
			}
		}

		return sb.toString();
	}
}
