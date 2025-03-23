package com.pwc.service.impl;

import com.pwc.mapper.OmsOrderSettingMapper;
import com.pwc.model.OmsOrderSetting;
import com.pwc.service.OmsOrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OmsOrderSettingServiceImpl implements OmsOrderSettingService {
    @Autowired
    private OmsOrderSettingMapper orderSettingMapper;
    
    @Override
    public OmsOrderSetting getItem(Long id) {
        return orderSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, OmsOrderSetting orderSetting) {
        orderSetting.setId(id);
        return orderSettingMapper.updateByPrimaryKeySelective(orderSetting);
    }
}
