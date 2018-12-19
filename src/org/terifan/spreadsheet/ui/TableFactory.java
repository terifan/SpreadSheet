package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import org.terifan.spreadsheet.CellStyle;
import org.terifan.spreadsheet.CellValue;
import org.terifan.spreadsheet.Map;
import org.terifan.spreadsheet.SpreadSheetTableColumn;


public class TableFactory
{
	public JScrollPane createTable(CellValue[][] aData, List<SpreadSheetTableColumn> aColumns, int aNumStaticColumns, String aRowHeaderTitle, int aRowNumberSize, int aRowHeaderSize, HashMap<Integer, String> aRowHeaders, Map<CellStyle> aStyles)
	{
		if (aRowHeaders.isEmpty())
		{
			aRowHeaderSize = 0;
		}

		addMissingColumns(aData, aColumns);

		SpreadSheetTableColumn[] dataColumns = new SpreadSheetTableColumn[aColumns.size() - aNumStaticColumns];
		SpreadSheetTableColumn[] staticColumns = new SpreadSheetTableColumn[aNumStaticColumns];

		for (int i = 0; i < aNumStaticColumns; i++)
		{
			staticColumns[i] = aColumns.get(i);
		}
		for (int i = aNumStaticColumns, j = 0; i < aColumns.size(); i++, j++)
		{
			dataColumns[j] = i >= aColumns.size() ? new SpreadSheetTableColumn(i) : aColumns.get(i);
		}

		CellValue[][] staticData = aData;
		CellValue[][] data = new CellValue[aData.length][dataColumns.length];
		for (int i = 0; i < data.length; i++)
		{
			data[i] = Arrays.copyOfRange(aData[i], aNumStaticColumns, aData[i].length);
		}

		DefaultTableModel model = new DefaultTableModel(data, aColumns.stream().map(e->e.getHeaderValue()).collect(Collectors.toList()).toArray());

		FixedTable table = new FixedTable(model);
		table.setBorder(null);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setGridColor(new Color(0xDADCDD));
		table.setRowHeight(19);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0,0));
		table.setDefaultRenderer(Object.class, new TableCellRenderer(table, aNumStaticColumns, aStyles));

		ListSelectionModel selectionModel = table.getSelectionModel();

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.setColumnSelectionAllowed(true);
		while (columnModel.getColumnCount() > 0)
		{
			columnModel.removeColumn(columnModel.getColumn(0));
		}
		for (int i = 0; i < aColumns.size() - aNumStaticColumns; i++)
		{
			SpreadSheetTableColumn col = aColumns.get(aNumStaticColumns + i);
			col.setModelIndex(i);
			columnModel.addColumn(col);
		}

		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setDefaultRenderer(new ColumnHeaderRenderer("", aRowNumberSize, 0, table));

		RowNumberTable rowTable = new RowNumberTable(table, staticData, staticColumns, aNumStaticColumns, aRowNumberSize, aRowHeaderSize, aRowHeaders, aStyles);

		ColumnHeaderRenderer cornerLeft = new ColumnHeaderRenderer(aRowHeaderTitle, aRowNumberSize, aRowHeaderSize, table);
		cornerLeft.setDrawLeftBorder(true);
		cornerLeft.setStaticColumns(staticColumns, aNumStaticColumns);

//		ColumnHeaderRenderer cornerRight = new ColumnHeaderRenderer(aRowHeaderTitle, aRowNumberSize, aRowHeaderSize);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerLeft);
//		scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, cornerRight);
		scrollPane.setBorder(null);

		selectionModel.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent aEvent)
			{
//				tableHeader.repaint();
//				table.repaint();
//				rowTable.repaint();
				scrollPane.repaint();
			}
		});

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
//				tableHeader.repaint();
//				table.repaint();
//				rowTable.repaint();
				scrollPane.repaint();
			}
		});

		return scrollPane;
	}


	private void addMissingColumns(CellValue[][] aData, List<SpreadSheetTableColumn> aColumns)
	{
		int columnCount = 0;

		for (CellValue[] row : aData)
		{
			columnCount = Math.max(columnCount, row.length);
		}

		while (columnCount > aColumns.size())
		{
			aColumns.add(new SpreadSheetTableColumn(aColumns.size(), Character.toString((char)('A' + aColumns.size()))));
		}
	}
}
