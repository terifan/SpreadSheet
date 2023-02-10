package org.terifan.spreadsheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


public class ValueMap<T> implements Iterable<MapRow<T>>, Serializable
{
	private final static long serialVersionUID = 1L;

	private ArrayList<MapRow<T>> mMap;
	private int mColumnCount;


	public ValueMap()
	{
		mMap = new ArrayList<>();
	}


	public T get(int aRow, int aCol)
	{
		if (aRow < 0 || aRow >= mMap.size() || aCol < 0 || aCol >= mColumnCount)
		{
//			System.out.println("*");
			return null;
		}
		MapRow<T> row = mMap.get(aRow);
		if (row == null)
		{
//			System.out.println("&");
			return null;
		}
		if (aCol < 0 || aCol >= row.size())
		{
//			System.out.println("%");
			return null;
		}
		return row.get(aCol);
	}


	public T get(Tuple aTuple)
	{
		return get(aTuple.getRow(), aTuple.getCol());
	}


	public void put(int aRow, int aColumn, T aValue)
	{
		if (aColumn < 0 || aRow < 0)
		{
			throw new IllegalArgumentException();
		}

		MapRow row = aRow >= mMap.size() ? null : mMap.get(aRow);
		if (row == null)
		{
			row = new MapRow();
			while (mMap.size() <= aRow)
			{
				mMap.add(null);
			}
			mMap.set(aRow, row);
		}
		while (row.size() <= aColumn)
		{
			row.add(null);
		}

		row.set(aColumn, aValue);

		mColumnCount = Math.max(mColumnCount, aColumn + 1);

		if (row.isEmpty())
		{
			mMap.set(aRow, null);
		}
	}


	public void remove(int aRow, int aCol)
	{
		if (mMap.size() >= aRow)
		{
			MapRow<T> row = mMap.get(aRow);
			if (row != null && row.size() >= aCol)
			{
				row.remove(aCol);

				if (row.isEmpty())
				{
					mMap.set(aRow, null);
				}
			}
		}
	}


	public int getRowCount()
	{
		return mMap.size();
	}


	public int getColumnCount()
	{
		return mColumnCount;
	}


	public void clear()
	{
		mMap.clear();
	}


	public void remove(int aRow)
	{
		mMap.remove(aRow);
	}


	public MapRow<T> get(int aRow)
	{
		return aRow < 0 || aRow >= mMap.size() ? null : mMap.get(aRow);
	}


	public boolean contains(int aRow, int aCol)
	{
		if (mMap.size() <= aRow)
		{
			return false;
		}
		MapRow<T> row = mMap.get(aRow);
		if (row != null && row.size() > aCol)
		{
			return row.get(aCol) != null;
		}
		return false;
	}


	public void addAll(ValueMap<T> aOtherMap)
	{
		for (int rowIndex = 0; rowIndex < aOtherMap.mMap.size(); rowIndex++)
		{
			MapRow<T> otherRow = aOtherMap.get(rowIndex);
			if (otherRow != null)
			{
				for (int colIndex = 0; colIndex < otherRow.size(); colIndex++)
				{
					put(rowIndex, colIndex, otherRow.get(colIndex));
				}
			}
		}
	}


	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		for (MapRow<T> item : mMap)
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
		if (aOther instanceof ValueMap)
		{
			return mMap.equals(((ValueMap)aOther).mMap);
		}
		return false;
	}


	@Override
	public Iterator<MapRow<T>> iterator()
	{
		return mMap.iterator();
	}


	public int countColumns()
	{
		int count = 0;
		for (MapRow row : mMap)
		{
			if (row != null)
			{
				count = Math.max(count, row.size());
			}
		}
		mColumnCount = count;

		return mColumnCount;
	}


	public void trim()
	{
		int rows = getRowCount();
		int maxColumns = 0;
		boolean found = false;

		for (int y = rows; --y >= 0; )
		{
			MapRow<T> row = mMap.get(y);

			if (row != null)
			{
				int cols = row.trim();

				if (cols == 0)
				{
					if (!found)
					{
						mMap.remove(y);
					}
					else
					{
						mMap.set(y, null);
					}
				}
				else
				{
					found = true;
				}

				maxColumns = Math.max(maxColumns, cols);
			}
			else if (!found)
			{
				mMap.remove(row);
			}
		}

		mColumnCount = maxColumns;
	}


//	public static void main(String ... args)
//	{
//		try
//		{
//			Map<Boolean> map = new Map<>();
//			map.put(10, 10, Boolean.FALSE);
//			map.put(10, 20, Boolean.FALSE);
//			System.out.println(map.getRowCount()+"/"+map.getColumnCount()+" "+map);
//			map.remove(10, 20);
//			System.out.println(map.getRowCount()+"/"+map.getColumnCount()+" "+map);
//			map.trim();
//			System.out.println(map.getRowCount()+"/"+map.getColumnCount()+" "+map);
//			map.remove(10, 10);
//			System.out.println(map.getRowCount()+"/"+map.getColumnCount()+" "+map);
//			map.trim();
//			System.out.println(map.getRowCount()+"/"+map.getColumnCount()+" "+map);
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace(System.out);
//		}
//	}
}
