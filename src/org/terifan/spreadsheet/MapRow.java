package org.terifan.spreadsheet;

import java.util.ArrayList;
import java.util.Iterator;


public class MapRow<T> implements Iterable<T>
{
	private final ArrayList<T> mList;


	public MapRow()
	{
		mList = new ArrayList<>();
	}


	public int size()
	{
		return mList.size();
	}


	public T remove(int aIndex)
	{
		return mList.remove(aIndex);
	}


	public T get(int aIndex)
	{
		return mList.get(aIndex);
	}


	public boolean add(T aItem)
	{
		return mList.add(aItem);
	}


	public T set(int aIndex, T aItem)
	{
		return mList.set(aIndex, aItem);
	}


	public boolean isEmpty()
	{
		for (T item : mList)
		{
			if (item != null)
			{
				return false;
			}
		}
		return true;
	}


	public int trim()
	{
		while (size() > 0 && get(size() - 1) == null)
		{
			remove(size() - 1);
		}

		mList.trimToSize();

		return size();
	}


	@Override
	public Iterator<T> iterator()
	{
		return mList.iterator();
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
