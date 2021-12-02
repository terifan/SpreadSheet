package org.terifan.spreadsheet;


public class ExpressionStatement
{
	private Object mStatement;
	private ExpressionStatementType mType;


	public ExpressionStatement(String aStatement)
	{
		if (aStatement == null)
		{
			throw new IllegalArgumentException();
		}

		for (ExpressionStatementType type : ExpressionStatementType.values())
		{
			if (type.getPattern().matcher(aStatement).matches())
			{
				mType = type;
				mStatement = mType.getConverter().apply(aStatement);
				break;
			}
		}
	}


	public ExpressionStatement(ExpressionStatementType aType, Object aStatement)
	{
		mType = aType;
		mStatement = aStatement;
	}


	public Object getStatement()
	{
		return mStatement;
	}


	public ExpressionStatementType getType()
	{
		return mType;
	}


	@Override
	public String toString()
	{
		return mStatement == null ? "" : mStatement.toString();
	}
}
