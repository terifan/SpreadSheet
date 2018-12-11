package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;


public class ColumnHeaderRenderer extends JLabel implements TableCellRenderer
{
	private static final long serialVersionUID = 1L;

	private boolean mLeftBorder;
	private boolean mTopBorder;


	public ColumnHeaderRenderer()
	{
		setHorizontalAlignment(JLabel.CENTER);
		setOpaque(true);

		mTopBorder = true;

		update();
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

		if (table.isColumnSelected(column))
		{
			setBackground(new Color(0xFFDC61));
		}
		else
		{
			setBackground(new Color(0xF0F0F0));
		}

		setText((value == null) ? "" : value.toString());

		return this;
	}


	public void setDrawLeftBorder(boolean aState)
	{
		mLeftBorder = aState;
		update();
	}


	public void setDrawTopBorder(boolean aState)
	{
		mTopBorder = aState;
		update();
	}


	private void update()
	{
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(mTopBorder ? 1 : 0, mLeftBorder ? 1 : 0, 1, 1, new Color(0xB1B5BA)), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
	}
}
