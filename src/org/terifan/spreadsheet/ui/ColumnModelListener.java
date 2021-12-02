package org.terifan.spreadsheet.ui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;


public class ColumnModelListener implements TableColumnModelListener
{
	private FixedTable mTable;


	public ColumnModelListener(FixedTable aTable)
	{
		mTable = aTable;
	}


	@Override
	public void columnAdded(TableColumnModelEvent aEvent)
	{
	}


	@Override
	public void columnRemoved(TableColumnModelEvent aEvent)
	{
	}


	@Override
	public void columnMoved(TableColumnModelEvent aEvent)
	{
	}


	@Override
	public void columnMarginChanged(ChangeEvent aEvent)
	{
	}


	@Override
	public void columnSelectionChanged(ListSelectionEvent aEvent)
	{
		mTable.getTableHeader().repaint();
		mTable.repaint();
	}
}
