package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


public class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener, TableModelListener
{
	private final static long serialVersionUID = 1L;

	private final static Color B1B5BA = new Color(0xB1B5BA);
	private final static Color FFDC61 = new Color(0xFFDC61);
	private final static Color F0F0F0 = new Color(0xF0F0F0);
	private final static Color FF0000 = new Color(0xff0000);
	private final static Color C28A30 = new Color(0xC28A30);
	private final static Color BLACK = new Color(0x000000);

	private JTable mTable;
	private int mRowNumberSize;
	private int mRowHeaderSize;
	private HashMap<Integer, String> mRowHeaders;


	public RowNumberTable(FixedTable aTable, int aRowNumberSize, int aRowHeaderSize, HashMap<Integer, String> aRowHeaders)
	{
		mRowNumberSize = aRowNumberSize;
		mRowHeaderSize = aRowHeaderSize;
		mRowHeaders = aRowHeaders;

		mTable = aTable;
		mTable.addPropertyChangeListener(this);
		mTable.getModel().addTableModelListener(aEvent ->
		{
			revalidate();
			DefaultTableModel model = (DefaultTableModel)getModel();
			int rowCount = mTable.getModel().getRowCount();
			while (model.getRowCount() < rowCount)
			{
				model.addRow(new Object[0]);
			}
		});

		int width = mRowNumberSize + mRowHeaderSize;

		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		column.setCellRenderer(new RowNumberRenderer(aTable, false));
		column.setPreferredWidth(width);

		super.setFocusable(false);
		super.setAutoCreateColumnsFromModel(false);
		super.setSelectionModel(mTable.getSelectionModel());
		super.setGridColor(FF0000);
		super.addColumn(column);
		super.setPreferredScrollableViewportSize(getPreferredSize());
		super.setShowGrid(false);
		super.setIntercellSpacing(new Dimension(0, 0));
	}


	@Override
	public void addNotify()
	{
		super.addNotify();

		Component c = getParent();

		//  Keep scrolling of the row table in sync with the main table.
		if (c instanceof JViewport)
		{
			JViewport viewport = (JViewport)c;
			viewport.addChangeListener(this);
		}
	}


	/*
	 *  Delegate method to main table
	 */
	@Override
	public int getRowCount()
	{
		return mTable.getRowCount();
	}


	@Override
	public int getRowHeight(int row)
	{
		int rowHeight = mTable.getRowHeight(row);

		if (rowHeight != super.getRowHeight(row))
		{
			super.setRowHeight(row, rowHeight);
		}

		return rowHeight;
	}


	/*
	 *  No model is being used for this table so just use the row number
	 *  as the value of the cell.
	 */
	@Override
	public Object getValueAt(int row, int column)
	{
		return Integer.toString(1 + row);
	}


	/*
	 *  Don't edit data in the main TableModel by mistake
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}


	/*
	 *  Do nothing since the table ignores the model
	 */
	@Override
	public void setValueAt(Object value, int row, int column)
	{
	}


	@Override
	public void stateChanged(ChangeEvent e)
	{
		//  Keep the scrolling of the row table in sync with main table

		JViewport viewport = (JViewport)e.getSource();
		JScrollPane scrollPane = (JScrollPane)viewport.getParent();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}


	@Override
	public void propertyChange(PropertyChangeEvent e)
	{
		//  Keep the row table in sync with the main table

		if ("selectionModel".equals(e.getPropertyName()))
		{
			setSelectionModel(mTable.getSelectionModel());
		}

		if ("rowHeight".equals(e.getPropertyName()))
		{
			repaint();
		}

		if ("model".equals(e.getPropertyName()))
		{
			mTable.getModel().addTableModelListener(this);
			revalidate();
		}
	}


	private class RowNumberRenderer extends TableCellRenderer
	{
		private static final long serialVersionUID = 1L;


		public RowNumberRenderer(FixedTable aTable, boolean aDrawLeftBorder)
		{
			super(aTable, aTable);

			setHorizontalAlignment(JLabel.CENTER);

			mDrawLeftBorder = aDrawLeftBorder;
		}


		@Override
		public Component getTableCellRendererComponent(JTable aTable, Object aValue, boolean aSelected, boolean aFocused, int aRow, int aColumn)
		{
			mRow = aRow;
			mColumn = aColumn;

			if (aTable != null)
			{
				JTableHeader header = aTable.getTableHeader();

				if (header != null)
				{
					setForeground(header.getForeground());
					setBackground(header.getBackground());
					setFont(header.getFont());
				}
			}

			setBackground(aSelected ? FFDC61 : F0F0F0);
			setText((aValue == null) ? "" : aValue.toString());

			if (mDrawLeftBorder)
			{
				setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, aSelected ? C28A30 : B1B5BA));
			}
			else
			{
				setBorder(null);
			}

			return this;
		}


		@Override
		protected void paintComponent(Graphics aGraphics)
		{
			boolean rowSelected = mTable.isRowSelected(mRow);

			if (mRowHeaderSize > 0)
			{
				int h = getHeight();

				TextPainter.drawString(getText(), 0, 0, mRowNumberSize, h, true, aGraphics, BLACK);

				String text = mRowHeaders.getOrDefault(mRow, "");

				aGraphics.setColor(rowSelected ? FFDC61 : F0F0F0);
				aGraphics.fillRect(mRowNumberSize, 0, mRowHeaderSize, h);
				aGraphics.setColor(B1B5BA);
				aGraphics.drawLine(mRowNumberSize, 0, mRowNumberSize, h);

				TextPainter.drawString(text, mRowNumberSize + 2, 0, mRowHeaderSize - 4, h, false, aGraphics, BLACK);
			}
			else
			{
				setBackground(rowSelected ? FFDC61 : F0F0F0);

				super.paintComponent(aGraphics);
			}
		}
	}
}
