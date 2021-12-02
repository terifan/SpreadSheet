package org.terifan.spreadsheet.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.Tuple;


public class SpreadSheetTable extends JPanel
{
	private static final long serialVersionUID = 1L;

	private SpreadSheet mSpreadSheet;
	private JScrollPane mScrollPane;
	private FixedTable mTable;
	private WorkBook mWorkBook;


	SpreadSheetTable(FixedTable aTable, SpreadSheet aSpreadSheet)
	{
		super(new BorderLayout());

		mTable = aTable;
		mSpreadSheet = aSpreadSheet;
		mScrollPane = new JScrollPane(aTable);

		super.add(mScrollPane, BorderLayout.CENTER);
	}


	void setWorkBook(WorkBook aWorkBook)
	{
		mWorkBook = aWorkBook;
	}


	public FixedTable getTable()
	{
		return mTable;
	}


	public JScrollPane getScrollPane()
	{
		return mScrollPane;
	}


	public SpreadSheet getSpreadSheet()
	{
		return mSpreadSheet;
	}


	public ArrayList<Tuple> getSelections()
	{
		return mTable.getSelections();
	}


	public void setValueAt(Tuple aPos, Object aValue)
	{
		mTable.setValueAt(aPos, aValue == null ? "" : aValue.toString());
	}


	public void clearSelection()
	{
		mTable.clearSelection();
	}


	public void changeSelection(int aRow, int aCol, boolean aToggle, boolean aExtend)
	{
		mTable.changeSelection(aRow, aCol, aToggle, aExtend);
	}


	void fireCellSelected()
	{
		mWorkBook.fireCellSelected();
	}
}
