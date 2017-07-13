import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {
	String headerName() default "";
}



import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExtend {
	String headerName() default "";
}


import com.xxx.utils.annotation.Excel;
import com.xxx.utils.annotation.ExcelExtend;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {

    final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    static final Integer MAX_CELL_WIDTH = 25;
    /**
     * 数据导入
     *
     * @param file
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> loadFromExcel(MultipartFile file, Class<T> clazz) throws Exception {

        InputStream in = file.getInputStream();
        return loadFromExcel(in, clazz);

    }

    /**
     * 数据导入
     *
     * @param file
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> loadFromExcel(MultipartFile file, Class<T> clazz, boolean useExtendColumns ) throws Exception {

        InputStream in = file.getInputStream();
        return loadFromExcel(in, clazz,useExtendColumns);

    }

    /**
     * 数据导入
     *
     * @param filePath
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> loadFromExcel(String filePath, Class<T> clazz) throws Exception {

        FileInputStream in = new FileInputStream(new File(filePath));
        return loadFromExcel(in, clazz);

    }

    public static <T> List<T> loadFromExcel(InputStream in, Class<T> clazz) throws Exception {
        return loadFromExcel(in, clazz,false);
    }

    /**
     * 数据导入
     *
     * @param in
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> List<T> loadFromExcel(InputStream in, Class<T> clazz,boolean useExtendColumns) throws Exception {

        Map<String, String> headerNames = new HashMap<String, String>();
        Map<String, String> extendColumns = new HashMap<String, String>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field m : fields) {
            if (m.isAnnotationPresent(Excel.class)) {
                // 获取该字段的注解对象
                Excel anno = m.getAnnotation(Excel.class);
                headerNames.put(anno.headerName(), m.getName());
            }
            if( useExtendColumns ){
                if (m.isAnnotationPresent(ExcelExtend.class)) {
                    // 获取该字段的注解对象
                    ExcelExtend anno = m.getAnnotation(ExcelExtend.class);
                    extendColumns.put(anno.headerName(), m.getName());
                }
            }
        }
        if (headerNames.isEmpty()) {
            in.close();
            throw new IllegalArgumentException("表头为空, 请添加表头");
        }

        List<T> list = new ArrayList<T>();

        // HSSFWorkbook xwb = new HSSFWorkbook(in);

        //创建一个Workbook
        Workbook xwb = newExcelByInputStream(in);

        Sheet sheet = xwb.getSheetAt(0);

        Row headerRow = sheet.getRow(sheet.getFirstRowNum());

        //导入Excel文件为空
        if(headerRow==null){
            throw new IllegalArgumentException("导入Excel文件内容为空");
        }
        //headRow和headerNames比较
        Set<String> keySet = headerNames.keySet();
        int rowNum = headerRow.getLastCellNum();
        boolean flag=false;
        //判断上传的文件是否相符
        if(keySet.size()!=0 && rowNum>0) {
            for (int i = 0; i < rowNum; i++) {
                String cell = headerRow.getCell(i).getStringCellValue();
                if (!keySet.contains(cell)) {
                    flag=true;
                }
                if(flag){
                    throw new IllegalArgumentException("导入模板错误");
                }
            }
        }
        boolean hasOneRight = false;
        for (int i = sheet.getFirstRowNum() + 1; i <= 5000; i++) {
            Row row = sheet.getRow(i);
            if( null == row ){ break; }
            //初始化一个bean对象,用于存放当前行数据,其中,每一列的数据对应这个bean对象的某个字段
            T t = clazz.newInstance();
            boolean isAllBlank = true;
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                //获取首行 对应列的字段
                String propertyName = headerNames.get(getCellStringValue(headerRow.getCell(j),String.class));

                if (StringUtils.isNotBlank(propertyName)) {
                    hasOneRight = true;
                    //获取当前列对应的bean字段
                    PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(t, propertyName);
                    //获取bean字段的数据类型
                    Class<?> type = descriptor.getPropertyType();

                    //TODO BigDecimal类型数据转换时,会四舍五入
                    String cellValue = getCellStringValue(cell,type);
                    if( !StringUtils.isBlank(cellValue)){
                        isAllBlank = false;
                    }

                    //给当前行对应的bean对象的某个字段赋值
                    PropertyUtils.setSimpleProperty(t, propertyName, stringToObject(type, cellValue));
                }
            }
            // TODO
            //原来逻辑:--整行为空,则跳出循环
            //现在的逻辑:--整行为空时,结束本层循环读取下一行
            if( isAllBlank ){
                continue;
            }

            //
            if( extendColumns.size() > 0 ){
                for (Map.Entry<String, String> entry : extendColumns.entrySet()) {
                    String value = entry.getValue();
                    if( "row".equals(value) ){
                        PropertyUtils.setSimpleProperty(t, value, i);
                    }else{
                        PropertyUtils.setSimpleProperty(t, value, null);
                    }
                }
            }
            list.add(t);
        }

        if( !hasOneRight ){
            throw new IllegalArgumentException("导入模板错误");
        }
        if( list.size() > 5000 ){
            throw new IllegalArgumentException("导入内容太长");
        }

        return list;
    }

    public static <T> boolean writeToExcel(OutputStream out, Class<T> clazz, List<T> list) throws Exception {
        return writeToExcel(out, clazz, list,false);
    }

    /**
     * 数据导出
     *
     * @param out
     * @param clazz
     * @return
     * @throws Exception
     */

    public static <T> boolean writeToExcel(OutputStream out, Class<T> clazz, List<T> list,boolean useExtendColumn) throws Exception {

        if (list == null)
            throw new RuntimeException("导出的内容为空");
        if (list.isEmpty())
            throw new RuntimeException("导出的内容为空");

        Map<String, String> map = new HashMap<String, String>();
        Field[] fields = clazz.getDeclaredFields();
        List<String> headerNames = fetchTitleList(map, fields,useExtendColumn);
        if (map.isEmpty()) {
            return false;
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = null;
        HSSFRow row = null;
//        CellStyle headerCell = newTableHeaderStyle(workbook);
//        CellStyle evenStyle = newTableEvenCellStyle(workbook);
//        CellStyle oddStyle = newTableOddCellStyle(workbook);
        int rowNumber = 0;
        Short rowHeight = 20*25;
        Map<Integer,Integer> columnWidths = new LinkedHashMap<Integer, Integer>();
        for (int i = 0, length = list.size(); i < length; i++) {
            if (rowNumber == 0) {
                sheet = workbook.createSheet();
                row = sheet.createRow(0);
                row.setHeight(rowHeight);
                for (int j = 0; j < headerNames.size(); j++) {
                    HSSFCell headCell = row.createCell(j);
//                    headCell.setCellStyle(headerCell);
                    String headerName = headerNames.get(j);
                    gatherColumnWidths(columnWidths, headerName, headerName, j);
                    setCellValueByType(headCell, headerNames.get(j));
                }
                rowNumber = 1;
            } else if (rowNumber % 20000 == 0) {
                sheet = workbook.createSheet();
                row = sheet.createRow(0);
                row.setHeight(rowHeight);
                for (int j = 0; j < headerNames.size(); j++) {
                    HSSFCell headCell = row.createCell(j);
//                    headCell.setCellStyle(headerCell);
                    String headerName = headerNames.get(j);
                    gatherColumnWidths(columnWidths, headerName, headerName, j);
                    setCellValueByType(headCell, headerName);
                }
                rowNumber = 1;
            }
            // for( int k = 0 ; k <list.size() ; k++ ){
            row = sheet.createRow(rowNumber % 20000);
            row.setHeight(rowHeight);
            for (int j = 0; j < headerNames.size(); j++) {
                HSSFCell cell = row.createCell(j);
//                if( i % 2 == 0 ){
//                    cell.setCellStyle(oddStyle);
//                }else{
//                    cell.setCellStyle(evenStyle);
//                }
                String headerName = headerNames.get(j);
                if (headerName.equals("序号")) {
                    setCellValueByType(cell, rowNumber % 20000);
                } else {
                    T obj = list.get(i);
                    Object value = PropertyUtils.getSimpleProperty(obj, map.get(headerName));
                    gatherColumnWidths(columnWidths, headerName, String.valueOf(value), j);
                    setCellValueByType(cell, value);
                }
            }

            rowNumber++;
            // }
            setColumnWidth(sheet, columnWidths);
        }
        boolean returnVal = false;
        try {
            workbook.write(out);
            returnVal = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnVal;
    }

    private static Workbook newExcelByInputStream(InputStream inp) throws IOException, InvalidFormatException {
        if (!inp.markSupported()) {
            inp = new PushbackInputStream(inp, 8);
        }
        //操作Excel2003以前（包括2003）的版本，扩展名是.xls
        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
            return new HSSFWorkbook(inp);
        }
        //操作Excel2007的版本，扩展名是.xlsx
        if (POIXMLDocument.hasOOXMLHeader(inp)) {
            return new XSSFWorkbook(OPCPackage.open(inp));
        }
        throw new IllegalArgumentException("Excel文件格式错误,请使用2003版本的Excel上传！");
    }

    /**
     * 获取cell的alue
     * @param cell
     * @param clazz 指定字段类型时,可以根据类型做出处理,当前只针对BigDecimal进行单独处理
     * @return
     */
    private static String getCellStringValue(Cell cell,Class clazz) {
        try {
            if (null == cell) {
                return "";
            }
            /**
             * Cell.CELL_TYPE_BLANK Cell.CELL_TYPE_NUMERIC Cell.CELL_TYPE_STRING
             * Cell.CELL_TYPE_FORMULA Cell.CELL_TYPE_BOOLEAN
             * Cell.CELL_TYPE_ERROR
             */
            if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                return "";
            }
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                if(HSSFDateUtil.isCellDateFormatted(cell)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                }
                //数字格式化
                DecimalFormat df = new DecimalFormat("0");
                //TODO 没找到好的类型比对方法
                if(clazz == BigDecimal.class){
                    df = new DecimalFormat("0.00");
                }
                df.setRoundingMode(RoundingMode.DOWN);
                return df.format(cell.getNumericCellValue());
            }
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                return cell.getStringCellValue();
            }
            if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                return cell.getCellFormula();
            }
            if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            }
            if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                return String.valueOf(cell.getErrorCellValue());
            }
            return cell.getStringCellValue();
        } catch (RuntimeException e) {
            // logger.error("单元格字符值获取,错误信息{}",e.getMessage());
        }
        return "";
    }

    /*
     * 字段类型转换
     */
    private static Object stringToObject(Class<?> clazz, String str) throws Exception {
        Object o = str;
        if (clazz == BigDecimal.class) {
            if( StringUtils.isBlank(str) ){
                o = new BigDecimal(0);
            }else{
                o = new BigDecimal(str);
            }
        } else if (clazz == Long.class) {
            if( StringUtils.isBlank(str) ){
                o = new Long(0);
            }else{
                o = new Long(str);
            }
        } else if (clazz == Integer.class) {
            if( StringUtils.isBlank(str) ){
                o = new Integer(0);
            }else{
                o = new Integer(str);
            }
        } else if (clazz == int.class) {
            o = Integer.parseInt(str);
        } else if (clazz == float.class) {
            o = Float.parseFloat(str);
        } else if (clazz == boolean.class) {
            o = Boolean.parseBoolean(str);
        } else if (clazz == byte.class) {
            o = Byte.parseByte(str);
        }
        return o;
    }

    /**
     * 从对象中获取Excel表头
     *
     * @param map
     *            title名称和字段名称的对应关系
     * @param fields
     *            对象对应的字段
     * @param useExtendColumn
     * @return 返回Title名称
     */
    private static List<String> fetchTitleList(Map<String, String> map, Field[] fields, boolean useExtendColumn) {
        List<String> headerNames = new ArrayList<String>();
        for (Field m : fields) {
            if (m.isAnnotationPresent(Excel.class)) {
                // 获取该字段的注解对象
                Excel anno = m.getAnnotation(Excel.class);
                map.put(anno.headerName(), m.getName());
                headerNames.add(anno.headerName());
            }
            if( useExtendColumn ){
                if (m.isAnnotationPresent(ExcelExtend.class)) {
                    // 获取该字段的注解对象
                    ExcelExtend anno = m.getAnnotation(ExcelExtend.class);
                    map.put(anno.headerName(), m.getName());
                    headerNames.add(anno.headerName());
                }
            }
        }
        return headerNames;
    }

    private static void setCellValueByType(Cell cell, Object value) {
        if (null == value) {
            return;
        }
        if (value instanceof BigDecimal) {
            // cell.setCellValue(((BigDecimal)value).doubleValue());
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Long) {
            cell.setCellValue(((Long) value).longValue());
        } else if (value instanceof Integer) {
            cell.setCellValue(((Integer) value).intValue());
        } else if (value instanceof Float) {
            // cell.setCellValue(Math.floor(((Float)value).floatValue()));
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Double) {
            // cell.setCellValue(Math.floor(((Double)value).doubleValue()));
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Byte) {
            cell.setCellValue(((Byte) value));
        } else if (value instanceof String) {
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Timestamp) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            cell.setCellValue(dateFormat.format(value));
        }
    }

