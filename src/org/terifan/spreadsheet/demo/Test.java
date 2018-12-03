package org.terifan.spreadsheet.demo;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.terifan.spreadsheet.Range;
import org.terifan.spreadsheet.SpreadSheet;
import org.terifan.spreadsheet.Sum;
import org.terifan.spreadsheet.Tuple;


public class Test
{
	public static void main(String... args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			SpreadSheet ss = new SpreadSheet();

//			ss.set(1, 0, "sum(a0:a3");
			ss.set(1, 0, new Sum(new Range(new Tuple(0, 0), new Tuple(0, 4))));
			ss.set(0, 0, 4);
			ss.set(0, 1, 7);
			ss.set(0, 2, 12);
			ss.set(0, 4, new Sum(new Range(new Tuple(0, 0), new Tuple(0, 2))));

			ss.print();
			
			JFrame frame = new JFrame();
			frame.add(ss.createJTable(), BorderLayout.CENTER);
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
