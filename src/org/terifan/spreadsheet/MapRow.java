package org.terifan.spreadsheet;

import java.util.ArrayList;


public class MapRow<T> extends ArrayList<T>
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


	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T item : this)
		{
			if (!first)
			{
				sb.append(",");
			}
			first = false;

			if (item != null)
			{
				sb.append(item);
			}
		}

		return "[" + sb.toString() + "]";
	}
}
