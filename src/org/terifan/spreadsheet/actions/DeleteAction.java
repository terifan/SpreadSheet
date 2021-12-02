package org.terifan.spreadsheet.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import org.terifan.spreadsheet.Tuple;
import org.terifan.spreadsheet.ui.FixedTable;


public class DeleteAction extends AbstractAction
{
	private final static long serialVersionUID = 1L;
	private FixedTable mTable;


	public DeleteAction(FixedTable aTable)
	{
		mTable = aTable;
		putValue(NAME, "Delete");
	}


	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		try
		{
			for (Tuple tuple : mTable.getSelections())
			{
				mTable.setValueAt(null, tuple.getRow(), tuple.getCol());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
	}
}
