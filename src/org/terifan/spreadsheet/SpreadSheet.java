package org.terifan.spreadsheet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JScrollPane;
import org.terifan.spreadsheet.ui.TableFactory;


public class SpreadSheet
{
	private Map mMap;
	private ArrayList<SpreadSheetTableColumn> mColumns;
	private int mRowHeaderSize;
	private int mRowNumberSize;
	private int mStaticColumnCount;
	private String mRowHeaderTitle;
	private HashMap<Integer,String> mRowHeaders;


	public SpreadSheet()
	{
		mMap = new Map();
		mColumns = new ArrayList<>();
		mRowHeaders = new HashMap<>();

		mRowNumberSize = 50;
		mRowHeaderSize = 75;
		mStaticColumnCount = 0;
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
		int maxCol = lastColumn();
		int maxRow = lastRow();
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


	public int lastRow()
	{
		return mMap.getMaxRow();
	}


	public int getColumnCount()
	{
		return mMap.getColumnCount();
	}


	public int lastColumn()
	{
		return mMap.getMaxColumn();
	}


	public void print()
	{
		long timeCode = System.nanoTime();
		int maxColumn = nextColumn();
		int maxRow = nextRow();

		System.out.print("");
		System.out.print("\t");

		for (int col = 0; col < maxColumn; col++)
		{
			System.out.print(getColumn(col).getHeaderValue());
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


	public JScrollPane createUI()
	{
		long timeCode = System.nanoTime();
		int maxRow = nextRow();
		int maxColumn = 1 + lastColumn();

		CellValue[][] data = new CellValue[maxRow][maxColumn];
		for (int row = 0; row < maxRow; row++)
		{
			for (int col = 0; col < maxColumn; col++)
			{
				data[row][col] = getComputed(col, row, timeCode);
			}
		}

		return new TableFactory().createTable(data, mColumns, mStaticColumnCount, mRowHeaderTitle, mRowNumberSize, mRowHeaderSize, mRowHeaders);
	}


	/**
	 * Returns the column with the index specified. This method create and add new columns when not found.
	 */
	public synchronized SpreadSheetTableColumn getColumn(int aColumn)
	{
		while (mColumns.size() <= aColumn)
		{
			mColumns.add(new SpreadSheetTableColumn(mColumns.size()));
		}

		return mColumns.get(aColumn);
	}


	public void setColumn(SpreadSheetTableColumn aColumn)
	{
		while (mColumns.size() <= aColumn.getModelIndex())
		{
			mColumns.add(new SpreadSheetTableColumn(mColumns.size()));
		}
		mColumns.set(aColumn.getModelIndex(), aColumn);
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
		if (aValue instanceof Date)
		{
			return new DateValue((Date)aValue);
		}

//		throw new IllegalArgumentException("Unsupported: " + aValue.getClass());
		System.out.println("Unsupported cell value, displaying as text: " + aValue.getClass());

		return new Text(aValue.toString());
	}


	/**
	 * Return the row number of the next free row.
	 * <p>
	 * Append three new rows:<br/>
	 * <code>
	 * ss.set(0, ss.nextRow(), "row 0, column a");<br/>
	 * ss.set(1, ss.lastRow(), "row 0, column b");<br/>
	 * ss.set(2, ss.lastRow(), "row 0, column c");<br/>
	 * ss.set(0, ss.nextRow(), "row 1, column a");<br/>
	 * ss.set(1, ss.lastRow(), "row 1, column b");<br/>
	 * ss.set(2, ss.lastRow(), "row 1, column c");<br/>
	 * </code>
	 * </p>
	 *
	 * @return
	 *
	 */
	public int nextRow()
	{
		return lastRow() + 1;
	}


	public int nextColumn()
	{
		return lastColumn() + 1;
	}


	public int getRowNumberSize()
	{
		return mRowNumberSize;
	}


	public SpreadSheet setRowNumberSize(int aRowNumberSize)
	{
		mRowNumberSize = aRowNumberSize;
		return this;
	}


	public int getRowHeaderSize()
	{
		return mRowHeaderSize;
	}


	public SpreadSheet setRowHeaderSize(int aRowHeaderSize)
	{
		mRowHeaderSize = aRowHeaderSize;
		return this;
	}


	public String getRowHeaderTitle()
	{
		return mRowHeaderTitle;
	}


	public SpreadSheet setRowHeaderTitle(String aRowHeaderTitle)
	{
		mRowHeaderTitle = aRowHeaderTitle;
		return this;
	}


	public int getStaticColumnCount()
	{
		return mStaticColumnCount;
	}


	public SpreadSheet setStaticColumnCount(int aStaticColumnCount)
	{
		mStaticColumnCount = aStaticColumnCount;
		return this;
	}


	public String getRowHeaders(int aRowIndex)
	{
		return mRowHeaders.get(aRowIndex);
	}


	public void setRowHeader(int aRowIndex, String aRowHeader)
	{
		mRowHeaders.put(aRowIndex, aRowHeader);
	}
}
