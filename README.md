# SpreadSheet
SpreadSheet Swing component with some artichmetic capabilites

<img src="https://github.com/terifan/SpreadSheet/blob/master/preview.png"></img>

```java
SpreadSheet ss = new SpreadSheet();
ss.ensureCapacity(20, 10);
ss.setValueAt(4, 0, 0);
ss.setValueAt(7, 0, 1);
ss.setValueAt("=SUM(A1:B1)", 0, 2);
ss.setStyleAt(0, 2, CellStyle.GOOD);

WorkBook workBook = new WorkBook();
workBook.addTab("Sheet1", ss);
workBook.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

JFrame frame = new JFrame();
frame.add(workBook, BorderLayout.CENTER);
```
