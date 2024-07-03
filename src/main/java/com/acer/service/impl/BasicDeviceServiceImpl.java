package com.acer.service.impl;

import com.acer.dao.BasicDeviceMapping;
import com.acer.model.po.FaceDevice;
import com.acer.service.BasicDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicDeviceServiceImpl implements BasicDeviceService {

    private final BasicDeviceMapping basicDeviceMapping;


    @Override
    public List<FaceDevice> saveDevice() {

        return basicDeviceMapping.saveDevice();
    }
}
