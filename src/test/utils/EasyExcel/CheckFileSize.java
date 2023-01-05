/**
     * -------------------
     * 功能定义_批量导入功能点
     */
    @PutMapping("/importFunctionPoints")
    @ApiOperation(value = "功能定义_批量导入功能点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", paramType = "form", value = "功能点Excel", dataType = "file", required = true),
            @ApiImplicitParam(name = "moduleCode", value = "模块编码", dataTypeClass = String.class, required = true)})
    public ResultModel importFunctionPoints(@RequestPart("file") MultipartFile file, @RequestParam String moduleCode) throws Exception




//=============== controller
@PostMapping("importGoods")
    @ApiOperation(value = "导入商品", httpMethod = "POST")
    public IResult importGoods(@RequestParam("file") MultipartFile file,@RequestParam("batchId") String batchId,
                               @RequestParam("singleNature") String singleNature,@RequestParam("categoryNature") String categoryNature,
                               @RequestParam("firstSingle") boolean firstSingle){

        log.info("========>进入导入商品方法");
        IResult result = new IResult();
        if(EmptyUtil.isNullOrEmpty(file)){
            return result.setCode(IResultCode.FileEmpty);
        }
        List<String> batchIdList=null;
        if(StringUtils.isNotEmpty(batchId)){
            batchIdList= JSONArray.parseArray(batchId,String.class);
        }
        //Long userId = getUser().getUserId();
        if(EmptyUtil.isNullOrEmpty(getUser())){
            return result.setCode(IResultCode.LoginInvalid);
        }
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            log.info("=====》导入文件格式不对");
            return result.setCode(IResultCode.ParameterFormat);
        }
        try {
            Map<String,Object> map =iActiveBaseService.importGoods(file,batchIdList,singleNature,categoryNature,firstSingle);
            if("firstSingle".equals(map.get("firstSingle"))){
                return result.setCode(IResultCode.GoodsKindError);
            }
            return result.setCode(IResultCode.Success).setData(map);
        } catch (Exception e) {
            log.info("导入商品失败:"+e);
            return result.setCode(IResultCode.Error);
        }

		

//========================== 封装单元格数据
public static List<List<String>> getCellValueToList2(MultipartFile multipartFile) throws Exception {

		Workbook work = null;
		List<List<String>> result = new ArrayList<List<String>>();
		try {
			work = WorkbookUtil.getWorkbook(multipartFile);
		} catch (Exception e) {
			throw new Exception("读取excel表格失败!");
		}
		Sheet sheet = work.getSheetAt(0);
		int rowNum = sheet.getLastRowNum() + 1;
		Row row = null;
		Cell cell = null;
		int bodyNum = sheet.getRow(0).getLastCellNum();

		for (int i = 2; i < rowNum; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			if(StringUtils.isEmpty(WorkbookUtil.getCellValueAsString(row.getCell(0)))){
				continue;
			}
			List<String> list = new ArrayList<String>();
			for (int n = 0; n < bodyNum; n++) {
				cell = row.getCell(n);
				String value = WorkbookUtil.getCellValueAsString(cell);

				list.add(value);
			}
			result.add(list);
		}
		return result;
	}		
		
		


//================================ 导出
@GetMapping("exportGoods")
    @ApiOperation(value = "导出商品", httpMethod = "GET")
    public void exportGoods(String importCode, HttpServletResponse response) {

        log.info("商品导出开始");
        List<GoodsColumn> result = iActiveBaseService.findGoodsBatchInfo(Long.parseLong(importCode));
        ActiveExcelUtils.writeExcel(response, result, GoodsColumn.class,"exportGoods");
        log.info("商品导出完成");
    }		
	
	
