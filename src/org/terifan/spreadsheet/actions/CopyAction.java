package org.terifan.spreadsheet.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.terifan.spreadsheet.ui.FixedTable;
import static javax.swing.Action.NAME;
import static org.terifan.spreadsheet.actions.CutAction.extract;


public class CopyAction extends AbstractAction
{
	private final static long serialVersionUID = 1L;
	private FixedTable mTable;


	public CopyAction(FixedTable aTable)
	{
		mTable = aTable;
		putValue(NAME, "Copy");
	}


	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		StringBuilder sb = new StringBuilder();

		extract(mTable, sb, false);

		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
//		cb.setContents(new CellTransferable(mTable.getValueAt(row, col)), null);
		cb.setContents(new StringSelection(sb.toString()), null);
	}
}
