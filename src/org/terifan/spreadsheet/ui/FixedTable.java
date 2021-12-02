package org.terifan.spreadsheet.ui;

import org.terifan.spreadsheet.actions.CutAction;
import org.terifan.spreadsheet.actions.PasteAction;
import org.terifan.spreadsheet.actions.CopyAction;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.TableModel;
import org.terifan.spreadsheet.Map;
import org.terifan.spreadsheet.MapRow;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.SpreadSheetTableColumn;
import org.terifan.spreadsheet.Tuple;
import org.terifan.spreadsheet.actions.DeleteAction;


public class FixedTable extends JTable
{
	private static final long serialVersionUID = 1L;

	private WorkBook mWorkBook;
	private SpreadSheet mSpreadSheet;
	private Map<Boolean> mMap;
	private Map<Boolean> mTempMap;
	private Point mFirstExtendCell;
	private Boolean mNewState;
	private Tuple mAnchor;
	private String mAnchorText;


	public FixedTable(WorkBook aWorkBook, SpreadSheet aSpreadSheet, TableModel aTableModel)
	{
		super(aTableModel);

		mMap = new Map<>();
		mWorkBook = aWorkBook;
		mSpreadSheet = aSpreadSheet;

		super.addMouseListener(new TableMouseListener(this));

		ActionMap actionMap = super.getActionMap();
		actionMap.put("paste", new PasteAction(this));
		actionMap.put("cut", new CutAction(this));
		actionMap.put("copy", new CopyAction(this));
		actionMap.put("delete", new DeleteAction(this));

		InputMap inputMap = super.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(KeyStroke.getKeyStroke('v', InputEvent.CTRL_DOWN_MASK), "paste");
		inputMap.put(KeyStroke.getKeyStroke('c', InputEvent.CTRL_DOWN_MASK), "copy");
		inputMap.put(KeyStroke.getKeyStroke('x', InputEvent.CTRL_DOWN_MASK), "cut");
		inputMap.put(KeyStroke.getKeyStroke("DELETE"), "delete");
	}


	public void selectCell(int aRow, int aColumn)
	{
		changeSelection(aRow, aColumn, false, false);
	}


	public SpreadSheet getSpreadSheet()
	{
		return mSpreadSheet;
	}


	@Override
	public boolean isRowSelected(int aRow)
	{
		MapRow<Boolean> row = mMap.get(aRow);
		if (row != null)
		{
			for (Boolean b : row)
			{
				if (Boolean.TRUE.equals(b))
				{
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public boolean isColumnSelected(int aColumn)
	{
		for (int i = 0; i < mMap.getRowCount(); i++)
		{
			MapRow<Boolean> row = mMap.get(i);
			if (row != null && row.size() > aColumn && Boolean.TRUE.equals(row.get(aColumn)))
			{
				return true;
			}
		}
		return false;
	}


	@Override
	public void changeSelection(int aRow, int aCol, boolean aToggle, boolean aExtend)
	{
//		System.out.println(aRow+" "+aCol+" "+aToggle+" "+aExtend);

		Map<Boolean> old = new Map<>();
		old.addAll(mMap);

		if (aToggle && !aExtend)
		{
			mFirstExtendCell = new Point(aRow, aCol);
			mNewState = !isCellSelected(aRow, aCol);
			mTempMap = new Map<>();
			mTempMap.addAll(mMap);
		}
		if (!aToggle && !aExtend)
		{
			mNewState = true;
		}

		if (aToggle && !aExtend && isCellSelected(aRow, aCol))
		{
			mMap.put(aRow, aCol, false);
		}
		else
		{
			if (!aToggle && !aExtend)
			{
				mMap.clear();
			}

			if (aExtend)
			{
				if (mTempMap == null)
				{
					mTempMap = new Map<>();
					mTempMap.addAll(mMap);
				}
				else
				{
					mMap.clear();
					mMap.addAll(mTempMap);
				}
			}
			else
			{
				mTempMap = null;
			}

			if (!aExtend)
			{
				mMap.put(aRow, aCol, mNewState);
				mFirstExtendCell = new Point(aRow, aCol);
			}
			else
			{
				for (int row = Math.min(mFirstExtendCell.x, aRow); row <= Math.max(mFirstExtendCell.x, aRow); row++)
				{
					for (int col = Math.min(mFirstExtendCell.y, aCol); col <= Math.max(mFirstExtendCell.y, aCol); col++)
					{
						mMap.put(row, col, mNewState);
					}
				}
			}
		}

		super.changeSelection(aRow, aCol, aToggle, aExtend);

		if (!old.equals(mMap))
		{
			repaint();
		}

		mWorkBook.fireCellSelected(mWorkBook, mSpreadSheet, aRow, aCol, aToggle, aExtend);
	}


	@Override
	public void selectAll()
	{
		mMap.clear();
		super.selectAll();
	}


	@Override
	public void clearSelection()
	{
		if (mMap != null)
		{
			mMap.clear();
		}
		super.clearSelection();
	}


	@Override
	public boolean isCellSelected(int aRow, int aCol)
	{
		return Boolean.TRUE.equals(mMap.get(aRow, aCol));
	}


	public ArrayList<Tuple> getSelections()
	{
		ArrayList<Tuple> list = new ArrayList<>();

		for (int row = 0; row < mMap.getRowCount(); row++)
		{
			MapRow<Boolean> r = mMap.get(row);
			if (r != null)
			{
				for (int col = 0; col < r.size(); col++)
				{
					if (Boolean.TRUE.equals(r.get(col)))
					{
						list.add(new Tuple(row, col));
					}
				}
			}
		}

		return list;
	}


	public void forEachSelection(Consumer<Tuple> aTuple)
	{
		for (int row = 0; row < mMap.getRowCount(); row++)
		{
			MapRow<Boolean> r = mMap.get(row);
			if (r != null)
			{
				for (int col = 0; col < r.size(); col++)
				{
					if (Boolean.TRUE.equals(r.get(col)))
					{
						aTuple.accept(new Tuple(row, col));
					}
				}
			}
		}
	}


	public void forEachValue(BiConsumer<Tuple, String> aTuple)
	{
		for (int row = 0, rows = mMap.getRowCount(); row < rows; row++)
		{
			for (int col = 0, cols = mMap.getColumnCount(); col < cols; col++)
			{
				Tuple pos = new Tuple(row, col);
				String value = mSpreadSheet.getValueAt(pos);
				if (value != null)
				{
					aTuple.accept(pos, value);
				}
			}
		}
	}


	@Override
	public void setValueAt(Object aValue, int aRow, int aColumn)
	{
		super.setValueAt(aValue == null ? "" : aValue.toString(), aRow, aColumn);
	}


	public void setValueAt(Tuple aPos, Object aValue)
	{
		mSpreadSheet.setValueAt(aValue, aPos);

		super.setValueAt(aValue == null ? "" : aValue.toString(), aPos.getRow(), aPos.getCol());
	}


	public void ensureCapacity(int aRows, int aColumns)
	{
		mSpreadSheet.ensureCapacity(aRows, aColumns);

		for (int i = getColumnCount(); i < aColumns; i++)
		{
			getColumnModel().addColumn(new SpreadSheetTableColumn(i));
		}
	}


	public void setAnchor(Tuple aAnchor, String aText)
	{
		mAnchor = aAnchor;
		mAnchorText = aText;
	}


	public Tuple getAnchor(String aText)
	{
		if (aText.equals(mAnchorText))
		{
			return mAnchor;
		}
		return null;
	}
}
