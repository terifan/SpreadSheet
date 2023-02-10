package org.terifan.spreadsheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.terifan.spreadsheet.ui.SpreadSheetTable;
import org.terifan.spreadsheet.ui.TableFactory;
import org.terifan.spreadsheet.ui.WorkBook;


public class SpreadSheet implements Iterable<Tuple>, DataProvider, Serializable
{
	private final static long serialVersionUID = 1L;

	private ArrayList<SpreadSheetTableColumn> mColumns;
	private HashMap<Integer, String> mRowHeaders;
	private ValueMap<Cell> mMap;
	private int mRowHeaderSize;
	private int mRowNumberSize;
	private String mRowHeaderTitle;

	private transient DataLookup mDataLookup;
	private transient SpreadSheetFunctions mFunctions;


	public SpreadSheet()
	{
		mMap = new ValueMap();
		mColumns = new ArrayList<>();
		mRowHeaders = new HashMap<>();

		mRowNumberSize = 50;
		mRowHeaderSize = 75;
	}


	public SpreadSheet ensureCapacity(int aRows, int aColumns)
	{
		mMap.put(aRows, aColumns, new Cell(this));
		return this;
	}


	public Cell getCell(Tuple aPos)
	{
		return getCell(aPos.getRow(), aPos.getCol());
	}


	public Cell getCell(int aRow, int aCol)
	{
		return mMap.get(aRow, aCol);
	}


	public Cell createCell(Tuple aPos)
	{
		return createCell(aPos.getRow(), aPos.getCol());
	}


	public Cell createCell(int aRow, int aCol)
	{
		Cell cell = mMap.get(aRow, aCol);
		if (cell == null)
		{
			cell = new Cell(this);
			mMap.put(aRow, aCol, cell);
		}
		return cell;
	}


	public String getValueAt(Tuple aPos)
	{
		return getValueAt(aPos.getRow(), aPos.getCol());
	}


	public String getValueAt(int aRow, int aCol)
	{
		Cell cell = mMap.get(aRow, aCol);
		if (cell == null)
		{
			return null;
		}
		return cell.getValue();
	}


	public SpreadSheet setValueAt(Tuple aPos, Object aValue)
	{
		setValueAt(aPos.getRow(), aPos.getCol(), aValue);
		return this;
	}


	public SpreadSheet setValueAt(int aRow, int aCol, Object aValue)
	{
		createCell(aRow, aCol).setValue(aValue == null ? null : aValue.toString());
		return this;
	}


	public SpreadSheet parseValuesTo(int aRow, int aCol, String aValue)
	{
		for (String rowValues : aValue.split("\n"))
		{
			int col = aCol;
			for (String colValue : rowValues.split("\t"))
			{
				Cell cell = createCell(aRow, col++);
				if (colValue.equals("null"))
				{
					cell.setValue(null);
				}
				else
				{
					cell.setValue(colValue);
				}
			}
			aRow++;
		}
		return this;
	}


	public CellStyle getStyleAt(Tuple aPos)
	{
		return getStyleAt(aPos.getRow(), aPos.getCol());
	}


	public CellStyle getStyleAt(int aRow, int aCol)
	{
		Cell cell = getCell(aRow, aCol);
		if (cell == null)
		{
			return null;
		}
		return cell.getStyle();
	}


	public SpreadSheet setStyleAt(Tuple aPos, CellStyle aStyle)
	{
		return setStyleAt(aPos.getRow(), aPos.getCol(), aStyle);
	}


	public SpreadSheet setStyleAt(int aRow, int aCol, CellStyle aStyle)
	{
		createCell(aRow, aCol).setStyle(aStyle);
		return this;
	}


	public void trim()
	{
		mMap.trim();
	}


	public int getRowCount()
	{
		return mMap.getRowCount();
	}


	public int getColumnCount()
	{
		return mMap.getColumnCount();
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
				Cell cell = getCell(row, col);
				Object v = cell == null ? null : cell.getDisplayValue(timeCode);
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


	public SpreadSheetTable createUI(WorkBook aWorkBook)
	{
		SpreadSheetTable table = new TableFactory().createTable(aWorkBook, this, mColumns, mRowHeaderTitle, mRowNumberSize, mRowHeaders.isEmpty() ? 0 : mRowHeaderSize, mRowHeaders, mMap);

		return table;
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
		return getRowCount();
	}


	public int nextColumn()
	{
		return getColumnCount();
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


	public String getRowHeaders(int aRowIndex)
	{
		return mRowHeaders.get(aRowIndex);
	}


	public void setRowHeader(int aRowIndex, String aRowHeader)
	{
		mRowHeaders.put(aRowIndex, aRowHeader);
	}


	@Override
	public Iterator<Tuple> iterator()
	{
		ArrayList<Tuple> list = new ArrayList<>();

		for (int row = 0; row < mMap.getRowCount(); row++)
		{
			MapRow<Cell> r = mMap.get(row);
			if (r != null)
			{
				for (int cell = 0; cell < r.size(); cell++)
				{
					if (r.get(cell) != null)
					{
						list.add(new Tuple(row, cell));
					}
				}
			}
		}

		return list.iterator();
	}


	@Override
	public String toString()
	{
		return mMap.toString();
	}


	public void setColumnWidths(int aStartColumn, int... aWidths)
	{
		for (int i = 0; i < aWidths.length; i++)
		{
			SpreadSheetTableColumn column = getColumn(aStartColumn + i);
			column.setMinWidth(0);
			column.setMaxWidth(32000);
			column.setPreferredWidth(aWidths[i]);
		}
	}


	public void clear()
	{
		mMap.clear();
	}


	public SpreadSheet setDataLookup(DataLookup aDataLookup)
	{
		mDataLookup = aDataLookup;
		return this;
	}


	@Override
	public CellValue getComputeValueAt(String aReference, int aRow, int aColumn, long aTimeCode)
	{
		if (aReference != null)
		{
			return mDataLookup.getDataProvider(aReference).getComputeValueAt(null, aRow, aColumn, aTimeCode);
		}

		Cell cell = getCell(aRow, aColumn);

		if (cell == null)
		{
//			System.out.println(aRow+" "+aColumn+" #"+"null");
			return null;
		}

		CellValue v = cell.computeValue(aTimeCode);
		return v;
	}


	@Override
	public SpreadSheetFunctions getFunctions()
	{
		return mFunctions;
	}


	public void setFunctions(SpreadSheetFunctions aFunctions)
	{
		mFunctions = aFunctions;
	}


	public void validateData()
	{
		long time = System.currentTimeMillis();

		for (int row = 0; row < mMap.getRowCount(); row++)
		{
			MapRow<Cell> r = mMap.get(row);
			if (r != null)
			{
				for (int c = 0; c < r.size(); c++)
				{
					Cell cell = r.get(c);

					if (cell != null)
					{
						cell.computeValue(time);
					}
				}
			}
		}
	}
}
