package org.terifan.spreadsheet.ui;


public class CellTransferable //implements Transferable
{
//	public static final DataFlavor CELL_DATA_FLAVOR = new DataFlavor(Object.class, "application/x-cell-value");
//	private Object mCellValue;
//
//
//	public CellTransferable(Object cellValue)
//	{
//		mCellValue = cellValue;
//	}
//
//
//	@Override
//	public DataFlavor[] getTransferDataFlavors()
//	{
//		return new DataFlavor[]
//		{
//			CELL_DATA_FLAVOR
//		};
//	}
//
//
//	@Override
//	public boolean isDataFlavorSupported(DataFlavor flavor)
//	{
//		return CELL_DATA_FLAVOR.equals(flavor);
//	}
//
//
//	@Override
//	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
//	{
//		if (!isDataFlavorSupported(flavor))
//		{
//			throw new UnsupportedFlavorException(flavor);
//		}
//		return mCellValue;
//	}
}
