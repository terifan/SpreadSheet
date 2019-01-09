package org.terifan.spreadsheet;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.terifan.spreadsheet.ui.ColumnHeaderRenderer;
import org.terifan.spreadsheet.ui.FixedTable;
import org.terifan.spreadsheet.ui.RowNumberTable;
import org.terifan.spreadsheet.ui.TableCellRenderer;


public class SpreadSheet implements TableModel
{
	private Map<CellValue> mValues;
	private Map<CellStyle> mStyles;
	private HashMap<Integer, String> mRowHeaders;
	private int mRowHeaderSize;
	private int mRowNumberSize;
	private String mRowHeaderTitle;
	private JScrollPane mScrollPane;
	private FixedTable mTable;
	private ArrayList<TableModelListener> mListeners;
	private ArrayList<SpreadSheetTableColumn> mColumns;


	public SpreadSheet()
	{
		mListeners = new ArrayList<>();
		mColumns = new ArrayList<>();
		mValues = new Map();
		mStyles = new Map();
		mRowHeaders = new HashMap<>();

		mRowNumberSize = 50;
		mRowHeaderSize = 75;

//		long timeCode = System.nanoTime();
//		int maxRow = nextRow();
//		int maxColumn = 1 + lastColumn();
//
//		CellValue[][] data = new CellValue[maxRow][maxColumn];
//		for (int row = 0; row < maxRow; row++)
//		{
//			for (int col = 0; col < maxColumn; col++)
//			{
//				data[row][col] = getComputed(col, row, timeCode);
//			}
//		}

		createTable();
	}


	public CellValue get(int aCol, int aRow)
	{
		return mValues.get(aCol, aRow);
	}


	public <T extends CellValue> T get(int aCol, int aRow, Class<T> aCellValue, T aDefaultValue)
	{
		T v = (T)mValues.get(aCol, aRow);
		return v == null ? aDefaultValue : v;
	}


	public void set(int aCol, int aRow, Object aValue)
	{
		mValues.put(aCol, aRow, convertValue(aValue));
	}


	public CellStyle getStyle(int aCol, int aRow)
	{
		return mStyles.get(aCol, aRow);
	}


	public void setStyle(int aCol, int aRow, CellStyle aValue)
	{
		mStyles.put(aCol, aRow, aValue);
	}


	public int findRow(int aCol, Object aValue)
	{
		CellValue value = convertValue(aValue);

		int maxRow = mValues.getRowCount();

		for (int row = 0; row < maxRow; row++)
		{
			if (value.equals(mValues.get(aCol, row)))
			{
				return row;
			}
		}

		return -1;
	}


	public void removeEmptyRows()
	{
		int maxCol = getColumnCount();
		int maxRow = getRowCount();
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


	public void print()
	{
		long timeCode = System.nanoTime();
		int maxColumn = getColumnCount();
		int maxRow = getRowCount();

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
		return mScrollPane;
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


	public NumberValue getComputedNumber(Tuple aTuple, long aTimeCode)
	{
		return (NumberValue)getComputed(aTuple, aTimeCode);
	}


	public CellValue getComputed(Tuple aTuple, long aTimeCode)
	{
		CellValue v = mValues.get(aTuple);

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
			return new NumberValue(((java.lang.Number)aValue).doubleValue());
		}
		if (aValue instanceof Integer || aValue instanceof Long || aValue instanceof Short || aValue instanceof Byte)
		{
			return new NumberValue(((java.lang.Number)aValue).longValue());
		}
		if (aValue instanceof String)
		{
			return new TextValue((String)aValue);
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

		System.out.println("Unsupported cell value, displaying as text: " + aValue.getClass());

		return new TextValue(aValue.toString());
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
//	public int nextRow()
//	{
//		return getRowCount();
//	}
//
//
//	public int nextColumn()
//	{
//		return getColumnCount();
//	}


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


	private JScrollPane createTable()
	{
		mTable = new FixedTable(this);
		mTable.setBorder(null);
		mTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		mTable.setColumnSelectionAllowed(false);
		mTable.setRowSelectionAllowed(true);
		mTable.setCellSelectionEnabled(true);
		mTable.setGridColor(new Color(0xDADCDD));
		mTable.setRowHeight(19);
		mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		mTable.setShowGrid(false);
		mTable.setIntercellSpacing(new Dimension(0, 0));
		mTable.setDefaultRenderer(Object.class, new TableCellRenderer(mTable, mStyles));
		mTable.setSelectionForeground(null);
		mTable.setSelectionBackground(null);

		ListSelectionModel selectionModel = mTable.getSelectionModel();

		TableColumnModel columnModel = mTable.getColumnModel();
		columnModel.setColumnSelectionAllowed(true);

		JTableHeader tableHeader = mTable.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setDefaultRenderer(new ColumnHeaderRenderer("", mRowNumberSize, 0, mTable));

		RowNumberTable rowTable = new RowNumberTable(mTable, mRowNumberSize, mRowHeaderSize, mRowHeaders, mStyles);

		ColumnHeaderRenderer cornerLeft = new ColumnHeaderRenderer(mRowHeaderTitle, mRowNumberSize, mRowHeaderSize, mTable);
		cornerLeft.setDrawLeftBorder(true);

		mScrollPane = new JScrollPane(mTable);
		mScrollPane.setRowHeaderView(rowTable);
		mScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerLeft);
		mScrollPane.setBorder(null);

		selectionModel.addListSelectionListener(aEvent -> mScrollPane.repaint());

		columnModel.addColumnModelListener(new TableColumnModelListener()
		{
			@Override
			public void columnAdded(TableColumnModelEvent aE)
			{
			}


			@Override
			public void columnRemoved(TableColumnModelEvent aE)
			{
			}


			@Override
			public void columnMoved(TableColumnModelEvent aE)
			{
			}


			@Override
			public void columnMarginChanged(ChangeEvent aE)
			{
			}


			@Override
			public void columnSelectionChanged(ListSelectionEvent aE)
			{
				mScrollPane.repaint();
			}
		});

		return mScrollPane;
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

		fireListeners(new TableModelEvent(this));
	}


	@Override
	public int getRowCount()
	{
		return mValues.getRowCount();
	}


	@Override
	public int getColumnCount()
	{
		return 20;//mValues.getColumnCount();
	}


	@Override
	public String getColumnName(int aColumnIndex)
	{
		return getColumn(aColumnIndex).getHeaderValue().toString();
	}


	@Override
	public Class<?> getColumnClass(int aColumnIndex)
	{
		return getColumn(aColumnIndex).getColumnClass();
	}


	@Override
	public boolean isCellEditable(int aRowIndex, int aColumnIndex)
	{
		return true;
	}


	@Override
	public Object getValueAt(int aRowIndex, int aColumnIndex)
	{
		return getComputed(aColumnIndex, aRowIndex, System.nanoTime());
	}


	@Override
	public void setValueAt(Object aValue, int aRowIndex, int aColumnIndex)
	{
		mValues.put(aColumnIndex, aRowIndex, (CellValue)aValue);

		fireListeners(new TableModelEvent(this, aRowIndex, aRowIndex, aColumnIndex));
	}


	@Override
	public void addTableModelListener(TableModelListener aListener)
	{
		mListeners.add(aListener);
	}


	@Override
	public void removeTableModelListener(TableModelListener aListener)
	{
		mListeners.remove(aListener);
	}


	private void fireListeners(TableModelEvent aEvent)
	{
		for (TableModelListener listener : mListeners)
		{
			listener.tableChanged(aEvent);
		}
	}
}
