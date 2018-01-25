import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class ExcelUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    private ExcelUtil() {
    }

    /**
     * 新建Excel文件
     *
     * @param fileName  文件的名字
     * @param sheetName 工作表名称
     * @param rowNames  第一行的名称
     */
    public static void newExcel(String fileName, String sheetName, String[] rowNames) throws ServiceException {
        WritableWorkbook wwb;
        try {
            wwb = Workbook.createWorkbook(new File(fileName));
        } catch (IOException e) {
            LOGGER.info("新建可写入Excel文件失败 ", e);
            throw new ServiceException("新建excel文件失败");
        }

        WritableSheet sheet = wwb.createSheet(sheetName, 0);
        for (int j = 0; j < rowNames.length; j++) {
            Label label = new Label(j, 0, rowNames[j], getFormat()); //在第0行第j列写入数据
            try {
                sheet.addCell(label);
                sheet.setColumnView(j, 40); //设置第j列的宽度
            } catch (WriteException e) {
                LOGGER.error("新建Excel时候写入标题错误 ", e);
            }
        }
        try {
            wwb.write();
            wwb.close();
        } catch (Exception e) {
            LOGGER.error("保存Excel失败", e);
        }
    }

    /**
     * 在已存在的Excel文件中写入记录 (此方法只提供写入一行记录)
     *
     * @param fileName  文件名称
     * @param sheetName 工作表名称
     * @param contents  写入的内容
     */
    public static void writeExcel(String fileName, String sheetName, String[] contents) {
        WritableWorkbook book = null;
        try {
            Workbook wb = Workbook.getWorkbook(new File(fileName));
            book = Workbook.createWorkbook(new File(fileName), wb);

            WritableSheet sheet = book.getSheet(sheetName);
            int rows = sheet.getRows();

            for (int i = 0; i < contents.length; i++) {
                if (i == 0) {
                    sheet.addCell(new Label(i, rows, contents[i], getFormat()));
                } else {
                    sheet.addCell(new Number(i, rows, Double.valueOf(contents[i]), getFormat()));
                }
            }
        } catch (Exception e) {
            LOGGER.info("写入数据失败", e);
        } finally {
            try {
                if (book != null) {
                    book.write();
                    book.close();
                }
            } catch (Exception e) {
                LOGGER.error("关闭文件失败", e);
            }

        }
    }

    public static void writeExcel(WritableSheet sheet, List<List<String>> excelData) throws WriteException {
        int rows = sheet.getRows();
        for (List<String> rowData : excelData) {
            int column = 0;
            for (String data : rowData) {
                sheet.addCell(new Label(column, rows, data, getFormat()));
                column++;
            }
            rows++;
        }
    }
    /**
     * 获取单元格样式
     */
    private static WritableCellFormat getFormat() {
        WritableCellFormat cell = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 14));
        try {
            cell.setAlignment(Alignment.CENTRE);  //水平居中
            cell.setVerticalAlignment(VerticalAlignment.CENTRE); //垂直居中
        } catch (WriteException e) {
            LOGGER.error("设置单元格样式失败", e);
        }
        return cell;
    }
}


private void makeItemExcel(String fileName, Object items) {
    WritableWorkbook book = null;
    try {
        ExcelUtil.newExcel(fileName, SHEET_NAME, ITEM_COLUMN_NAME);
        Workbook wb = Workbook.getWorkbook(new File(fileName));
        book = Workbook.createWorkbook(new File(fileName), wb);
        WritableSheet sheet = book.getSheet(SHEET_NAME);
        List<List<String>> excelData = 数据处理;
        ExcelUtil.writeExcel(sheet, excelData);
    }catch (Exception e) {
        LOGGER.error("生成excel失败...", e.getCause());
    }finally {
        try {
            if(book!=null) {
                book.write();
                book.close();
            }
        } catch (IOException | WriteException e) {
            LOGGER.error("关闭错误", e);
        }
    }
}


