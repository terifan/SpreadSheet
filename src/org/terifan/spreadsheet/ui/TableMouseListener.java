package org.terifan.spreadsheet.ui;

import org.terifan.spreadsheet.actions.CutAction;
import org.terifan.spreadsheet.actions.PasteAction;
import org.terifan.spreadsheet.actions.CopyAction;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import org.terifan.spreadsheet.actions.DeleteAction;


public class TableMouseListener extends MouseAdapter
{
	private final FixedTable mTable;
	private final JPopupMenu mPopupMenu;


	TableMouseListener(FixedTable aTable)
	{
		mTable = aTable;

		mPopupMenu = new JPopupMenu();
		mPopupMenu.add(new CopyAction(mTable));
		mPopupMenu.add(new CutAction(mTable));
		mPopupMenu.add(new PasteAction(mTable));
		mPopupMenu.addSeparator();
		mPopupMenu.add(new DeleteAction(mTable));
	}


	@Override
	public void mouseClicked(MouseEvent aEvent)
	{
		if (aEvent.isPopupTrigger())
		{
			doPopup(aEvent);
		}
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		if (aEvent.isPopupTrigger())
		{
			doPopup(aEvent);
		}
	}


	protected void doPopup(MouseEvent aEvent)
	{
		int row = mTable.rowAtPoint(aEvent.getPoint());
		int col = mTable.columnAtPoint(aEvent.getPoint());

		if (!mTable.isCellSelected(row, col))
		{
			mTable.selectCell(row, col);
		}

		mPopupMenu.show(aEvent.getComponent(), aEvent.getX(), aEvent.getY());
	}
}
