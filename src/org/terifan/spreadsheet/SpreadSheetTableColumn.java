package org.terifan.spreadsheet;

import javax.swing.table.TableColumn;


public class SpreadSheetTableColumn extends TableColumn
{
	private static final long serialVersionUID = 1L;


	public SpreadSheetTableColumn(int aIndex)
	{
		super(aIndex);
		setHeaderValue(Convert.formatColumnIndex(aIndex));
	}


	public SpreadSheetTableColumn(int aIndex, Object aHeaderValue)
	{
		super(aIndex);
		setHeaderValue(aHeaderValue);
	}


	public SpreadSheetTableColumn(int aIndex, Object aHeaderValue, int aPreferredWidth)
	{
		super(aIndex);
		setHeaderValue(aHeaderValue);
		setPreferredWidth(aPreferredWidth);
	}


	@Override
	public String toString()
	{
		return getHeaderValue().toString();
	}
}
