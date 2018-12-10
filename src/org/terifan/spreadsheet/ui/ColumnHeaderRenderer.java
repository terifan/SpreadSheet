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
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0xB1B5BA)), BorderFactory.createEmptyBorder(3,3,3,3)));
		setOpaque(true);
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
}
