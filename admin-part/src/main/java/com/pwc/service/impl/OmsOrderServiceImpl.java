package com.pwc.service.impl;

import com.github.pagehelper.PageHelper;
import com.pwc.dao.OmsOrderDao;
import com.pwc.dao.OmsOrderOperateHistoryDao;
import com.pwc.dto.*;
import com.pwc.mapper.OmsOrderMapper;
import com.pwc.mapper.OmsOrderOperateHistoryMapper;
import com.pwc.model.OmsOrder;
import com.pwc.model.OmsOrderExample;
import com.pwc.model.OmsOrderOperateHistory;
import com.pwc.service.OmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OmsOrderServiceImpl implements OmsOrderService {
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private OmsOrderDao orderDao;
    @Autowired
    private OmsOrderOperateHistoryDao orderOperateHistoryDao;
    @Autowired
    private OmsOrderOperateHistoryMapper orderOperateHistoryMapper;

    @Override
    public List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return orderDao.getList(queryParam);
    }

    @Override
    public int delivery(List<OmsOrderDeliveryParam> deliveryParamList) {
        int count = orderDao.delivery(deliveryParamList);

        List<OmsOrderOperateHistory> historyList = deliveryParamList
                .stream()
                .map(omsOrderDeliveryParam -> {
                    OmsOrderOperateHistory operateHistory = new OmsOrderOperateHistory();
                    operateHistory.setOrderId(omsOrderDeliveryParam.getOrderId());
                    operateHistory.setCreateTime(new Date());
                    operateHistory.setOperateMan("administrator");
                    operateHistory.setOrderStatus(2);
                    operateHistory.setNote("已发货");
                    return operateHistory;
                }).collect(Collectors.toList());
        orderOperateHistoryDao.insertList(historyList);
        return count;
    }

    @Override
    public int close(List<Long> ids, String note) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setStatus(4);
        OmsOrderExample orderExample = new OmsOrderExample();   
        orderExample.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        int count = orderMapper.updateByExampleSelective(omsOrder, orderExample);

        List<OmsOrderOperateHistory> historyList = ids
                .stream()
                .map(orderId -> {
                    OmsOrderOperateHistory operateHistory = new OmsOrderOperateHistory();
                    operateHistory.setOrderId(orderId);
                    operateHistory.setCreateTime(new Date());
                    operateHistory.setOperateMan("administrator");
                    operateHistory.setOrderStatus(4);
                    operateHistory.setNote("订单关闭" + note);
                    return operateHistory;
                }).collect(Collectors.toList());
        orderOperateHistoryDao.insertList(historyList);
        return count;
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrder omsOrder = new OmsOrder();
        omsOrder.setDeleteStatus(1);
        OmsOrderExample orderExample = new OmsOrderExample();
        orderExample.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        return orderMapper.updateByExampleSelective(omsOrder, orderExample);
    }

    @Override
    public OmsOrderDetail detail(Long id) {
        return orderDao.getDetail(id);
    }

    @Override
    public int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
        OmsOrder order = new OmsOrder();
        
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(receiverInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("administrator");
        history.setOrderStatus(receiverInfoParam.getStatus());
        history.setNote("修改收货人信息");
        orderOperateHistoryMapper.insert(history);
        return count;
    }

    @Override
    public int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
        OmsOrder order = new OmsOrder();
        
        order.setId(moneyInfoParam.getOrderId());
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);

        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(moneyInfoParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("administrator");
        history.setOrderStatus(moneyInfoParam.getStatus());
        history.setNote("修改费用信息");
        orderOperateHistoryMapper.insert(history);
        return count;
    }

    @Override
    public int updateNote(Long id, String note, Integer status) {
        OmsOrder order = new OmsOrder();
        order.setId(id);
        order.setNote(note);
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(id);
        history.setCreateTime(new Date());
        history.setOperateMan("administrator");
        history.setOrderStatus(status);
        history.setNote("修改备注信息："+note);
        orderOperateHistoryMapper.insert(history);
        return count;
    }
}
