package org.terifan.spreadsheet.ui;

import java.awt.Point;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.terifan.spreadsheet.Map;
import org.terifan.spreadsheet.MapRow;


public class FixedTable extends JTable
{
	private static final long serialVersionUID = 1L;

	private Map<Boolean> mMap;
	private Map<Boolean> mTempMap;
	private Point mFirstExtendCell;
	private Boolean mNewState;


	public FixedTable(TableModel tableModel)
	{
		super(tableModel);

		mMap = new Map<>();
	}


	@Override
	public boolean isRowSelected(int aRow)
	{
		MapRow<Boolean> row = mMap.get(aRow);
		if (row != null)
		{
			for (Boolean b : row)
			{
				if (Boolean.TRUE.equals(b))
				{
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public boolean isColumnSelected(int aColumn)
	{
		for (int i = 0; i <= mMap.getMaxRow(); i++)
		{
			MapRow<Boolean> row = mMap.get(i);
			if (row != null && row.size() > aColumn && Boolean.TRUE.equals(row.get(aColumn)))
			{
				return true;
			}
		}
		return false;
	}


	@Override
	public void changeSelection(int aRow, int aCol, boolean aToggle, boolean aExtend)
	{
//		System.out.println(aRow+" "+aCol+" "+aToggle+" "+aExtend);

		Map<Boolean> old = new Map<>();
		old.addAll(mMap);

		if (aToggle && !aExtend)
		{
			mFirstExtendCell = new Point(aRow, aCol);
			mNewState = !isCellSelected(aRow, aCol);
			mTempMap = new Map<>();
			mTempMap.addAll(mMap);
		}
		if (!aToggle && !aExtend)
		{
			mNewState = true;
		}

		if (aToggle && !aExtend && isCellSelected(aRow, aCol))
		{
			mMap.put(aCol, aRow, false);
		}
		else
		{
			if (!aToggle && !aExtend)
			{
				mMap.clear();
			}

			if (aExtend)
			{
				if (mTempMap == null)
				{
					mTempMap = new Map<>();
					mTempMap.addAll(mMap);
				}
				else
				{
					mMap.clear();
					mMap.addAll(mTempMap);
				}
			}
			else
			{
				mTempMap = null;
			}

			if (!aExtend)
			{
				mMap.put(aCol, aRow, mNewState);
				mFirstExtendCell = new Point(aRow, aCol);
			}
			else
			{
				for (int row = Math.min(mFirstExtendCell.x, aRow); row <= Math.max(mFirstExtendCell.x, aRow); row++)
				{
					for (int col = Math.min(mFirstExtendCell.y, aCol); col <= Math.max(mFirstExtendCell.y, aCol); col++)
					{
						mMap.put(col, row, mNewState);
					}
				}
			}
		}

		super.changeSelection(aRow, aCol, aToggle, aExtend);

		if (!old.equals(mMap))
		{
			repaint();
		}
	}


	@Override
	public void selectAll()
	{
		mMap.clear();
		super.selectAll();
	}


	@Override
	public void clearSelection()
	{
		if (mMap != null)
		{
			mMap.clear();
		}
		super.clearSelection();
	}


	@Override
	public boolean isCellSelected(int aRow, int aCol)
	{
		return Boolean.TRUE.equals(mMap.get(aCol, aRow));
	}


	public Map<Boolean> getSelections()
	{
		return mMap;
	}
}
