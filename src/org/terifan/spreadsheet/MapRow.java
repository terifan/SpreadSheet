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


	@Override
	public boolean equals(Object aOther)
	{
		if (aOther instanceof MapRow)
		{
			MapRow<T> otherRow = (MapRow<T>)aOther;

			if (otherRow.size() != size())
			{
				return false;
			}

			for (int i = 0; i < size(); i++)
			{
				T item = get(i);
				T otherItem = otherRow.get(i);

				if (item != otherItem && (item == null && otherItem != null || item != null && otherItem == null || !item.equals(otherItem)))
				{
					return false;
				}
			}
		}
		return true;
	}
}
