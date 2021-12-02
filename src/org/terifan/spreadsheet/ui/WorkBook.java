package org.terifan.spreadsheet.ui;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.Tuple;


public class WorkBook extends JComponent
{
	private static final long serialVersionUID = 1L;

	private ArrayList<CellSelectionListener> mCellSelectionListeners;
	private JTabbedPane mTabbedPane;
	private JTextField mFormulaField;
	private Tuple mTargetCell;


	public WorkBook()
	{
		mCellSelectionListeners = new ArrayList<>();

		mTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		mFormulaField = new JTextField();

		mFormulaField.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent aEvent)
			{
				if (getSelectedTable().getSelections().isEmpty())
				{
					mTargetCell = null;
				}
				else
				{
					mTargetCell = getSelectedTable().getSelections().get(0);
					mFormulaField.setText(getSelectedTable().getSpreadSheet().getValueAt(mTargetCell));
				}
			}

			@Override
			public void focusLost(FocusEvent aEvent)
			{
				if (mTargetCell != null)
				{
					getSelectedTable().getTable().setValueAt(mTargetCell, mFormulaField.getText());
					getSelectedTable().repaint();
				}
			}
		});
		mFormulaField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent aEvent)
			{
				if (mTargetCell != null)
				{
					if (aEvent.getKeyCode() == KeyEvent.VK_ENTER)
					{
						getSelectedTable().getTable().setValueAt(mTargetCell, mFormulaField.getText());
						getSelectedTable().repaint();
					}
				}
			}
		});

		mTabbedPane.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent aEvent)
			{
				if (aEvent.getSource() instanceof JTabbedPane)
				{
					mTargetCell = null;
					mFormulaField.setText("");
				}
			}
		});

		super.setLayout(new BorderLayout());
		super.add(mFormulaField, BorderLayout.NORTH);
		super.add(mTabbedPane, BorderLayout.CENTER);
	}


	public int getTableCount()
	{
		return mTabbedPane.getComponentCount();
	}


	public void removeAllSheets()
	{
		mTabbedPane.removeAll();
	}


	public WorkBook addTab(String aTitle, SpreadSheet aSpreadSheet)
	{
		ImageIcon icon = new ImageIcon(WorkBook.class.getResource("icon16.png"));

		SpreadSheetTable table = aSpreadSheet.createUI(this);
		table.setWorkBook(this);
		table.getScrollPane().setBorder(BorderFactory.createEmptyBorder());
		table.getScrollPane().setViewportBorder(null);

		mTabbedPane.addTab(aTitle, icon, table);

		return this;
	}


	public SpreadSheet getSpreadSheet(int aIndex)
	{
		return getTable(aIndex).getSpreadSheet();
	}


	public SpreadSheetTable getTable(int aIndex)
	{
		return (SpreadSheetTable)mTabbedPane.getComponent(aIndex);
	}


	public SpreadSheetTable getTable(SpreadSheet aSpreadSheet)
	{
		for (int i = 0; i < mTabbedPane.getTabCount(); i++)
		{
			SpreadSheetTable table = (SpreadSheetTable)mTabbedPane.getComponent(i);
			if (table.getSpreadSheet() == aSpreadSheet)
			{
				return table;
			}
		}

		return null;
	}


	public SpreadSheetTable getSelectedTable()
	{
		return (SpreadSheetTable)mTabbedPane.getSelectedComponent();
	}


	void fireCellSelected()
	{
		SpreadSheetTable selectedTable = getSelectedTable();
		SpreadSheet spreadSheet = selectedTable.getTable().getSpreadSheet();
		ArrayList<Tuple> selections = selectedTable.getSelections();

		if (selections.size() == 1)
		{
			mFormulaField.setText(spreadSheet.getValueAt(selections.get(0)));
		}
		else
		{
			mFormulaField.setText("");
		}
	}


	public int getPageCount()
	{
		return mTabbedPane.getComponentCount();
	}


	public WorkBook addCellSelectionListener(CellSelectionListener aCellSelectionListener)
	{
		mCellSelectionListeners.add(aCellSelectionListener);
		return this;
	}


	public WorkBook removeCellSelectionListener(CellSelectionListener aCellSelectionListener)
	{
		mCellSelectionListeners.remove(aCellSelectionListener);
		return this;
	}


	void fireCellSelected(WorkBook aWorkBook, SpreadSheet aSpreadSheet, int aRow, int aColumn, boolean aToggle, boolean aExtend)
	{
		for (CellSelectionListener listener : mCellSelectionListeners)
		{
			listener.changeSelection(aWorkBook, aSpreadSheet, aRow, aColumn, aToggle, aExtend);
		}
	}
}
