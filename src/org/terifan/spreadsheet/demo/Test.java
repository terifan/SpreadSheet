package org.terifan.spreadsheet.demo;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.terifan.spreadsheet.Range;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.SpreadSheetTableColumn;
import org.terifan.spreadsheet.functions.Subtract;
import org.terifan.spreadsheet.functions.Sum;
import org.terifan.spreadsheet.Tuple;
import org.terifan.spreadsheet.ui.WorkBook;


public class Test
{
	public static void main(String... args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			SpreadSheet ss = new SpreadSheet();
			ss.setColumn(new SpreadSheetTableColumn(0, "John"));
			ss.setColumn(new SpreadSheetTableColumn(1, "Lisa"));
			ss.setColumn(new SpreadSheetTableColumn(3, "Total", 200));

//			ss.setRowHeaderSize(50);
			ss.setRowHeaderTitle("alpha");
			ss.setRowHeader(0, "q");
			ss.setRowHeader(1, "w");
			ss.setRowHeader(2, "e");
			ss.setRowHeader(3, "r");
			ss.setRowHeader(4, "t");
			ss.setRowHeader(5, "y");

//			ss.set(1, 0, "sum(a0:a3");
			ss.set(0, 0, 4);
			ss.set(0, 1, 7);
			ss.set(0, 2, 12);
			ss.set(1, 0, 10);
			ss.set(1, 1, 5);
			ss.set(1, 2, 8);
			ss.set(3, 0, new Sum(new Range(new Tuple(0, 0), new Tuple(1, 0))));
			ss.set(3, 1, new Sum(new Range(new Tuple(0, 1), new Tuple(1, 1))));
			ss.set(3, 2, new Sum(new Range(new Tuple(0, 2), new Tuple(1, 2))));
			ss.set(0, 4, new Sum(new Range(new Tuple(0, 0), new Tuple(0, 2))));
			ss.set(1, 4, new Sum(new Range(new Tuple(1, 0), new Tuple(1, 2))));
			ss.set(3, 4, new Sum(new Range(new Tuple(0, 4), new Tuple(1, 4))));

			ss.set(3, 6, new Subtract(new Tuple(3, 4), new Tuple(0, 0)));

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
