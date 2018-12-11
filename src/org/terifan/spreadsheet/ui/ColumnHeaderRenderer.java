package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.terifan.spreadsheet.SpreadSheetTableColumn;


public class ColumnHeaderRenderer extends JLabel implements TableCellRenderer
{
	private static final long serialVersionUID = 1L;

	private boolean mLeftBorder;
	private boolean mTopBorder;
	private SpreadSheetTableColumn[] mStaticColumns;
	private int mNumStaticColumns;
	private String mRowHeaderTitle;
	private int mRowHeaderSize;


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


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		if (mNumStaticColumns > 0 || mRowHeaderSize > 0)
		{
			int x = 50;
			int h = getHeight();

			aGraphics.setColor(new Color(0xF0F0F0));
			aGraphics.fillRect(0, 0, x, h);
			aGraphics.setColor(new Color(0xB1B5BA));
			aGraphics.drawLine(x, 0, x, h);

			if (mRowHeaderSize > 0)
			{
				int w = mRowHeaderSize;

				aGraphics.setColor(Color.BLACK);
				aGraphics.drawString(mRowHeaderTitle, x + 5, 15);

				x += mRowHeaderSize;
			}

			for (int i = 0; i < mNumStaticColumns; i++)
			{
				int z = mStaticColumns[i].getPreferredWidth();

				String data = "" + mStaticColumns[i].getHeaderValue();

				aGraphics.setColor(Color.BLACK);
				aGraphics.drawString(data, x + 5, 15);
				aGraphics.setColor(new Color(0xB1B5BA));
				aGraphics.drawLine(x, 0, x, h);

				x += z;
			}
		}
		else
		{
			super.paintComponent(aGraphics);
		}
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


	void setStaticColumns(SpreadSheetTableColumn[] aStaticColumns, int aNumStaticColumns)
	{
		mStaticColumns = aStaticColumns;
		mNumStaticColumns = aNumStaticColumns;
	}


	void setRowHeaders(String aRowHeaderTitle, int aRowHeaderSize)
	{
		mRowHeaderTitle = aRowHeaderTitle;
		mRowHeaderSize = aRowHeaderSize;
	}
}
