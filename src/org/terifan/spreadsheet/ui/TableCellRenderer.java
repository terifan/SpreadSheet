package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.terifan.spreadsheet.Cell;
import org.terifan.spreadsheet.CellStyle;
import org.terifan.spreadsheet.SpreadSheet;


public class TableCellRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 1L;

	private final static Color B1B5BA = new Color(0xB1B5BA);
	private final static Color C28A30 = new Color(0xC28A30);
	private final static Color DADCDD = new Color(0xDADCDD);
	private final static Color BLACK = new Color(0x000000);
	private final static Color DARK = new Color(0x808080);

	private final boolean mRowHeader;
	private final FixedTable mMainTable;
	private final FixedTable mTable;
	protected int mRow;
	protected int mColumn;
	protected boolean mDrawLeftBorder;


	public TableCellRenderer(FixedTable aTable)
	{
		mTable = aTable;
		mRowHeader = false;
		mMainTable = null;
		mDrawLeftBorder = false;
	}


	public TableCellRenderer(FixedTable aTable, FixedTable aMainTable)
	{
		mTable = aTable;
		mMainTable = aMainTable;
		mRowHeader = true;
		mDrawLeftBorder = false;
	}


	@Override
	public Component getTableCellRendererComponent(JTable aTable, Object aValue, boolean aSelected, boolean aFocused, int aRow, int aColumn)
	{
		CellStyle DEFAULT = new CellStyle().setFont(mTable.getFont());

		mRow = aRow;
		mColumn = aColumn;

		Component comp = super.getTableCellRendererComponent(aTable, aValue, aSelected, aFocused, aRow, aColumn);

		if (aSelected)
		{
			comp.setForeground(mTable.getSelectionForeground());
			comp.setBackground(mTable.getSelectionBackground());
		}
		else
		{
			comp.setForeground(mTable.getForeground());
			comp.setBackground(mTable.getBackground());
		}

		Cell cell = mTable.getSpreadSheet().getCell(aRow, aColumn);
		CellStyle style;
		if (cell != null)
		{
			style = cell.getStyle();
			if (style == null)
			{
				style = DEFAULT;
			}
		}
		else
		{
			style = DEFAULT;
		}
		style.apply(comp);

		return comp;
	}


	@Override
	protected void paintBorder(Graphics aGraphics)
	{
		int col = mRowHeader ? mColumn - 1 : mColumn;

		int w = getWidth();
		int h = getHeight();

		Color colorLeft = null;
		Color colorRight = null;
		Color colorBottom = null;
		Color colorRightBottom = null;

		if (!mRowHeader)
		{
			SpreadSheet spreadSheet = mTable.getSpreadSheet();
			CellStyle style = getCellStyle(spreadSheet, mRow, mColumn);
			if (style != null && style.getBackgroundColor() != null)
			{
				colorRight = style.getBackgroundColor();
				colorBottom = style.getBackgroundColor();
				colorRightBottom = style.getBackgroundColor();
			}
			if (colorBottom == null)
			{
				style = getCellStyle(spreadSheet, mRow + 1, mColumn);
				if (style != null && style.getBackgroundColor() != null)
				{
					colorBottom = style.getBackgroundColor();
				}
			}
			if (colorRight == null)
			{
				style = getCellStyle(spreadSheet, mRow, mColumn + 1);
				if (style != null && style.getBackgroundColor() != null)
				{
					colorRight = style.getBackgroundColor();
				}
			}
			if (colorRightBottom == null)
			{
				style = getCellStyle(spreadSheet, mRow, mColumn + 1);
				if (style != null && style.getBackgroundColor() != null)
				{
					colorRightBottom = style.getBackgroundColor();
				}
			}
			if (colorRightBottom == null)
			{
				style = getCellStyle(spreadSheet, mRow + 1, mColumn);
				if (style != null && style.getBackgroundColor() != null)
				{
					colorRightBottom = style.getBackgroundColor();
				}
			}
			if (colorRightBottom == null)
			{
				style = getCellStyle(spreadSheet, mRow + 1, mColumn + 1);
				if (style != null && style.getBackgroundColor() != null)
				{
					colorRightBottom = style.getBackgroundColor();
				}
			}
		}

		if (mRowHeader)
		{
			if (mMainTable.isRowSelected(mRow))
			{
				colorBottom = C28A30;
				colorRight = C28A30;
				colorRightBottom = C28A30;
				colorLeft = C28A30;
			}
			else if (mMainTable.isRowSelected(mRow + 1))
			{
				colorBottom = C28A30;
				colorRightBottom = C28A30;
			}

			if (mDrawLeftBorder && colorLeft == null)
			{
				colorLeft = B1B5BA;
			}
		}

		aGraphics.setColor(colorRight == null ? DADCDD : colorRight);
		aGraphics.drawLine(w - 1, 0, w - 1, h - 2);

		aGraphics.setColor(colorBottom == null ? DADCDD : colorBottom);
		aGraphics.drawLine(0, h - 1, w - 2, h - 1);

		aGraphics.setColor(colorRightBottom == null ? DADCDD : colorRightBottom);
		aGraphics.drawLine(w - 1, h - 1, w - 1, h - 1);

		if (colorLeft != null)
		{
			drawLine(aGraphics, colorLeft, 0, 0, 0, h - 1);
		}

		boolean s00 = mTable.isCellSelected(mRow - 1, col - 1);
		boolean s10 = mTable.isCellSelected(mRow, col - 1);
		boolean s20 = mTable.isCellSelected(mRow + 1, col - 1);
		boolean s01 = mTable.isCellSelected(mRow - 1, col);
		boolean s11 = mTable.isCellSelected(mRow, col);
		boolean s21 = mTable.isCellSelected(mRow + 1, col);
		boolean s02 = mTable.isCellSelected(mRow - 1, col + 1);
		boolean s12 = mTable.isCellSelected(mRow, col + 1);
		boolean s22 = mTable.isCellSelected(mRow + 1, col + 1);

		if (s11)
		{
			if (!s12)
			{
				drawLine(aGraphics, BLACK, w - 2, 0, w - 2, h - 2);
			}
			if (!s21)
			{
				drawLine(aGraphics, BLACK, 0, h - 2, w - 2, h - 2);
			}
			if (!s01)
			{
				drawLine(aGraphics, BLACK, 0, 0, w - 1, 0);
			}
			if (!s10)
			{
				drawLine(aGraphics, BLACK, 0, 0, 0, h - 1);
			}
			if (s12 && !s22)
			{
				drawLine(aGraphics, BLACK, w - 1, h - 2, w - 1, h - 2);
			}
			if (s21 && !s22)
			{
				drawLine(aGraphics, BLACK, w - 2, h - 1, w - 2, h - 1);
			}
			if (!s12)
			{
				drawLine(aGraphics, DARK, w - 1, 0, w - 1, h - 1);
			}
			if (!s21)
			{
				drawLine(aGraphics, DARK, 0, h - 1, w - 1, h - 1);
			}
		}
		if (s12)
		{
			if (!s11)
			{
				drawLine(aGraphics, BLACK, w - 2, 0, w - 2, h - 1);
				drawLine(aGraphics, DARK, w - 1, 0, w - 1, h - 1);
			}
		}
		if (s21)
		{
			if (!s11)
			{
				drawLine(aGraphics, BLACK, 0, h - 2, w - 1, h - 2);
				drawLine(aGraphics, DARK, 0, h - 1, w - 1, h - 1);
			}
		}
		if (s22)
		{
			if (!s11)
			{
				drawLine(aGraphics, BLACK, w - 2, h - 2, w - 2, h - 2);
				drawLine(aGraphics, DARK, w - 1, h - 1, w - 1, h - 1);
			}
			if (!s11 && !s12)
			{
				drawLine(aGraphics, BLACK, w - 1, h - 2, w - 1, h - 2);
			}
			if (!s11 && !s10 && !s12 && !s21)
			{
				drawLine(aGraphics, BLACK, w - 2, h - 1, w - 2, h - 1);
			}
		}
		if (s11)
		{
			if (!s22)
			{
				drawLine(aGraphics, BLACK, w - 2, h - 2, w - 2, h - 2);
				drawLine(aGraphics, DARK, w - 1, h - 1, w - 1, h - 1);
			}
		}
		if (s20)
		{
			if (!s11)
			{
				drawLine(aGraphics, BLACK, 0, h - 2, 0, h - 2);
			}
			if (!s11 && !s21)
			{
				drawLine(aGraphics, BLACK, 0, h - 1, 0, h - 1);
			}
		}
		if (s10)
		{
			if (!s11)
			{
				drawLine(aGraphics, BLACK, 0, 0, 0, h - 1);
			}
		}
		if (s02)
		{
			if (!s11)
			{
				drawLine(aGraphics, BLACK, w - 2, 0, w - 2, 0);
			}
			if (!s11 && !s12)
			{
				drawLine(aGraphics, BLACK, w - 1, 0, w - 1, 0);
			}
		}
		if (s01)
		{
			if (!s11)
			{
				drawLine(aGraphics, BLACK, 0, 0, w - 1, 0);
			}
		}
		if (s00)
		{
			if (!s11)
			{
				drawLine(aGraphics, BLACK, 0, 0, 0, 0);
			}
		}
	}


	private CellStyle getCellStyle(SpreadSheet aSpreadSheet, int aRow, int aColumn)
	{
		Cell cell = aSpreadSheet.getCell(aRow, aColumn);
		if (cell == null)
		{
			return null;
		}
		return cell.getStyle();
	}


	private void drawLine(Graphics aGraphics, Color aColor, int aX, int aY, int aWidth, int aHeight)
	{
		aGraphics.setColor(aColor);
		aGraphics.drawLine(aX, aY, aWidth, aHeight);
	}
}
