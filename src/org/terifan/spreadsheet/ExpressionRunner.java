package org.terifan.spreadsheet;

import java.util.ArrayList;
import static org.terifan.spreadsheet.ExpressionStatementType.DIV;
import static org.terifan.spreadsheet.ExpressionStatementType.FUNCTION;
import static org.terifan.spreadsheet.ExpressionStatementType.LITERAL;
import static org.terifan.spreadsheet.ExpressionStatementType.LPARAN;
import static org.terifan.spreadsheet.ExpressionStatementType.MINUS;
import static org.terifan.spreadsheet.ExpressionStatementType.MUL;
import static org.terifan.spreadsheet.ExpressionStatementType.NAME;
import static org.terifan.spreadsheet.ExpressionStatementType.NUMBER;
import static org.terifan.spreadsheet.ExpressionStatementType.PLUS;
import static org.terifan.spreadsheet.ExpressionStatementType.RANGE;
import static org.terifan.spreadsheet.ExpressionStatementType.REF;
import static org.terifan.spreadsheet.ExpressionStatementType.RPARAN;
import static org.terifan.spreadsheet.ExpressionStatementType.SEPARATOR;
import static org.terifan.spreadsheet.ExpressionStatementType.TUPLE;


class ExpressionRunner
{
	private final ArrayList<ExpressionStatement> mExpressions;
	private DataProvider mDataProvider;
	private long mTimeCode;
	private int mOffset;


	public ExpressionRunner(ArrayList<ExpressionStatement> aExpressions, DataProvider aContext, long aTimeCode)
	{
		mExpressions = aExpressions;
		mDataProvider = aContext;
		mTimeCode = aTimeCode;
	}


	public CellValue parseExpression()
	{
		CellValue v = parseTerm();
		while (mOffset < mExpressions.size())
		{
			ExpressionStatement exp = mExpressions.get(mOffset);
			if (exp.getType() == PLUS) {mOffset++; v = v.add(parseTerm());}
			else if (exp.getType() == MINUS) {mOffset++; v = v.sub(parseTerm());}
			else if (exp.getType() == RPARAN) break;
			else if (exp.getType() == SEPARATOR) break;
			else if (exp.getType() == REF) break;
			else throw new IllegalStateException(exp.toString());
		}
		return v;
	}


	private CellValue parseTerm()
	{
		CellValue v = parseFactor();
		while (mOffset < mExpressions.size())
		{
			ExpressionStatement exp = mExpressions.get(mOffset);
			if (exp.getType() == MUL) {mOffset++; v = v.mul(parseFactor());}
			else if (exp.getType() == DIV) {mOffset++; v = v.div(parseFactor());}
			else if (exp.getType() == RPARAN) break;
			else if (exp.getType() == SEPARATOR) break;
			else if (exp.getType() == MINUS) break;
			else if (exp.getType() == PLUS) break;
			else if (exp.getType() == REF) break;
			else throw new IllegalStateException(exp.toString());
		}
		return v;
	}


