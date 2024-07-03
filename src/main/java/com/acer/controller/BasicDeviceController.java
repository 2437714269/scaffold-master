package com.acer.controller;

import com.acer.model.po.FaceDevice;
import com.acer.service.BasicDeviceService;
import com.acer.service.EastService;
import com.acer.utils.ExcelUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BasicDeviceController {

    private final BasicDeviceService basicDeviceService;

    private final EastService eastService;

    /**
     * 查找抓拍
     * @param num 当日设备抓拍数量
     * @param startDateTimeStr 起始时间
     * @param endDateTimeStr 结束时间
     */
    @GetMapping("/getDevice")
    public void getDevice(MultipartFile file, Integer num, String startDateTimeStr, String endDateTimeStr){
        List<FaceDevice> result = null;

        try {
            List<FaceDevice> faceDevices = ExcelUtils.uploadExcel(file, FaceDevice.class);
            System.out.println("读取Excel设备数量："+faceDevices.size());
            result = new ArrayList<>(faceDevices.size());
            for (FaceDevice faceDevice : faceDevices) {
                eastService.saveDeviceCount1(faceDevice,num,startDateTimeStr,endDateTimeStr);
                // if (Objects.isNull(eastService.saveDeviceCount1(faceDevice,num,startDateTimeStr,endDateTimeStr))) {
                //     System.out.println("无数据的设备："+faceDevice.toString());
                //     result.add(faceDevice);
                // }
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // ExcelUtils.downloadExcel("/home/hong_ji/device.xlsx",result);
    }
}
