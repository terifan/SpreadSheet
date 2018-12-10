package org.terifan.spreadsheet;

import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.terifan.spreadsheet.ui.TableFactory;


public class SpreadSheet
{
	private Map mMap;
	private HashMap<Integer,String> mColumnLabels;


	public SpreadSheet()
	{
		mMap = new Map();
		mColumnLabels = new HashMap<>();
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
		mMap.put(aCol, aRow, convertValue(aValue));
	}


	public int findRow(int aCol, Object aValue)
	{
		CellValue value = convertValue(aValue);

		int maxRow = mMap.getMaxRow();

		for (int row = 0; row <= maxRow; row++)
		{
			if (value.equals(mMap.get(aCol, row)))
			{
				return row;
			}
		}

		return -1;
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


	public JScrollPane createJTable()
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
		return mColumnLabels.getOrDefault(aColumn, Character.toString((char)(65 + aColumn)));
	}


	public SpreadSheet setColumnLabel(int aColumn, String aLabel)
	{
		mColumnLabels.put(aColumn, aLabel);
		return this;
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


	private CellValue convertValue(Object aValue)
	{
		if (aValue == null)
		{
			return new NullValue();
		}
		if (aValue instanceof Double || aValue instanceof Float)
		{
			return new Number(((java.lang.Number)aValue).doubleValue());
		}
		if (aValue instanceof Integer || aValue instanceof Long || aValue instanceof Short || aValue instanceof Byte)
		{
			return new Number(((java.lang.Number)aValue).longValue());
		}
		if (aValue instanceof String)
		{
			return new Text((String)aValue);
		}
		if (aValue instanceof Boolean)
		{
			return new BooleanValue((Boolean)aValue);
		}
		if (aValue instanceof CellValue)
		{
			return (CellValue)aValue;
		}
		if (aValue instanceof Enum)
		{
			return new EnumValue((Enum)aValue);
		}

//		throw new IllegalArgumentException("Unsupported: " + aValue.getClass());
		System.out.println("Unsupported cell value, displaying as text: " + aValue.getClass());

		return new Text(aValue.toString());
	}
}
