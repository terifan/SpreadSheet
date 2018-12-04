package org.terifan.spreadsheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JComponent;
import org.terifan.spreadsheet.ui.TableFactory;


public class SpreadSheet
{
	private Map mMap;


	public SpreadSheet()
	{
		mMap = new Map();
	}


	public CellValue get(int aCol, int aRow)
	{
		return mMap.get(aCol, aRow);
	}


	public CellValue get(Tuple aTuple)
	{
		return mMap.get(aTuple);
	}


	public void set(int aCol, int aRow, Object aValue)
	{
		if (aValue instanceof Double || aValue instanceof Float)
		{
			mMap.put(aCol, aRow, new Number(((java.lang.Number)aValue).doubleValue()));
		}
		else if (aValue instanceof Integer || aValue instanceof Long || aValue instanceof Short || aValue instanceof Byte)
		{
			mMap.put(aCol, aRow, new Number(((java.lang.Number)aValue).longValue()));
		}
		else if (aValue instanceof Formula)
		{
			mMap.put(aCol, aRow, (Formula)aValue);
		}
		else if (aValue instanceof Number)
		{
			mMap.put(aCol, aRow, (Number)aValue);
		}
		else
		{
			throw new IllegalArgumentException("Unsupported: " + aValue);
		}
	}


	public void removeEmptyRows()
	{
		int maxCol = getMaxColumn();
		int maxRow = getMaxRow();
		for (int y = 0; y < maxRow; y++)
		{
			boolean empty = true;
			for (int x = 0; x < maxCol; x++)
			{
				if (get(x, y) != null)
				{
					empty = false;
					break;
				}
			}
			if (empty)
			{
				for (int yi = y + 1; yi < maxRow; yi++)
				{

				}
				y--;
				maxRow--;
			}
		}
	}


	public int getRowCount()
	{
		return mMap.getRowCount();
	}


	public int getMaxRow()
	{
		return mMap.getMaxRow();
	}


	public int getColumnCount()
	{
		return mMap.getColumnCount();
	}


	public int getMaxColumn()
	{
		return mMap.getMaxColumn();
	}


	public void print()
	{
		long timeCode = System.nanoTime();
		int maxColumn = 1 + getMaxColumn();
		int maxRow = 1 + getMaxRow();

		System.out.print("");
		System.out.print("\t");

		for (int col = 0; col < maxColumn; col++)
		{
			System.out.print(getColumnLabel(col));
			System.out.print("\t");
		}
		System.out.println();

		for (int row = 0; row < maxRow; row++)
		{
			System.out.print(row);
			System.out.print("\t");
			for (int col = 0; col < maxColumn; col++)
			{
				CellValue v = getComputed(col, row, timeCode);
				if (v == null)
				{
					System.out.print("");
				}
				else
				{
					System.out.print(v);
				}
				System.out.print("\t");
			}
			System.out.println();
		}
	}


	public JComponent createJTable()
	{
		long timeCode = System.nanoTime();
		int maxRow = 1 + getMaxRow();
		int maxColumn = 1 + getMaxColumn();

		CellValue[][] data = new CellValue[maxRow][maxColumn];
		for (int row = 0; row < maxRow; row++)
		{
			for (int col = 0; col < maxColumn; col++)
			{
				data[row][col] = getComputed(col, row, timeCode);
			}
		}

		Object[] columns = new Object[maxColumn];
		for (int col = 0; col < maxColumn; col++)
		{
			columns[col] = getColumnLabel(col);
		}

		return new TableFactory().createTable(data, columns);
	}


	public String getColumnLabel(int aColumn)
	{
		return Character.toString((char)(65 + aColumn));
	}


	public CellValue getComputed(int aCol, int aRow, long aTimeCode)
	{
		CellValue v = get(aCol, aRow);

		if (v instanceof Formula)
		{
			v = ((Formula)v).compute(this, aTimeCode);
		}
		else if (v != null)
		{
			v = v.clone();
		}

		return v;
	}


	public Number getComputedNumber(Tuple aTuple, long aTimeCode)
	{
		return (Number)getComputed(aTuple, aTimeCode);
	}


	public CellValue getComputed(Tuple aTuple, long aTimeCode)
	{
		CellValue v = get(aTuple);

		if (v instanceof Formula)
		{
			v = ((Formula)v).compute(this, aTimeCode);
		}
		else if (v != null)
		{
			v = v.clone();
		}

		return v;
	}
}
