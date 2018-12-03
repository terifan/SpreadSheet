package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;


public class ColumnHeaderRenderer extends JLabel implements TableCellRenderer
{
	public ColumnHeaderRenderer()
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
			setFont(getFont().deriveFont(Font.BOLD));
		}

		setText((value == null) ? "" : value.toString());
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));

		return this;
	}
}
