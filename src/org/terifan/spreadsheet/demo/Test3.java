package org.terifan.spreadsheet.demo;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.terifan.spreadsheet.CellStyle;
import org.terifan.spreadsheet.DataLookup;
import org.terifan.spreadsheet.DataProvider;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.ui.WorkBook;


public class Test3
{
	public static void main(String... args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			SpreadSheet ss1 = new SpreadSheet();
//			ss.setColumn(new SpreadSheetTableColumn(0, "John"));
//			ss.setColumn(new SpreadSheetTableColumn(1, "Lisa"));
//			ss.setColumn(new SpreadSheetTableColumn(3, "Total", 200));

//			ss.setStaticColumnCount(2);

//			ss.setRowHeaderTitle("alpha");
//			ss.setRowHeader(0, "q");
//			ss.setRowHeader(1, "w");
//			ss.setRowHeader(2, "e");
//			ss.setRowHeader(3, "r");
//			ss.setRowHeader(4, "t");
//			ss.setRowHeader(5, "y");

			ss1.setValueAt(0, 0, 4);
			ss1.setValueAt(1, 0, 7);
			ss1.setValueAt(2, 0, 12);
			ss1.setValueAt(0, 1, 10);
			ss1.setValueAt(1, 1, 5);
			ss1.setValueAt(2, 1, 8);
			ss1.setValueAt(0, 3, "=SUM(A1:B1)");
			ss1.setValueAt(1, 3, "=SUM(A2:B2)");
			ss1.setValueAt(2, 3, "=SUM(A3:B3)");
			ss1.setValueAt(4, 0, "=SUM(A1:A3)");
			ss1.setValueAt(4, 1, "=SUM(B1:B3)");
			ss1.setValueAt(4, 3, "=SUM(A5:B5)");
//			ss.setValueAt(5, 4, "=SUM(A5)");
//			ss.setValueAt(12, 0, "=count(a1:a5)");
//			ss.setValueAt(7, 0, "=sum(a1:d5)");
//			ss.setValueAt(7, 3, "=pow(a1,b1)");
//			ss.setValueAt(8, 3, "=a1^b1");
//			ss.setValueAt(6, 3, "=sum(a1,b1,a2,b2,a3,b3)");
//			ss.setValueAt(6, 5, "=count(a1,b1,c1)");
//
//			ss.setValueAt(40, 3, 1);
//			ss.setValueAt(3, 40, 1);

			ss1.setValueAt(10, 0, "=[sheet1]!a1");
			ss1.setValueAt(10, 1, "=[sheet2]!a1");

			ss1.setStyleAt(0, 3, CellStyle.GOOD);
			ss1.setStyleAt(2, 3, CellStyle.BAD);
			ss1.setStyleAt(4, 3, CellStyle.NEUTRAL);

			ss1.setStyleAt(6, 3, CellStyle.BAD);
			ss1.setStyleAt(7, 3, CellStyle.GOOD);

			ss1.setStyleAt(6, 5, CellStyle.BAD);
			ss1.setStyleAt(7, 5, CellStyle.GOOD);
			ss1.setStyleAt(6, 6, CellStyle.NEUTRAL);
			ss1.setStyleAt(7, 6, CellStyle.NEUTRAL);

			ss1.setStyleAt(7, 0, CellStyle.NEUTRAL);

			SpreadSheet ss2 = new SpreadSheet().ensureCapacity(100, 26);

			ss2.setValueAt(0, 0, 111);

			DataLookup dataLookup = new DataLookup()
			{
				@Override
				public DataProvider getDataProvider(String aReference)
				{
					if (aReference.equals("[sheet1]")) return ss1;
					if (aReference.equals("[sheet2]")) return ss2;
					throw new IllegalArgumentException(aReference);
				}
			};

			ss1.setDataLookup(dataLookup);
			ss2.setDataLookup(dataLookup);

			WorkBook workBook = new WorkBook();
			workBook.addTab("Sheet1", ss1);
			workBook.addTab("Sheet2", ss2);
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
