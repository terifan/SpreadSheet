package org.terifan.spreadsheet;

import java.util.HashMap;
import java.util.HashSet;



public class SpreadSheet
{
	private HashMap<Tuple, Object> mMap;


	public SpreadSheet()
	{
		mMap = new HashMap<>();
	}


	void set(int aCol, int aRow, Object aValue)
	{
		mMap.put(new Tuple(aCol, aRow), aValue);
	}


	void removeEmptyRows()
	{
		int maxRow = getMaxRow();
		for (int i = 0; i < maxRow; i++)
		{

		}
	}


	int getRowCount()
	{
		HashSet<Integer> rows = new HashSet<>();
		for (Tuple tuple : mMap.keySet())
		{
			rows.add(tuple.getCol());
		}
		return rows.size();
	}


	int getMaxRow()
	{
		int rows = 0;
		for (Tuple tuple : mMap.keySet())
		{
			rows = Math.max(tuple.getCol(), rows);
		}
		return rows;
	}


	int getColumnCount()
	{
		HashSet<Integer> rows = new HashSet<>();
		for (Tuple tuple : mMap.keySet())
		{
			rows.add(tuple.getRow());
		}
		return rows.size();
	}


	int getMaxColumn()
	{
		int rows = 0;
		for (Tuple tuple : mMap.keySet())
		{
			rows = Math.max(tuple.getRow(), rows);
		}
		return rows;
	}


	void print()
	{
		int maxColumn = getMaxColumn();
		int maxRow = getMaxRow();

		for (int row = 0; row < maxRow; row++)
		{
			System.out.print(row);
			System.out.print("\t");
			for (int col = 0; col < maxColumn; col++)
			{
				System.out.print(get(col,row));
				System.out.print("\t");
			}
			System.out.println();
		}
	}


	private Object get(int aCol, int aRow)
	{
		return mMap.get(new Tuple(aCol, aRow));
	}
}
