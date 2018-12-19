package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.terifan.spreadsheet.CellStyle;
import org.terifan.spreadsheet.Map;


public class TableCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	private final boolean mRowHeader;
	private final FixedTable mMainTable;
	private final FixedTable mTable;
	private final Map<CellStyle> mStyles;
	private int mNumStaticColumns;
	protected int mRow;
	protected int mColumn;
	protected int mColumnX;
	protected boolean mDrawLeftBorder;


	public TableCellRenderer(FixedTable aTable, int aNumStaticColumns, Map<CellStyle> aStyles)
	{
//		mNumStaticColumns = aNumStaticColumns;
		mStyles = aStyles;
		mTable = aTable;
		mRowHeader = false;
		mMainTable = null;
	}


	public TableCellRenderer(FixedTable aTable, FixedTable aMainTable, int aNumStaticColumns, Map<CellStyle> aStyles)
	{
		mMainTable = aMainTable;
//		mNumStaticColumns = aNumStaticColumns;
		mStyles = aStyles;
		mTable = aTable;
		mRowHeader = true;
	}


	@Override
	public Component getTableCellRendererComponent(JTable aTable, Object aValue, boolean aSelected, boolean aFocused, int aRow, int aColumn)
	{
		CellStyle DEFAULT = new CellStyle().setForegroundColor(mTable.getForeground()).setBackgroundColor(mTable.getBackground()).setFont(mTable.getFont());

		mRow = aRow;
		mColumnX = aColumn;
		mColumn = mNumStaticColumns + aColumn;

		Component comp = super.getTableCellRendererComponent(aTable, aValue, aSelected, aFocused, aRow, aColumn);

		aColumn += mNumStaticColumns;

//		if (!aIsSelected)
		{
			CellStyle style = mStyles.get(aColumn, aRow);

			(style == null ? DEFAULT : style).apply(comp);
		}

		return comp;
	}


	@Override
	protected void paintBorder(Graphics aGraphics)
	{
		Color colorLeft = null;
		Color colorRight = null;
		Color colorBottom = null;
		Color colorRightBottom = null;

		CellStyle style = mStyles.get(mColumn, mRow);
		if (style != null && style.getBackgroundColor() != null)
		{
			colorRight = style.getBackgroundColor();
			colorBottom = style.getBackgroundColor();
			colorRightBottom = style.getBackgroundColor();
		}
		if (colorBottom == null)
		{
			style = mStyles.get(mColumn, mRow + 1);
			if (style != null && style.getBackgroundColor() != null)
			{
				colorBottom = style.getBackgroundColor();
			}
		}
		if (colorRight == null)
		{
			style = mStyles.get(mColumn + 1, mRow);
			if (style != null && style.getBackgroundColor() != null)
			{
				colorRight = style.getBackgroundColor();
			}
		}
		if (colorRightBottom == null)
		{
			style = mStyles.get(mColumn + 1, mRow);
			if (style != null && style.getBackgroundColor() != null)
			{
				colorRightBottom = style.getBackgroundColor();
			}
		}
		if (colorRightBottom == null)
		{
			style = mStyles.get(mColumn, mRow + 1);
			if (style != null && style.getBackgroundColor() != null)
			{
				colorRightBottom = style.getBackgroundColor();
			}
		}
		if (colorRightBottom == null)
		{
			style = mStyles.get(mColumn + 1, mRow + 1);
			if (style != null && style.getBackgroundColor() != null)
			{
				colorRightBottom = style.getBackgroundColor();
			}
		}

		if (mRowHeader)
		{
			if (mMainTable.isRowSelected(mRow))
			{
				colorBottom = new Color(0xC28A30);
				colorRight = new Color(0xC28A30);
				colorRightBottom = new Color(0xC28A30);
				colorLeft = new Color(0xC28A30);
			}
			else if (mMainTable.isRowSelected(mRow + 1))
			{
				colorBottom = new Color(0xC28A30);
				colorRightBottom = new Color(0xC28A30);
			}

			if (mDrawLeftBorder && colorLeft == null)
			{
				colorLeft = new Color(0xB1B5BA);
			}
		}

		aGraphics.setColor(colorRight == null ? new Color(0xDADCDD) : colorRight);
		aGraphics.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 2);

		aGraphics.setColor(colorBottom == null ? new Color(0xDADCDD) : colorBottom);
		aGraphics.drawLine(0, getHeight() - 1, getWidth() - 2, getHeight() - 1);

		aGraphics.setColor(colorRightBottom == null ? new Color(0xDADCDD) : colorRightBottom);
		aGraphics.drawLine(getWidth() - 1, getHeight() - 1, getWidth() - 1, getHeight() - 1);

		if (colorLeft != null)
		{
			aGraphics.setColor(colorLeft);
			aGraphics.drawLine(0, 0, 0, getHeight() - 1);
		}

		if (mTable.isCellSelected(mRow, mColumnX))
		{
			aGraphics.setColor(Color.BLACK);
			if (!mTable.isCellSelected(mRow, mColumnX + 1))
			{
				aGraphics.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight() - 2);
			}
			if (!mTable.isCellSelected(mRow + 1, mColumnX))
			{
				aGraphics.drawLine(0, getHeight() - 2, getWidth() - 2, getHeight() - 2);
			}
			if (!mTable.isCellSelected(mRow - 1, mColumnX))
			{
				aGraphics.drawLine(0, 0, getWidth() - 1, 0);
			}
			if (!mTable.isCellSelected(mRow, mColumnX - 1))
			{
				aGraphics.drawLine(0, 0, 0, getHeight() - 1);
			}
			if (mTable.isCellSelected(mRow, mColumnX + 1) && !mTable.isCellSelected(mRow + 1, mColumnX + 1))
			{
				aGraphics.drawLine(getWidth() - 1, getHeight() - 2, getWidth() - 1, getHeight() - 2);
			}
			if (mTable.isCellSelected(mRow + 1, mColumnX) && !mTable.isCellSelected(mRow + 1, mColumnX + 1))
			{
				aGraphics.drawLine(getWidth() - 2, getHeight() - 1, getWidth() - 2, getHeight() - 1);
			}
			aGraphics.setColor(Color.DARK_GRAY);
			if (!mTable.isCellSelected(mRow, mColumnX + 1))
			{
				aGraphics.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1);
			}
			if (!mTable.isCellSelected(mRow + 1, mColumnX))
			{
				aGraphics.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
			}
		}
		if (mTable.isCellSelected(mRow, mColumnX + 1))
		{
			if (!mTable.isCellSelected(mRow, mColumnX))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight() - 1);
				aGraphics.setColor(Color.DARK_GRAY);
				aGraphics.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1);
			}
		}
		if (mTable.isCellSelected(mRow + 1, mColumnX))
		{
			if (!mTable.isCellSelected(mRow, mColumnX))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(0, getHeight() - 2, getWidth() - 1, getHeight() - 2);
				aGraphics.setColor(Color.DARK_GRAY);
				aGraphics.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
			}
		}
		if (mTable.isCellSelected(mRow + 1, mColumnX + 1))
		{
			if (!mTable.isCellSelected(mRow, mColumnX))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(getWidth() - 2, getHeight() - 2, getWidth() - 2, getHeight() - 2);
				aGraphics.drawLine(getWidth() - 1, getHeight() - 2, getWidth() - 1, getHeight() - 2);
				aGraphics.setColor(Color.DARK_GRAY);
				aGraphics.drawLine(getWidth() - 1, getHeight() - 1, getWidth() - 1, getHeight() - 1);
			}
			if (!mTable.isCellSelected(mRow, mColumnX) && !mTable.isCellSelected(mRow, mColumnX - 1))
			{
				aGraphics.setColor(Color.DARK_GRAY);
				aGraphics.drawLine(getWidth() - 2, getHeight() - 1, getWidth() - 2, getHeight() - 1);
			}
		}
		if (mTable.isCellSelected(mRow, mColumnX))
		{
			if (!mTable.isCellSelected(mRow + 1, mColumnX + 1))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(getWidth() - 2, getHeight() - 2, getWidth() - 2, getHeight() - 2);
				aGraphics.setColor(Color.DARK_GRAY);
				aGraphics.drawLine(getWidth() - 1, getHeight() - 1, getWidth() - 1, getHeight() - 1);
			}
		}
		if (mTable.isCellSelected(mRow + 1, mColumnX - 1))
		{
			if (!mTable.isCellSelected(mRow, mColumnX))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(0, getHeight() - 2, 0, getHeight() - 2);
			}
			if (!mTable.isCellSelected(mRow, mColumnX) && !mTable.isCellSelected(mRow + 1, mColumnX))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(0, getHeight() - 1, 0, getHeight() - 1);
			}
		}
		if (mTable.isCellSelected(mRow, mColumnX - 1))
		{
			if (!mTable.isCellSelected(mRow, mColumnX))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(0, 0, 0, getHeight() - 1);
			}
		}
		if (mTable.isCellSelected(mRow - 1, mColumnX + 1))
		{
			if (!mTable.isCellSelected(mRow, mColumnX))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(getWidth() - 2, 0, getWidth() - 2, 0);
			}
			if (!mTable.isCellSelected(mRow, mColumnX) && !mTable.isCellSelected(mRow, mColumnX + 1))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(getWidth() - 1, 0, getWidth() - 1, 0);
			}
		}
		if (mTable.isCellSelected(mRow - 1, mColumnX))
		{
			if (!mTable.isCellSelected(mRow, mColumnX))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(0, 0, getWidth() - 1, 0);
			}
		}
		if (mTable.isCellSelected(mRow - 1, mColumnX - 1))
		{
			if (!mTable.isCellSelected(mRow, mColumnX))
			{
				aGraphics.setColor(Color.BLACK);
				aGraphics.drawLine(0, 0, 0, 0);
			}
		}
	}
}
