package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import org.terifan.spreadsheet.Cell;
import org.terifan.spreadsheet.ValueMap;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.SpreadSheetTableColumn;


public class TableFactory
{
	private final static Color DADCDD = new Color(0xDADCDD);


	public SpreadSheetTable createTable(WorkBook aWorkBook, SpreadSheet aSpreadSheet, List<SpreadSheetTableColumn> aColumns, String aRowHeaderTitle, int aRowNumberSize, int aRowHeaderSize, HashMap<Integer, String> aRowHeaders, ValueMap<Cell> aStyles)
	{
		TableModelWrapper model = new TableModelWrapper(aSpreadSheet);

		FixedTable table = new FixedTable(aWorkBook, aSpreadSheet, model);
		table.setBorder(null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // warning: all other modes triggers bugs in BasicTableUI
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setGridColor(DADCDD);
		table.setRowHeight(19);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setDefaultRenderer(Object.class, new TableCellRenderer(table));
		table.setSelectionForeground(null);
		table.setSelectionBackground(null);

		table.setDefaultEditor(String.class, new TableCellEditor()
		{
			private JTextField mTextField = new JTextField();
			private ArrayList<CellEditorListener> mListeners = new ArrayList<>();


			@Override
			public Component getTableCellEditorComponent(JTable aTable, Object aValue, boolean aIsSelected, int aRow, int aColumn)
			{
				mTextField.setText(aSpreadSheet.getValueAt(aRow, aColumn));
				return mTextField;
			}


			@Override
			public Object getCellEditorValue()
			{
				return mTextField.getText();
			}


			@Override
			public boolean isCellEditable(EventObject aEvent)
			{
				if (aEvent instanceof MouseEvent)
				{
					return ((MouseEvent)aEvent).getClickCount() >= 2;
				}
				return true;
			}


			@Override
			public boolean shouldSelectCell(EventObject anEvent)
			{
				return false;
			}


			@Override
			public boolean stopCellEditing()
			{
				for (CellEditorListener listener : mListeners.toArray(new CellEditorListener[0]))
				{
					listener.editingStopped(new ChangeEvent(this));
				}
				return true;
			}


			@Override
			public void cancelCellEditing()
			{
				for (CellEditorListener listener : mListeners.toArray(new CellEditorListener[0]))
				{
					listener.editingStopped(new ChangeEvent(this));
				}
			}


			@Override
			public void addCellEditorListener(CellEditorListener aListener)
			{
				mListeners.add(aListener);
			}


			@Override
			public void removeCellEditorListener(CellEditorListener aListener)
			{
				mListeners.remove(aListener);
			}
		});

		ListSelectionModel selectionModel = table.getSelectionModel();

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.setColumnSelectionAllowed(true);

		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		tableHeader.setDefaultRenderer(new ColumnHeaderRenderer("", aRowNumberSize, 0, table));

		RowNumberTable rowTable = new RowNumberTable(table, aRowNumberSize, aRowHeaderSize, aRowHeaders);

		ColumnHeaderRenderer cornerLeft = new ColumnHeaderRenderer(aRowHeaderTitle == null ? "" : aRowHeaderTitle, aRowNumberSize, aRowHeaderSize, table);

		SpreadSheetTable spreadSheetTable = new SpreadSheetTable(table, aSpreadSheet);
		spreadSheetTable.getScrollPane().setRowHeaderView(rowTable);
		spreadSheetTable.getScrollPane().setCorner(JScrollPane.UPPER_LEFT_CORNER, cornerLeft);
		spreadSheetTable.getScrollPane().setBorder(null);

		selectionModel.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent aEvent)
			{
				if (!aEvent.getValueIsAdjusting())
				{
					spreadSheetTable.fireCellSelected();
				}
				spreadSheetTable.repaint();
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
			public void columnSelectionChanged(ListSelectionEvent aEvent)
			{
				if (!aEvent.getValueIsAdjusting())
				{
					spreadSheetTable.fireCellSelected();
				}
				spreadSheetTable.repaint();
			}
		});

		return spreadSheetTable;
	}
}
