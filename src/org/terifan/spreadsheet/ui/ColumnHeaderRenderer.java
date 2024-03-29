package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.AbstractBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;


public class ColumnHeaderRenderer extends JLabel implements TableCellRenderer
{
	private final static long serialVersionUID = 1L;

	private final static Color F0F0F0 = new Color(0xF0F0F0);
	private final static Color B1B5BA = new Color(0xB1B5BA);
	private final static Color C28A30 = new Color(0xC28A30);
	private final static Color FFDC61 = new Color(0xFFDC61);
	private final static Color BLACK = new Color(0x000000);

	private boolean mLeftBorder;
	private boolean mTopBorder;
	private String mRowHeaderTitle;
	private int mRowHeaderSize;
	private int mRowNumberSize;
	private JTable mTable;


	public ColumnHeaderRenderer(String aRowHeaderTitle, int aRowNumberSize, int aRowHeaderSize, JTable aTable)
	{
		mRowHeaderTitle = aRowHeaderTitle;
		mRowNumberSize = aRowNumberSize;
		mRowHeaderSize = aRowHeaderSize;
		mTable = aTable;

		super.setHorizontalAlignment(JLabel.CENTER);
		super.setOpaque(true);

		mTopBorder = false;

		update();
	}


	@Override
	public Component getTableCellRendererComponent(JTable aTable, Object aValue, boolean aSelected, boolean aFocused, int aRow, int aColumn)
	{
		JTableHeader header = aTable.getTableHeader();

		if (header != null)
		{
			super.setForeground(header.getForeground());
			super.setBackground(header.getBackground());
			super.setFont(header.getFont());
		}

		if (aTable.isColumnSelected(aColumn))
		{
			super.setBackground(FFDC61);
			super.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, C28A30));
		}
		else if (aTable.isColumnSelected(aColumn + 1))
		{
			super.setBorder(new AbstractBorder()
			{
				@Override
				public Insets getBorderInsets(Component aC, Insets aInsets)
				{
					aInsets.top = 0;
					aInsets.right = 1;
					aInsets.bottom = 1;
					aInsets.left = 0;
					return aInsets;
				}


				@Override
				public void paintBorder(Component aC, Graphics aGraphics, int aX, int aY, int aWidth, int aHeight)
				{
					aGraphics.setColor(B1B5BA);
//					aGraphics.drawLine(aX, aY, aX + aWidth - 2, aY);
					aGraphics.drawLine(aX, aY + aHeight - 1, aX + aWidth - 2, aY + aHeight - 1);
					aGraphics.setColor(C28A30);
					aGraphics.drawLine(aX + aWidth - 1, aY, aX + aWidth - 1, aY + aHeight - 1);
				}
			});
		}
		else
		{
			super.setBackground(F0F0F0);
			super.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, B1B5BA));
		}

		super.setText((aValue == null) ? "" : aValue.toString());

		return this;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		if (mRowHeaderSize > 0)
		{
			int x = mRowNumberSize;
			int w = mRowHeaderSize;
			int h = getHeight();

			aGraphics.setColor(F0F0F0);
			aGraphics.fillRect(0, 0, x, h);
			aGraphics.setColor(B1B5BA);
			aGraphics.drawLine(x, 0, x, h);

			TextPainter.drawString(mRowHeaderTitle, x + 2, 0, w - 4, getHeight(), true, aGraphics, BLACK);
		}
		else
		{
			super.paintComponent(aGraphics);
		}
	}


	@Override
	public Dimension getPreferredSize()
	{
		Dimension d = super.getPreferredSize();
		d.height = 20;
		return d;
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
		setBorder(new AbstractBorder()
		{
			@Override
			public Insets getBorderInsets(Component aC, Insets aInsets)
			{
				aInsets.bottom = 1;
				aInsets.left = 0;
				aInsets.right = 1;
				aInsets.top = mTopBorder ? 1 : 0;
				return aInsets;
			}


			@Override
			public void paintBorder(Component aC, Graphics aGraphics, int aX, int aY, int aWidth, int aHeight)
			{
				aGraphics.setColor(B1B5BA);
				if (mLeftBorder)
				{
					aGraphics.drawLine(aX, aY, aX, aY + aHeight);
				}
				if (mTopBorder)
				{
					aGraphics.drawLine(aX, aY, aX + aWidth - 2, aY);
				}
				aGraphics.drawLine(aX, aY + aHeight - 1, aX + aWidth - 2, aY + aHeight - 1);
				if (mTable.isColumnSelected(0))
				{
					aGraphics.setColor(C28A30);
				}
				aGraphics.drawLine(aX + aWidth - 1, aY, aX + aWidth - 1, aY + aHeight - 1);
			}
		});
	}
}
