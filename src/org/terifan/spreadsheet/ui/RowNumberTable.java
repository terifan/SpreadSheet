package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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


/*
 *	Use a JTable as a renderer for row numbers of a given main table.
 *  This table must be added to the row header of the scrollpane that
 *  contains the main table.
 */
public class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener, TableModelListener
{
	private final JTable mTable;


	public RowNumberTable(JTable aTable)
	{
		mTable = aTable;
		mTable.addPropertyChangeListener(this);
		mTable.getModel().addTableModelListener(this);

		super.setFocusable(false);
		super.setAutoCreateColumnsFromModel(false);
		super.setSelectionModel(mTable.getSelectionModel());
		super.setGridColor(new Color(0xB1B5BA));

		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		column.setCellRenderer(new RowNumberRenderer());
		super.addColumn(column);

		super.getColumnModel().getColumn(0).setPreferredWidth(50);
		super.setPreferredScrollableViewportSize(getPreferredSize());
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


	/*
	 *  Attempt to mimic the table header renderer
	 */
	private static class RowNumberRenderer extends DefaultTableCellRenderer
	{
		public RowNumberRenderer()
		{
			setHorizontalAlignment(JLabel.CENTER);
		}


		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			if (table != null)
			{
				JTableHeader header = table.getTableHeader();

				if (header != null)
				{
					setForeground(header.getForeground());
					setBackground(header.getBackground());
					setFont(header.getFont());
				}
			}

			if (isSelected)
			{
				setBackground(new Color(0xFFDC61));
//				setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0xC28A30)));
			}
			else
			{
				setBackground(new Color(0xF0F0F0));
			}

			setText((value == null) ? "" : value.toString());
			setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(0xB1B5BA)));
//			setBorder(null);

			return this;
		}
	}
}
