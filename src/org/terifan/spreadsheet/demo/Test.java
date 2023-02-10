package org.terifan.spreadsheet.demo;

import java.awt.BorderLayout;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.terifan.spreadsheet.DataLookup;
import org.terifan.spreadsheet.DataProvider;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.ui.WorkBook;


public class Test
{
	public static void main(String... args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			SpreadSheet ss1 = new SpreadSheet();
			ss1.setValueAt(0, 0, "Number of fruits");
			ss1.setValueAt(0, 1, "=count([sheet2]!a1:a100)");
			ss1.setValueAt(1, 0, "Value of all fruits");
			ss1.setValueAt(1, 1, "=sum([sheet2]!b1:b100)");
			ss1.setValueAt(2, 0, "Average value of all fruits");
			ss1.setValueAt(2, 1, "=b2/b1");
			ss1.ensureCapacity(10, 10);
			ss1.setColumnWidths(0, 200); // not working

			SpreadSheet ss2 = new SpreadSheet();
			ss2.parseValuesTo(0, 0, "apple\napricot\navocado\nbanana\nblackberries\nblackcurrant\nblueberries\nboysenberries\ncapers\ncherry\ncranberry\ndates\ndurian\nelderberry\ngrape\ngrapefruit\nguava\njackfruit\njujube\nkiwi\nkumquat\nlemon\nlime\nlychee\nmango\nmelon\nmulberry\nolives\norange\npapaya\npear\npersimmon\npineapple\nplum\npomegranate\nrambutan\nraspberry\nredberries\nrhubarb\nstarfruit\nstrawberry\ntangerine\nwatermelon");
			Random rnd = new Random();
			for (int i = 0; i < ss2.getRowCount(); i++)
			{
				ss2.setValueAt(i, 1, rnd.nextInt(1000)/10.0);
			}

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
			workBook.addTab("Fruits", ss2);
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
