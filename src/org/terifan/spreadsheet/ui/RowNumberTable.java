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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.terifan.spreadsheet.CellStyle;
import org.terifan.spreadsheet.CellValue;
import org.terifan.spreadsheet.Map;
import org.terifan.spreadsheet.SpreadSheetTableColumn;


public class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener, TableModelListener
{
	private static final long serialVersionUID = 1L;

	private JTable mTable;
	private boolean mDrawLeftBorder;
	private SpreadSheetTableColumn[] mStaticColumns;
	private CellValue[][] mStaticData;
	private int mNumStaticColumns;
	private int mRowNumberSize;
	private int mRowHeaderSize;
	private HashMap<Integer, String> mRowHeaders;


	public RowNumberTable(FixedTable aTable, CellValue[][] aStaticData, SpreadSheetTableColumn[] aStaticColumns, int aNumStaticColumns, int aRowNumberSize, int aRowHeaderSize, HashMap<Integer, String> aRowHeaders, Map<CellStyle> aStyles)
	{
		mStaticColumns = aStaticColumns;
		mStaticData = aStaticData;
		mNumStaticColumns = aNumStaticColumns;
		mRowNumberSize = aRowNumberSize;
		mRowHeaderSize = aRowHeaderSize;
		mRowHeaders = aRowHeaders;

		mTable = aTable;
		mTable.addPropertyChangeListener(this);
		mTable.getModel().addTableModelListener(this);

		int width = mRowNumberSize + mRowHeaderSize;

		for (int i = 0; i < mNumStaticColumns; i++)
		{
			width += mStaticColumns[i].getPreferredWidth();
		}

		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		column.setCellRenderer(new RowNumberRenderer(aTable, aStyles));
		column.setPreferredWidth(width);

		super.setFocusable(false);
		super.setAutoCreateColumnsFromModel(false);
		super.setSelectionModel(mTable.getSelectionModel());
		super.setGridColor(new Color(0xB1B5BA));
		super.addColumn(column);
		super.setPreferredScrollableViewportSize(getPreferredSize());
		super.setShowGrid(false);
		super.setIntercellSpacing(new Dimension(0,0));
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
		return Integer.toString(row);
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


	@Override
	public void tableChanged(TableModelEvent e)
	{
		revalidate();
	}


	public void setDrawLeftBorder(boolean aState)
	{
		mDrawLeftBorder = aState;
	}


	private class RowNumberRenderer extends TableCellRenderer
//	private class RowNumberRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		private int mRow;


		public RowNumberRenderer(FixedTable aTable, Map<CellStyle> aStyles)
		{
			super(aTable, 0, aStyles);

			setHorizontalAlignment(JLabel.CENTER);
		}


		@Override
		public Component getTableCellRendererComponent(JTable aTable, Object aValue, boolean aSelected, boolean aFocused, int aRow, int aColumn)
		{
			mRow = aRow;

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

			if (aSelected)
			{
				setBackground(new Color(0xFFDC61));
			}
			else
			{
				setBackground(new Color(0xF0F0F0));
			}

			setText((aValue == null) ? "" : aValue.toString());

			if (mDrawLeftBorder)
			{
				setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(0xB1B5BA)));
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
			if (mNumStaticColumns > 0 || mRowHeaderSize > 0)
			{
				((Graphics2D)aGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

				int h = getHeight();
				boolean rowSelected = mTable.isRowSelected(mRow);

				aGraphics.setColor(rowSelected ? new Color(0xFFDC61) : new Color(0xF0F0F0));
				aGraphics.fillRect(0, 0, mRowNumberSize + mRowHeaderSize, h);

				aGraphics.setColor(Color.BLACK);
				TextPainter.drawString(getText(), 0, 0, mRowNumberSize, getHeight(), true, aGraphics);

				int x = mRowNumberSize;

				if (mRowHeaderSize > 0)
				{
					String text = mRowHeaders.getOrDefault(mRow, "");

					int w = mRowHeaderSize;

					aGraphics.setColor(rowSelected ? new Color(0xFFDC61) : new Color(0xF0F0F0));
					aGraphics.fillRect(x, 0, w, h);
					aGraphics.setColor(new Color(0xB1B5BA));
					aGraphics.drawLine(x, 0, x, h);

					aGraphics.setColor(Color.BLACK);
					TextPainter.drawString(text, x + 2, 0, w - 4, getHeight(), false, aGraphics);

					x += w;
				}

				for (int i = 0; i < mNumStaticColumns; i++)
				{
					int w = mStaticColumns[i].getPreferredWidth();

					String text = mStaticData[mRow][i] == null ? "" : mStaticData[mRow][i].toString();

					aGraphics.setColor(rowSelected ? new Color(0xFFDC61) : new Color(0xFFFFFF));
					aGraphics.fillRect(x, 0, w, h);
					aGraphics.setColor(new Color(0xB1B5BA));
					aGraphics.drawLine(x, 0, x, h);

					aGraphics.setColor(Color.BLACK);
					TextPainter.drawString(text, x + 2, 0, w - 4, getHeight(), false, aGraphics);

					x += w;
				}
			}
			else
			{
				super.paintComponent(aGraphics);
			}
		}
	}
}
