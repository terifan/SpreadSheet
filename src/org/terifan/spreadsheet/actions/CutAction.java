package org.terifan.spreadsheet.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import org.terifan.spreadsheet.Tuple;
import org.terifan.spreadsheet.ui.FixedTable;


public class CutAction extends AbstractAction
{
	private final static long serialVersionUID = 1L;
	private FixedTable mTable;


	public CutAction(FixedTable aTable)
	{
		mTable = aTable;
		putValue(NAME, "Cut");
	}


	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		StringBuilder sb = new StringBuilder();

		extract(mTable, sb, true);

		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
//		cb.setContents(new CellTransferable(mTable.getValueAt(row, col)), null);
		cb.setContents(new StringSelection(sb.toString()), null);

		mTable.repaint();
	}


	static void extract(FixedTable aTable, StringBuilder aBuffer, boolean aClear)
	{
		int prevRow = -1;
		boolean firstCol = true;
		boolean firstRow = true;
		Tuple anchor = null;

		for (Tuple tuple : aTable.getSelections())
		{
			if (firstRow && firstCol)
			{
				anchor = tuple;
			}
			if (tuple.getRow() != prevRow)
			{
				if (!firstRow)
				{
					aBuffer.append("\n");
				}
				firstRow = false;
				firstCol = true;
			}
			if (!firstCol)
			{
				aBuffer.append("\t");
			}

			String value = aTable.getSpreadSheet().getValueAt(tuple.getRow(), tuple.getCol());
			if (value == null)
			{
				value = "";
			}

			firstCol = false;
			aBuffer.append(value);
			prevRow = tuple.getRow();

			if (aClear)
			{
				aTable.setValueAt(null, tuple.getRow(), tuple.getCol());
			}
		}

		if (anchor != null)
		{
			aTable.setAnchor(anchor, aBuffer.toString());
		}

//		if (aClear)
//		{
//			aTable.invalidate();
//			aTable.revalidate();
//			aTable.validate();
//			aTable.repaint();
//		}
	}
}
