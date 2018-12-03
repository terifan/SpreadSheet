package org.terifan.spreadsheet;

import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JComponent;
import org.terifan.spreadsheet.ui.TableFactory;


public class SpreadSheet
{
	private HashMap<Tuple, CellValue> mMap;


	public SpreadSheet()
	{
		mMap = new HashMap<>();
	}


	public CellValue get(int aCol, int aRow)
	{
		return mMap.get(new Tuple(aCol, aRow));
	}


	public CellValue get(Tuple aTuple)
	{
		return mMap.get(aTuple);
	}


	public void set(int aCol, int aRow, Object aValue)
	{
		if (aValue instanceof Double || aValue instanceof Float)
		{
			mMap.put(new Tuple(aCol, aRow), new Number(((java.lang.Number)aValue).doubleValue()));
		}
		else if (aValue instanceof Integer || aValue instanceof Long || aValue instanceof Short || aValue instanceof Byte)
		{
			mMap.put(new Tuple(aCol, aRow), new Number(((java.lang.Number)aValue).longValue()));
		}
		else if (aValue instanceof Formula)
		{
			mMap.put(new Tuple(aCol, aRow), (Formula)aValue);
		}
		else if (aValue instanceof Number)
		{
			mMap.put(new Tuple(aCol, aRow), (Number)aValue);
		}
		else
		{
			throw new IllegalArgumentException("Unsupported: " + aValue);
		}
	}


	public void removeEmptyRows()
	{
		int maxRow = getMaxRow();
		for (int i = 0; i < maxRow; i++)
		{

		}
	}


	public int getRowCount()
	{
		HashSet<Integer> rows = new HashSet<>();
		for (Tuple tuple : mMap.keySet())
		{
			rows.add(tuple.getRow());
		}
		return rows.size();
	}


	public int getMaxRow()
	{
		int rows = 0;
		for (Tuple tuple : mMap.keySet())
		{
			rows = Math.max(tuple.getRow(), rows);
		}
		return rows;
	}


	public int getColumnCount()
	{
		HashSet<Integer> cols = new HashSet<>();
		for (Tuple tuple : mMap.keySet())
		{
			cols.add(tuple.getCol());
		}
		return cols.size();
	}


	public int getMaxColumn()
	{
		int cols = 0;
		for (Tuple tuple : mMap.keySet())
		{
			cols = Math.max(tuple.getCol(), cols);
		}
		return cols;
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
		return Character.toString('A' + aColumn);
	}


	CellValue getComputed(int aCol, int aRow, long aTimeCode)
	{
		CellValue v = get(aCol, aRow);

		if (v instanceof Formula)
		{
			v = ((Formula)v).compute(this, aTimeCode);
		}

		return v;
	}


	CellValue getComputed(Tuple aTuple, long aTimeCode)
	{
		CellValue v = get(aTuple);

		if (v instanceof Formula)
		{
			v = ((Formula)v).compute(this, aTimeCode);
		}

		return v;
	}
}
