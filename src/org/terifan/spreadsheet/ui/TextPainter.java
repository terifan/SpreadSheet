package org.terifan.spreadsheet.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;


public class TextPainter
{
	static void drawString(String aText, int aX, int aY, int aWidth, int aHeight, boolean aCentered, Graphics aGraphics, Color aColor)
	{
		FontMetrics fm = aGraphics.getFontMetrics();
		((Graphics2D)aGraphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		Shape oldClip = aGraphics.getClip();
		aGraphics.setColor(aColor);
		aGraphics.clipRect(aX, aY, aWidth, aHeight);

		int ty = (aHeight + fm.getHeight() - fm.getDescent()) / 2;

		if (aCentered)
		{
			aGraphics.drawString(aText, aX + Math.max(0, aWidth - fm.stringWidth(aText)) / 2, ty);
		}
		else
		{
			aGraphics.drawString(aText, aX, ty);
		}

		aGraphics.setClip(oldClip);
	}
}
