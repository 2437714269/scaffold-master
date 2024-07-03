package com.acer.service;

import com.acer.model.po.FaceDevice;

/**
 * 查询东区ES抓拍
 */
public interface EastService {


    /**
     * 查询东区人脸抓拍数据
     * @param faceDevice 设备详情
     * @param num 查询抓拍数据数量
     * @param startDateTimeStr 起始时间
     * @param endDateTimeStr 结束时间
     */
    FaceDevice saveDeviceCount(FaceDevice faceDevice, Integer num,String startDateTimeStr,String endDateTimeStr);
    FaceDevice saveDeviceCount1(FaceDevice faceDevice, Integer num,String startDateTimeStr,String endDateTimeStr);
}
