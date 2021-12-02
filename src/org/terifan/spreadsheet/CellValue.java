package org.terifan.spreadsheet;

import static org.terifan.spreadsheet.Convert.isNumber;
import static org.terifan.spreadsheet.Convert.toDouble;


public class CellValue
{
	private Object mValue;
	private boolean mError;


	private CellValue()
	{
	}


	public CellValue(boolean aError)
	{
		mError = aError;
	}


	public Object getValue()
	{
		return mValue;
	}


	public CellValue(Object aValue)
	{
		if (aValue instanceof String)
		{
			if (isNumber(aValue))
			{
				aValue = toDouble(aValue);
			}
		}
		mValue = aValue;
	}


	public CellValue add(CellValue aValue)
	{
		if (mError || aValue.mError)
		{
			return new CellValue(true);
		}
		if (isNumber(mValue) && isNumber(aValue.mValue))
		{
			return new CellValue(toDouble(mValue) + toDouble(aValue.mValue));
		}
		throw new IllegalCellValueException("aValue=" + aValue + " (" + aValue.getClass() + ")");
	}


	public CellValue sub(CellValue aValue)
	{
		if (mError || aValue.mError)
		{
			return new CellValue(true);
		}
		if (isNumber(mValue) && isNumber(aValue.mValue))
		{
			return new CellValue(toDouble(mValue) - toDouble(aValue.mValue));
		}
		throw new IllegalCellValueException("aValue=" + aValue + " (" + aValue.getClass() + ")");
	}


	public CellValue mul(CellValue aValue)
	{
		if (mError || aValue.mError)
		{
			return new CellValue(true);
		}
		if (mValue == null || aValue.mValue == null)
		{
			return new CellValue(0L);
		}
		if (isNumber(mValue) && isNumber(aValue.mValue))
		{
			return new CellValue(toDouble(mValue) * toDouble(aValue.mValue));
		}
		throw new IllegalCellValueException("aValue=" + aValue + " (" + aValue.getClass() + ")");
	}


	public CellValue div(CellValue aValue)
	{
		if (mError || aValue.mError)
		{
			return new CellValue(true);
		}
		if (mValue == null)
		{
			return new CellValue(0L);
		}
		if (isNumber(mValue) && isNumber(aValue.mValue))
		{
			double div = aValue.doubleValue();
			if (div == 0)
			{
				return new CellValue("Division by zero");
			}
			return new CellValue(toDouble(mValue) / div);
		}
		throw new IllegalCellValueException("aValue=" + aValue + " (" + aValue.getClass() + ")");
	}


	public double doubleValue()
	{
		return toDouble(mValue);
	}


	private String toString(Object aValue)
	{
		return aValue.toString();
	}


	@Override
	public String toString()
	{
		return mError ? "ERROR" : mValue == null ? "" : mValue.toString();
	}


	boolean isEmpty()
	{
		return mValue == null || mValue.toString().isEmpty();
	}


	public boolean isError()
	{
		return mError;
	}


	void setError(boolean aError)
	{
		mError = aError;
	}
}
