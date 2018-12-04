package org.terifan.spreadsheet;

import java.util.ArrayList;


public class MapRow extends ArrayList<CellValue>
{
	@Override
	public void trimToSize()
	{
		while (size() > 0 && get(size() - 1) == null)
		{
			remove(size() - 1);
		}

		super.trimToSize();
	}
}
