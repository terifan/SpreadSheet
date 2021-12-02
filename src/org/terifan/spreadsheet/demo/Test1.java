package org.terifan.spreadsheet.demo;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.SpreadSheetTableColumn;
import org.terifan.spreadsheet.ui.WorkBook;


public class Test1
{
	public static void main(String... args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			SpreadSheet ss = new SpreadSheet();
			ss.setColumn(new SpreadSheetTableColumn(0, "File Name", 200));
			ss.setColumn(new SpreadSheetTableColumn(1, "Last Modified", 150));
			ss.setColumn(new SpreadSheetTableColumn(2, "Length", 120));

			for (File file : new File("c:\\").listFiles())
			{
				ss.setValueAt(file.getName(), ss.nextRow(), 0);
				ss.setValueAt(new Date(file.lastModified()), ss.getRowCount(), 1);
				ss.setValueAt(file.length(), ss.getRowCount(), 2);
			}

//			ss.setValueAt(2, ss.nextRow(), new Sum(new Range(new Tuple(2, 0), new Tuple(2, ss.lastRow()))));

			ss.print();

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
