package com.jgc.crm.commns.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class HSSFUtils {
    public static String getCellValue(HSSFCell cell){
        if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            return cell.getStringCellValue();
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BLANK){
            return "";
        }else  if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
            return cell.getNumericCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            return cell.getErrorCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            return cell.getBooleanCellValue()+"";
        }
        return "";
}
}
