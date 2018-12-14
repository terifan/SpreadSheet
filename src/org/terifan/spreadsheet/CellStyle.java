package org.terifan.spreadsheet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;


public class CellStyle
{
	protected Color mBackgroundColor;
	protected Color mForegroundColor;
	protected Font mFont;

	public final static CellStyle BAD = new CellStyle().setBackgroundColor(new Color(0xFFC7CE)).setForegroundColor(new Color(0x9C0006));
	public final static CellStyle GOOD = new CellStyle().setBackgroundColor(new Color(0xC6EFCE)).setForegroundColor(new Color(0x006100));
	public final static CellStyle NEUTRAL = new CellStyle().setBackgroundColor(new Color(0xFFEB9C)).setForegroundColor(new Color(0x9C6500));


	public CellStyle()
	{
	}


	public Color getBackgroundColor()
	{
		return mBackgroundColor;
	}


	public CellStyle setBackgroundColor(Color aBackgroundColor)
	{
		mBackgroundColor = aBackgroundColor;
		return this;
	}


	public Color getForegroundColor()
	{
		return mForegroundColor;
	}


	public CellStyle setForegroundColor(Color aForegroundColor)
	{
		mForegroundColor = aForegroundColor;
		return this;
	}


	public Font getFont()
	{
		return mFont;
	}


	public CellStyle setFont(Font aFont)
	{
		mFont = aFont;
		return this;
	}


	@Override
	public String toString()
	{
		return "CellStyle{" + "bc=[" + toString(mBackgroundColor) + "], fc=[" + toString(mForegroundColor) + "]" + ", font=" + mFont + '}';
	}


	private String toString(Color aColor)
	{
		return aColor == null ? null : aColor.getRed() + "," + aColor.getGreen() + "," + aColor.getBlue();
	}


	public void apply(Component aComponent)
	{
		if (mBackgroundColor != null)
		{
			aComponent.setBackground(mBackgroundColor);
		}
		if (mForegroundColor != null)
		{
			aComponent.setForeground(mForegroundColor);
		}
		if (mFont != null)
		{
			aComponent.setFont(mFont);
		}
	}
}
