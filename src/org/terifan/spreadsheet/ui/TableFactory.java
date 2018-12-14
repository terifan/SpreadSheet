package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
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
import javax.swing.table.DefaultTableCellRenderer;
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

		JTable table = new JTable(model);
		table.setBorder(null);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setGridColor(new Color(0xDADCDD));
		table.setRowHeight(19);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0,0));

		CellStyle DEFAULT = new CellStyle().setForegroundColor(table.getForeground()).setBackgroundColor(table.getBackground()).setFont(table.getFont());

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
			private boolean mSelected;
			private boolean mHasFocus;
			private int mRow;
			private int mColumn;
			private int mColumnX;


			@Override
			public Component getTableCellRendererComponent(JTable aTable, Object aValue, boolean aSelected, boolean aFocused, int aRow, int aColumn)
			{
				mRow = aRow;
				mColumnX = aColumn;
				mColumn = aNumStaticColumns + aColumn;
				mSelected = aSelected;
				mHasFocus = aFocused;

				Component comp = super.getTableCellRendererComponent(aTable, aValue, aSelected, aFocused, aRow, aColumn);

				aColumn += aNumStaticColumns;

//				if (!aIsSelected)
				{
					CellStyle style = aStyles.get(aColumn, aRow);

					(style == null ? DEFAULT : style).apply(comp);
				}

				return comp;
			}


			@Override
			protected void paintBorder(Graphics aGraphics)
			{
				Color colorRight = null;
				Color colorBottom = null;
				Color colorRightBottom = null;

				CellStyle style = aStyles.get(mColumn, mRow);
				if (style != null && style.getBackgroundColor() != null)
				{
					colorRight = style.getBackgroundColor();
					colorBottom = style.getBackgroundColor();
					colorRightBottom = style.getBackgroundColor();
				}

				if (colorBottom == null)
				{
					style = aStyles.get(mColumn, mRow + 1);
					if (style != null && style.getBackgroundColor() != null)
					{
						colorBottom = style.getBackgroundColor();
					}
				}

				if (colorRight == null)
				{
					style = aStyles.get(mColumn + 1, mRow);
					if (style != null && style.getBackgroundColor() != null)
					{
						colorRight = style.getBackgroundColor();
					}
				}

				if (colorRightBottom == null)
				{
					style = aStyles.get(mColumn + 1, mRow);
					if (style != null && style.getBackgroundColor() != null)
					{
						colorRightBottom = style.getBackgroundColor();
					}
				}
				if (colorRightBottom == null)
				{
					style = aStyles.get(mColumn, mRow + 1);
					if (style != null && style.getBackgroundColor() != null)
					{
						colorRightBottom = style.getBackgroundColor();
					}
				}
				if (colorRightBottom == null)
				{
					style = aStyles.get(mColumn + 1, mRow + 1);
					if (style != null && style.getBackgroundColor() != null)
					{
						colorRightBottom = style.getBackgroundColor();
					}
				}

				aGraphics.setColor(colorRight == null ? new Color(0xDADCDD) : colorRight);
				aGraphics.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 2);

				aGraphics.setColor(colorBottom == null ? new Color(0xDADCDD) : colorBottom);
				aGraphics.drawLine(0, getHeight() - 1, getWidth() - 2, getHeight() - 1);

				aGraphics.setColor(colorRightBottom == null ? new Color(0xDADCDD) : colorRightBottom);
				aGraphics.drawLine(getWidth() - 1, getHeight() - 1, getWidth() - 1, getHeight() - 1);

				if (table.isCellSelected(mRow, mColumnX))
				{
					aGraphics.setColor(Color.BLACK);
					if (!table.isCellSelected(mRow, mColumnX+1))
					{
						aGraphics.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight() - 2);
					}
					if (!table.isCellSelected(mRow+1, mColumnX))
					{
						aGraphics.drawLine(0, getHeight() - 2, getWidth() - 2, getHeight() - 2);
					}
					if (!table.isCellSelected(mRow-1, mColumnX))
					{
						aGraphics.drawLine(0, 0, getWidth() - 1, 0);
					}
					if (!table.isCellSelected(mRow, mColumnX-1))
					{
						aGraphics.drawLine(0, 0, 0, getHeight() - 1);
					}
			if (table.isCellSelected(mRow, mColumnX+1) && !table.isCellSelected(mRow+1, mColumnX+1))
			{
				aGraphics.drawLine(getWidth()-1, getHeight()-2, getWidth()-1, getHeight() - 2);
			}
			if (table.isCellSelected(mRow+1, mColumnX) && !table.isCellSelected(mRow+1, mColumnX+1))
			{
				aGraphics.drawLine(getWidth()-2, getHeight()-1, getWidth()-2, getHeight() - 1);
			}
					aGraphics.setColor(Color.DARK_GRAY);
					if (!table.isCellSelected(mRow, mColumnX+1))
					{
						aGraphics.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1);
					}
					if (!table.isCellSelected(mRow+1, mColumnX))
					{
						aGraphics.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
					}
				}
				if (table.isCellSelected(mRow, mColumnX + 1))
				{
					if (!table.isCellSelected(mRow, mColumnX))
					{
						aGraphics.setColor(Color.BLACK);
						aGraphics.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight() - 1);
						aGraphics.setColor(Color.DARK_GRAY);
						aGraphics.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1);
					}
				}
				if (table.isCellSelected(mRow+1, mColumnX))
				{
					if (!table.isCellSelected(mRow, mColumnX))
					{
						aGraphics.setColor(Color.BLACK);
						aGraphics.drawLine(0, getHeight()-2, getWidth() - 1, getHeight() - 2);
						aGraphics.setColor(Color.DARK_GRAY);
						aGraphics.drawLine(0, getHeight()-1, getWidth() - 1, getHeight() - 1);
					}
				}
				if (table.isCellSelected(mRow+1, mColumnX+1))
				{
					if (!table.isCellSelected(mRow, mColumnX))
					{
						aGraphics.setColor(Color.BLACK);
						aGraphics.drawLine(getWidth()-2, getHeight()-2, getWidth() - 2, getHeight() - 2);
						aGraphics.drawLine(getWidth()-1, getHeight()-2, getWidth() - 1, getHeight() - 2);
						aGraphics.setColor(Color.DARK_GRAY);
						aGraphics.drawLine(getWidth()-1, getHeight()-1, getWidth() - 1, getHeight() - 1);
					}
					if (!table.isCellSelected(mRow, mColumnX) && !table.isCellSelected(mRow, mColumnX-1))
					{
						aGraphics.setColor(Color.DARK_GRAY);
						aGraphics.drawLine(getWidth()-2, getHeight()-1, getWidth() - 2, getHeight() - 1);
					}
				}
				if (table.isCellSelected(mRow+1, mColumnX-1))
				{
					if (!table.isCellSelected(mRow, mColumnX))
					{
					aGraphics.setColor(Color.BLACK);
					aGraphics.drawLine(0, getHeight()-2, 0, getHeight() - 2);
					}
					if (!table.isCellSelected(mRow, mColumnX) && !table.isCellSelected(mRow+1, mColumnX))
					{
					aGraphics.setColor(Color.BLACK);
					aGraphics.drawLine(0, getHeight()-1, 0, getHeight() - 1);
					}
				}
				if (table.isCellSelected(mRow, mColumnX-1))
				{
					if (!table.isCellSelected(mRow, mColumnX))
					{
					aGraphics.setColor(Color.BLACK);
					aGraphics.drawLine(0, 0, 0, getHeight() - 1);
					}
				}
				if (table.isCellSelected(mRow-1, mColumnX+1))
				{
					if (!table.isCellSelected(mRow, mColumnX))
					{
					aGraphics.setColor(Color.BLACK);
					aGraphics.drawLine(getWidth()-2, 0, getWidth()-2, 0);
					}
					if (!table.isCellSelected(mRow, mColumnX) && !table.isCellSelected(mRow, mColumnX+1))
					{
					aGraphics.setColor(Color.BLACK);
					aGraphics.drawLine(getWidth()-1, 0, getWidth()-1, 0);
					}
				}
				if (table.isCellSelected(mRow-1, mColumnX))
				{
					if (!table.isCellSelected(mRow, mColumnX))
					{
					aGraphics.setColor(Color.BLACK);
					aGraphics.drawLine(0, 0, getWidth()-1, 0);
					}
				}
				if (table.isCellSelected(mRow-1, mColumnX-1))
				{
					if (!table.isCellSelected(mRow, mColumnX))
					{
					aGraphics.setColor(Color.BLACK);
					aGraphics.drawLine(0, 0, 0, 0);
					}
				}
			}


			@Override
			protected void paintComponent(Graphics aGraphics)
			{
				super.paintComponent(aGraphics);
			}
		});

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

		selectionModel.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent aEvent)
			{
				table.getTableHeader().repaint();
				table.repaint();
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
				table.getTableHeader().repaint();
				table.repaint();
			}
		});

		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setDefaultRenderer(new ColumnHeaderRenderer("", aRowNumberSize, 0));

		RowNumberTable rowTable = new RowNumberTable(table, staticData, staticColumns, aNumStaticColumns, aRowNumberSize, aRowHeaderSize, aRowHeaders);
		rowTable.setDrawLeftBorder(true);

		ColumnHeaderRenderer cornerLeft = new ColumnHeaderRenderer(aRowHeaderTitle, aRowNumberSize, aRowHeaderSize);
		cornerLeft.setDrawLeftBorder(true);
		cornerLeft.setStaticColumns(staticColumns, aNumStaticColumns);

//		ColumnHeaderRenderer cornerRight = new ColumnHeaderRenderer(aRowHeaderTitle, aRowNumberSize, aRowHeaderSize);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerLeft);
//		scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, cornerRight);
		scrollPane.setBorder(null);

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
