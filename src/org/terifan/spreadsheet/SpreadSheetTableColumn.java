package org.terifan.spreadsheet;

import javax.swing.table.TableColumn;


public class SpreadSheetTableColumn extends TableColumn
{
	private static final long serialVersionUID = 1L;


	public SpreadSheetTableColumn(int aModelIndex)
	{
		super(aModelIndex);
	}


	public SpreadSheetTableColumn(int aModelIndex, Object aHeaderValue)
	{
		super(aModelIndex);
		setHeaderValue(aHeaderValue);
	}


	public SpreadSheetTableColumn(int aModelIndex, Object aHeaderValue, int aPreferredWidth)
	{
		super(aModelIndex);
		setHeaderValue(aHeaderValue);
		setPreferredWidth(aPreferredWidth);
	}
}
