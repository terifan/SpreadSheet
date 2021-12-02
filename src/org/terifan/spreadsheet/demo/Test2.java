package org.terifan.spreadsheet.demo;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.ui.WorkBook;


public class Test2
{
	public static void main(String... args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			SpreadSheet ss = new SpreadSheet();
			ss.setValueAt(1, 3, 3);
			ss.setValueAt(2, 4, 3);

			System.out.println(ss.getRowCount());
			System.out.println(ss.getColumnCount());

			WorkBook workBook = new WorkBook();
			workBook.addTab("Sheet1", ss);
			workBook.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

			JFrame frame = new JFrame();
			frame.add(workBook, BorderLayout.CENTER);
			frame.setSize(1024, 768);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}
}
