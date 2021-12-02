package org.terifan.spreadsheet.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import static javax.swing.Action.NAME;
import org.terifan.spreadsheet.Expression;
import org.terifan.spreadsheet.ExpressionStatement;
import org.terifan.spreadsheet.ExpressionParser;
import org.terifan.spreadsheet.ExpressionStatementType;
import org.terifan.spreadsheet.Range;
import org.terifan.spreadsheet.Tuple;
import org.terifan.spreadsheet.ui.FixedTable;
import org.terifan.spreadsheet.util.Strings;


public class PasteAction extends AbstractAction
{
	private final static long serialVersionUID = 1L;
	private FixedTable mTable;


	public PasteAction(FixedTable aTable)
	{
		mTable = aTable;
		putValue(NAME, "Paste");
	}


	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

		Transferable transferable = cb.getContents(this);

		try
		{
			String text = Strings.nullToEmpty(transferable.getTransferData(DataFlavor.stringFlavor));

			Tuple anchor = mTable.getAnchor(text);

			int[] rows = mTable.getSelections().stream().mapToInt(e->e.getRow()).toArray();
			int[] cols = mTable.getSelections().stream().mapToInt(e->e.getCol()).toArray();
			int row = mTable.getSelectedRow();
			int col = mTable.getSelectedColumn();
			boolean allRowsConsumed = false;

			for (int destRow = 0; destRow < rows.length; )
			{
				for (String textLines : text.split("\n"))
				{
					if (allRowsConsumed && destRow >= rows.length)
					{
						break;
					}

					row = destRow >= rows.length ? row + 1 : rows[destRow];
					boolean allColsConsumed = false;

					for (int destCol = 0; destCol < cols.length; )
					{
						for (String value : textLines.split("\t"))
						{
							if (allColsConsumed && destCol >= cols.length)
							{
								break;
							}

							col = destCol >= cols.length ? col + 1 : cols[destCol];

							mTable.setValueAt(processValue(value, anchor, row, col), row, col);

							destCol++;
						}

						allColsConsumed = true;
					}
					destRow++;
				}

				allRowsConsumed = true;
			}

//			mTable.ensureCapacity(row, col);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
	}


	private String processValue(String aValue, Tuple aAnchor, int aRow, int aCol)
	{
		if (aValue.startsWith("="))
		{
			Expression expression = new ExpressionParser().compile(aValue.substring(1));
			for (ExpressionStatement e : expression.getStatements())
			{
				if (aAnchor != null)
				{
					if (e.getType() == ExpressionStatementType.TUPLE)
					{
						Tuple tuple = (Tuple)e.getStatement();
						expression.replaceExpression(e, tuple.relative(aRow - aAnchor.getRow(), aCol - aAnchor.getCol()));
					}
					else if (e.getType() == ExpressionStatementType.RANGE)
					{
						Range range = (Range)e.getStatement();
						Tuple from = range.getStart().relative(aRow - aAnchor.getRow(), aCol - aAnchor.getCol());
						Tuple end = range.getEnd().relative(aRow - aAnchor.getRow(), aCol - aAnchor.getCol());
						expression.replaceExpression(e, new Range(from, end));
					}
				}
			}

			aValue = "=" + expression.toString();
		}

		return aValue;
	}
}
