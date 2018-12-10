package org.terifan.spreadsheet.ui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.terifan.spreadsheet.SpreadSheet;


public class WorkBook extends JTabbedPane
{
	public WorkBook()
	{
	}


	public WorkBook addTab(String aTitle, SpreadSheet aSpreadSheet)
	{
		ImageIcon icon = new ImageIcon(SpreadSheet.class.getResource("icon16.png"));

		JScrollPane scrollPane = aSpreadSheet.createJTable();
		scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE)); // note: correcting a bug in jtabbedpane

		super.addTab("File", icon, scrollPane);
//		super.addTab("File", icon, new JPanel()
//		{
//			{
//				setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.WHITE));
//			}
//			@Override
//			protected void paintComponent(Graphics aG)
//			{
//				aG.setColor(Color.red);
//				aG.fillRect(0, 0, getWidth(), getHeight());
//			}
//
//		});

		return this;
	}
}