//    private static CellStyle newTableHeaderStyle(Workbook wb) {
//        CellStyle style = wb.createCellStyle();
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
//        style.setWrapText(true);
//        Font font = wb.createFont();
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        style.setFont(font);
//        return style;
//    }
//
//    private static CellStyle newTableEvenCellStyle(Workbook wb) {
//        CellStyle style = wb.createCellStyle();
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setWrapText(true);
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        return style;
//    }
//
//    private static CellStyle newTableOddCellStyle(Workbook wb) {
//        CellStyle style = wb.createCellStyle();
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderBottom(CellStyle.BORDER_THIN);
//        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setWrapText(true);
//        return style;
//    }

    private static void gatherColumnWidths(Map<Integer, Integer> columnWidths, String headerName, String content, int j) {
        int length = content.getBytes().length;
        if( headerName.equalsIgnoreCase(content) ){
            if( headerName.indexOf("（")>0 ){
                headerName = headerName.replace("（", "\r\n（");
                String lengthStr = headerName.substring(0, headerName.indexOf("（"));
                length = lengthStr.getBytes().length;
            }
            columnWidths.put(j,length);
        }else{
            if( content.matches("[0-9a-zA-Z]+") ){
                length = length * 2;
            }
            int oldLength = null==columnWidths.get(j)?0:columnWidths.get(j);
            if( length > MAX_CELL_WIDTH ){
                oldLength = oldLength > MAX_CELL_WIDTH?oldLength:MAX_CELL_WIDTH;
            }else{
                oldLength = oldLength > length?oldLength:length;
            }
            columnWidths.put(j,oldLength);
        }

    }

    private static void setColumnWidth(Sheet sheet, Map<Integer, Integer> columnWidths) {
        for( Map.Entry<Integer,Integer> entry: columnWidths.entrySet() ){
            sheet.setColumnWidth(entry.getKey(), (entry.getValue())*256);
        }
    }
}
