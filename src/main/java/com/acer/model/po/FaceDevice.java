package com.acer.model.po;


import com.acer.annotate.ExcelAnnotate;
import com.acer.annotate.ExcelSheetAnnotate;
import com.acer.model.excel.BaseExcel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备详情
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ExcelSheetAnnotate(sheet = 0)
public class FaceDevice extends BaseExcel {


    /**
     * 小区编号
     */
    @ExcelAnnotate(name = "小区编号")
    public String communityCode;

    /**
     * 小区名称
     */
    @ExcelAnnotate(name = "设备名称")
    public String deviceName;

    /**
     * 设备编号
     */
    @ExcelAnnotate(name = "设备逻辑编号")
    public String deviceCode;

    /**
     * 设备ip
     */
    @ExcelAnnotate(name = "设备ip")
    public String deviceIp;

    @ExcelAnnotate(name = "设备类型")
    public String deviceType;



}
