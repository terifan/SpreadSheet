package org.terifan.spreadsheet.ui;

import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.INSERT;
import static javax.swing.event.TableModelEvent.UPDATE;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.terifan.spreadsheet.Cell;
import org.terifan.spreadsheet.CellStyle;
import org.terifan.spreadsheet.SpreadSheet;


public class TableModelWrapper implements TableModel
{
	private SpreadSheet mSpreadSheet;
	private ArrayList<TableModelListener> mListeners;
	private long mLastEditingTime;


	public TableModelWrapper(SpreadSheet aSpreadSheet)
	{
		mSpreadSheet = aSpreadSheet;
		mListeners = new ArrayList<>();
		mLastEditingTime = System.currentTimeMillis();
	}


	@Override
	public int getRowCount()
	{
		return mSpreadSheet.getRowCount();
	}


	@Override
	public int getColumnCount()
	{
		return mSpreadSheet.getColumnCount();
	}


	@Override
	public String getColumnName(int aColumnIndex)
	{
		return mSpreadSheet.getColumn(aColumnIndex).toString();
	}


	@Override
	public Class<?> getColumnClass(int aColumnIndex)
	{
		return String.class;
	}


	@Override
	public boolean isCellEditable(int aRowIndex, int aColumnIndex)
	{
		return mSpreadSheet.getStyleAt(aRowIndex, aColumnIndex) != CellStyle.LOCKED;
	}


	@Override
	public Object getValueAt(int aRowIndex, int aColumnIndex)
	{
		Cell cell = mSpreadSheet.getCell(aRowIndex, aColumnIndex);
		if (cell == null)
		{
			return "";
		}
		return cell.getDisplayValue(mLastEditingTime);
	}


	@Override
	public void setValueAt(Object aValue, int aRowIndex, int aColumnIndex)
	{
		mLastEditingTime = System.currentTimeMillis();

		Cell cell = mSpreadSheet.createCell(aRowIndex, aColumnIndex);
		String old = cell.getValue();
		cell.setValue(aValue == null ? null : aValue.toString());

		for (TableModelListener listener : mListeners)
		{
			listener.tableChanged(new TableModelEvent(this, aRowIndex, aRowIndex, aColumnIndex, old == null ? INSERT : UPDATE));
		}
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
}
