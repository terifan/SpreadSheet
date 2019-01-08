package org.terifan.spreadsheet.ui;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.terifan.spreadsheet.SpreadSheet;


public class WorkBook extends JTabbedPane
{
	private static final long serialVersionUID = 1L;


	public WorkBook()
	{
	}


	public WorkBook addTab(String aTitle, SpreadSheet aSpreadSheet)
	{
		ImageIcon icon = new ImageIcon(WorkBook.class.getResource("icon16.png"));

		JScrollPane scrollPane = aSpreadSheet.createUI();
		scrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 2));
		scrollPane.setViewportBorder(null);

		super.addTab(aTitle, icon, scrollPane);

		return this;
	}
}
