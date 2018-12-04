package org.terifan.spreadsheet;

import java.util.ArrayList;


public class Map
{
	private ArrayList<MapRow> mMap;


	public Map()
	{
		mMap = new ArrayList<>();
	}


	public CellValue get(int aCol, int aRow)
	{
		MapRow row = mMap.get(aRow);
		if (row == null)
		{
			return null;
		}
		return aCol >= row.size() ? null : row.get(aCol);
	}


	public CellValue get(Tuple aTuple)
	{
		return get(aTuple.mCol, aTuple.mRow);
	}


	public void put(int aCol, int aRow, CellValue aCellValue)
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

		row.set(aCol, aCellValue);
		row.trimToSize();

		if (row.isEmpty())
		{
			mMap.set(aRow, null);
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
}
