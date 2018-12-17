package org.terifan.spreadsheet.ui;

import java.awt.Point;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.terifan.spreadsheet.Map;


public class FixedTable extends JTable
{
	private static final long serialVersionUID = 1L;

	private Map<Boolean> mMap = new Map<>();
	private Map<Boolean> mTempMap;
	private Point mFirstExtendCell;
	private Boolean mNewState;


	public FixedTable(TableModel tableModel)
	{
		super(tableModel);
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
	public void addRowSelectionInterval(int index0, int index1)
	{
//		for (int i = index0; i < index1; i++)
//		{
//			mMap.remove(i);
//		}
//		super.addRowSelectionInterval(index0, index1);
	}


	@Override
	public void removeRowSelectionInterval(int index0, int index1)
	{
//		for (int i = index0; i < index1; i++)
//		{
//			mMap.remove(i);
//		}
//		super.removeRowSelectionInterval(index0, index1);
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
}