	private CellValue parseFactor()
	{
		ExpressionStatement exp = mExpressions.get(mOffset);

		mOffset++;

		if (exp.getType() == PLUS) return parseFactor();
		if (exp.getType() == MINUS) return parseFactor().mul(new CellValue(-1L));

		CellValue v;
		if (exp.getType() == LPARAN)
		{
			v = parseExpression();
			expect(RPARAN);
		}
		else if (exp.getType() == FUNCTION)
		{
			String fn = exp.getStatement().toString().toUpperCase();

			if ("VLOOKUP".equals(fn))
			{
				String book = null;
				String sheet;

				sheet = (String)mExpressions.get(mOffset).getStatement();
				mOffset++;

				if (mExpressions.get(mOffset).getType() == NAME)
				{
					book = sheet;
					sheet = (String)mExpressions.get(mOffset).getStatement();
					mOffset++;
				}

				expect(SEPARATOR);
				Tuple s0 = (Tuple)mExpressions.get(mOffset).getStatement();
				mOffset++;
				expect(SEPARATOR);
				Tuple s1 = (Tuple)mExpressions.get(mOffset).getStatement();
				mOffset++;
				expect(SEPARATOR);
				Tuple s2 = (Tuple)mExpressions.get(mOffset).getStatement();
				mOffset++;

				v = mDataProvider.getFunctions().vlookup(mDataProvider, book, sheet, s0, s1, s2);
			}
			else
			{
				ArrayList<Object> params = new ArrayList<>();
				for (;;)
				{
					params.add(parseExpression());

					if (mExpressions.get(mOffset).getType() == RPARAN)
					{
						break;
					}
					expect(SEPARATOR);
				}

				if ("SUM".equals(fn) || "AVG".equals(fn) || "COUNT".equals(fn))
				{
					if ("SUM".equals(fn))
					{
						System.out.println(params);
					}

					v = new CellValue(0L);
					int count = 0;
					boolean error = false;
					try
					{
						for (Object param : params)
						{
							if (param instanceof CellValue)
							{
								CellValue z = (CellValue)param;
								error |= z.isError();
								if (z.getValue() instanceof Range)
								{
									Range range = (Range)z.getValue();
									for (Tuple pos : range)
									{
										CellValue w = mDataProvider.getComputeValueAt(range.getReference(), pos.getRow(), pos.getCol(), mTimeCode);
										if (w != null)
										{
											error |= w.isError();
											if (!w.isEmpty())
											{
												count++;
											}
											if (!"COUNT".equals(fn))
											{
												v = v.add(w);
											}
										}
									}
								}
								else
								{
									if (z.getValue() instanceof Tuple)
									{
										Tuple pos = (Tuple)z.getValue();
										param = mDataProvider.getComputeValueAt(null, pos.getRow(), pos.getCol(), mTimeCode);
									}
									CellValue w = (CellValue)param;
									if (w != null)
									{
										error |= w.isError();
										if (!w.isEmpty())
										{
											count++;
										}
										if (!"COUNT".equals(fn))
										{
											v = v.add(w);
										}
									}
								}
							}
							if (error)
							{
								break;
							}
						}
					}
					catch (IllegalCellValueException e)
					{
						error = true;
					}
					if (error)
					{
						v = new CellValue(true);
					}
					else if ("AVG".equals(fn))
					{
						if (count == 0)
						{
							v = new CellValue(0.0);
						}
						else
						{
							v = v.mul(new CellValue(1.0 / count));
						}
					}
					else if ("COUNT".equals(fn))
					{
						v = new CellValue(count);
					}
				}
				else if ("POW".equals(fn))
				{
					v = new CellValue(Math.pow(((CellValue)params.get(0)).doubleValue(), ((CellValue)params.get(1)).doubleValue()));
				}
				else if ("REPLACE".equals(fn))
				{
					System.out.println(params);

					String p0 = params.get(0).toString().replace("'", "").replace("\"", "");
					String p1 = params.get(1).toString().replace("'", "").replace("\"", "");
					String p2 = params.get(2).toString().replace("'", "").replace("\"", "");

					System.out.println(p0);
					System.out.println(p1);
					System.out.println(p2);

					v = new CellValue(p0.replace(p1, p2));
				}
				else
				{
					throw new IllegalStateException(exp.toString());
				}
			}

			expect(RPARAN);
		}
		else if (exp.getType() == NUMBER)
		{
			v = new CellValue(exp);
		}
		else if (exp.getType() == TUPLE)
		{
			Tuple pos = (Tuple)exp.getStatement();
			v = mDataProvider.getComputeValueAt(null, pos.getRow(), pos.getCol(), mTimeCode);
		}
		else if (exp.getType() == RANGE)
		{
			v = new CellValue((Range)exp.getStatement());
		}
		else if (exp.getType() == LITERAL)
		{
			v = new CellValue(exp);
		}
		else if (exp.getType() == NAME)
		{
			String ref = exp.getStatement().toString();

			if (mOffset < mExpressions.size())
			{
				if (mExpressions.get(mOffset).getType() == NAME)
				{
					ref += "," + mExpressions.get(mOffset).toString();
					mOffset++;
				}
			}

			if (mExpressions.get(mOffset).getType() == REF)
			{
				mOffset++;
			}
			else
			{
				throw new IllegalStateException("" + exp.getType());
			}

			if (mExpressions.get(mOffset).getType() == TUPLE)
			{
				Tuple pos = (Tuple)mExpressions.get(mOffset).getStatement();
				v = mDataProvider.getComputeValueAt(ref, pos.getRow(), pos.getCol(), mTimeCode);
				mOffset++;
			}
			else if (mExpressions.get(mOffset).getType() == RANGE)
			{
//				System.out.println("#"+ref+"#");
				v = new CellValue(new Range(ref, ((Range)mExpressions.get(mOffset).getStatement()).toString()));
				mOffset++;
			}
			else
			{
				throw new IllegalStateException("" + exp.getType());
			}
		}
		else
		{
			throw new IllegalStateException("" + exp.getType());
		}

		if (mOffset < mExpressions.size())
		{
			exp = mExpressions.get(mOffset);

			if ("^".equals(exp.getStatement())) {mOffset++; v = new CellValue(Math.pow(v.doubleValue(), parseExpression().doubleValue()));}
		}

		return v;
	}


	private void expect(ExpressionStatementType aType)
	{
		ExpressionStatementType found = mExpressions.get(mOffset).getType();
		if (found != aType)
		{
			throw new IllegalStateException(found + " != " + aType);
		}
		mOffset++;
	}


	public void validate()
	{
		if (mOffset != mExpressions.size())
		{
			throw new IllegalArgumentException(mOffset + " != " + mExpressions.size());
		}
	}
}