//=========================== POI工具类
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WorkbookUtil {

    public static Workbook getWorkbook(MultipartFile multipartFile) throws Exception {

	Workbook xls = null;
	String fileName = multipartFile.getOriginalFilename();
	String fileType = fileName.substring(fileName.lastIndexOf("."));
	try {
	    // 创建对Excel工作簿文件的引用
	    if (".xls".equals(fileType)) {
		xls = new HSSFWorkbook(multipartFile.getInputStream());
	    } else if (".xlsx".equals(fileType)) {
		xls = new XSSFWorkbook(multipartFile.getInputStream());
	    } else {
		throw new Exception("解析的文件格式有误！");
	    }
	    return xls;
	} catch (FileNotFoundException ex) {
	    throw new Exception("EXCEL文件未找到！请检查文件路径是否正确。FileName ：" + fileName);
	} catch (IOException ex) {
	    throw new Exception("IO异常！无法读取EXCEL文件。" + ex.getMessage());
	}
    }


    public static boolean isNull(Object o) {
	return (null == o);
    }

    /**
     * 获取单元格数据
     * 
     * @return
     */
    public static String getCellValueAsString(Cell aCell) {
	if (isNull(aCell)){
		return "";
	}
	DecimalFormat df = new DecimalFormat("#");
	String value = null;
	int cellType = aCell.getCellType();
	switch (cellType) {
	case Cell.CELL_TYPE_BLANK:
	    value = "";
	    break;
	case Cell.CELL_TYPE_BOOLEAN:
	    value = aCell.getBooleanCellValue() ? "true" : "false";
	    break;
	case Cell.CELL_TYPE_NUMERIC:
	    if (HSSFDateUtil.isCellDateFormatted(aCell)) {
		Date date = aCell.getDateCellValue();
		value = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	    } else {
		value = df.format(aCell.getNumericCellValue());
	    }
	    break;
	case Cell.CELL_TYPE_STRING:
	    value = aCell.getStringCellValue();
	    break;
	default:
	    break;
	}
	return value;
    }

    /**
     * 封装单元格数据
     * 
     * @return
     * @throws Exception
     */
    public static List<List<String>> getCellValueToList(MultipartFile multipartFile) throws Exception {

	Workbook work = null;
	List<List<String>> result = new ArrayList<List<String>>();
	try {
	    work = WorkbookUtil.getWorkbook(multipartFile);
	} catch (Exception e) {
	    throw new Exception("读取excel表格失败!"+e);
	}
	Sheet sheet = work.getSheetAt(0);
	int rowNum = sheet.getLastRowNum() + 1;
	Row row = null;
	Cell cell = null;
	int bodyNum = sheet.getRow(0).getLastCellNum();

	for (int i = 1; i < rowNum; i++) {
	    row = sheet.getRow(i);
	    if (row == null) {
		continue;
	    }
	    if(StringUtils.isEmpty(WorkbookUtil.getCellValueAsString(row.getCell(0)))){
			continue;
		}
	    List<String> list = new ArrayList<String>();
	    for (int n = 0; n < bodyNum; n++) {
		cell = row.getCell(n);
		String value = WorkbookUtil.getCellValueAsString(cell);

		list.add(value);
	    }
	    result.add(list);
	}
	return result;
    }

	/**
	 * 封装单元格数据
	 *
	 * @return
	 * @throws Exception
	 */
	public static List<List<String>> getCellValueToList2(MultipartFile multipartFile) throws Exception {

		Workbook work = null;
		List<List<String>> result = new ArrayList<List<String>>();
		try {
			work = WorkbookUtil.getWorkbook(multipartFile);
		} catch (Exception e) {
			throw new Exception("读取excel表格失败!");
		}
		Sheet sheet = work.getSheetAt(0);
		int rowNum = sheet.getLastRowNum() + 1;
		Row row = null;
		Cell cell = null;
		int bodyNum = sheet.getRow(0).getLastCellNum();

		for (int i = 2; i < rowNum; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			if(StringUtils.isEmpty(WorkbookUtil.getCellValueAsString(row.getCell(0)))){
				continue;
			}
			List<String> list = new ArrayList<String>();
			for (int n = 0; n < bodyNum; n++) {
				cell = row.getCell(n);
				String value = WorkbookUtil.getCellValueAsString(cell);

				list.add(value);
			}
			result.add(list);
		}
		return result;
	}

    
}
}
