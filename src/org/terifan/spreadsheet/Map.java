package org.terifan.spreadsheet;

import java.util.ArrayList;


public class Map<T>
{
	private ArrayList<MapRow<T>> mMap;


	public Map()
	{
		mMap = new ArrayList<>();
	}


	public T get(int aCol, int aRow)
	{
		if (aRow < 0 || aRow >= mMap.size())
		{
			return null;
		}
		MapRow<T> row = mMap.get(aRow);
		if (row == null)
		{
			return null;
		}
		return aCol < 0 || aCol >= row.size() ? null : row.get(aCol);
	}


	public T get(Tuple aTuple)
	{
		return get(aTuple.mCol, aTuple.mRow);
	}


	public void put(int aCol, int aRow, T aValue)
	{
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
		while (row.size() <= aCol)
		{
			row.add(null);
		}

		row.set(aCol, aValue);
		row.trimToSize();

		if (row.isEmpty())
		{
			mMap.set(aRow, null);
		}
	}


	public void remove(int aCol, int aRow)
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


	public int getMaxRow()
	{
		int rows = mMap.size();
		while (--rows > 0)
		{
			if (mMap.get(rows) != null)
			{
				break;
			}
		}
		return rows;
	}


	public int getColumnCount()
	{
		int count = 0;
		for (MapRow row : mMap)
		{
			if (row != null)
			{
				count = Math.max(count, row.size());
			}
		}
		return count;
	}


	public int getMaxColumn()
	{
		int count = 0;
		for (MapRow row : mMap)
		{
			if (row != null)
			{
				count = Math.max(count, row.size());
			}
		}
		return count - 1;
	}


	@Override
	public String toString()
	{
		return mMap.toString();
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
		return mMap.get(aRow);
	}


	public boolean contains(int aCol, int aRow)
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


	public void addAll(Map<T> aOtherMap)
	{
		for (int rowIndex = 0; rowIndex < aOtherMap.mMap.size(); rowIndex++)
		{
			MapRow<T> otherRow = aOtherMap.get(rowIndex);
			if (otherRow != null)
			{
				for (int colIndex = 0; colIndex < otherRow.size(); colIndex++)
				{
					put(colIndex, rowIndex, otherRow.get(colIndex));
				}
			}
		}
	}
}
