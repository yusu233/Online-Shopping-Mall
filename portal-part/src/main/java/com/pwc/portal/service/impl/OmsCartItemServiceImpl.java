package com.pwc.portal.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.pwc.mapper.OmsCartItemMapper;
import com.pwc.model.OmsCartItem;
import com.pwc.model.OmsCartItemExample;
import com.pwc.model.UmsMember;
import com.pwc.portal.dao.PortalProductDao;
import com.pwc.portal.domain.CartProduct;
import com.pwc.portal.domain.CartPromotionItem;
import com.pwc.portal.service.OmsCartItemService;
import com.pwc.portal.service.OmsPromotionService;
import com.pwc.portal.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {
    @Autowired
    private OmsCartItemMapper cartItemMapper;
    @Autowired
    private PortalProductDao productDao;
    @Autowired
    private OmsPromotionService promotionService;
    @Autowired
    private UmsMemberService memberService;

    @Override
    public int add(OmsCartItem cartItem) {
        int count;
        UmsMember currentMember =memberService.getCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
        OmsCartItem existCartItem = getCartItem(cartItem);
        if (existCartItem == null) {
            cartItem.setCreateDate(new Date());
            count = cartItemMapper.insert(cartItem);
        } else {
            cartItem.setModifyDate(new Date());
            existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            count = cartItemMapper.updateByPrimaryKey(existCartItem);
        }
        return count;
    }
    
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        OmsCartItemExample example = new OmsCartItemExample();
        OmsCartItemExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(cartItem.getMemberId())
                .andProductIdEqualTo(cartItem.getProductId()).andDeleteStatusEqualTo(0);
        if (cartItem.getProductSkuId()!=null) {
            criteria.andProductSkuIdEqualTo(cartItem.getProductSkuId());
        }
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(cartItemList)) {
            return cartItemList.get(0);
        }
        return null;
    }

    @Override
    public List<OmsCartItem> list(Long memberId) {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andMemberIdEqualTo(memberId);
        return cartItemMapper.selectByExample(example);
    }

    @Override
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds) {
        List<OmsCartItem> cartItemList = list(memberId);
        if(CollUtil.isNotEmpty(cartIds)){
            cartItemList = cartItemList.stream().filter(item->cartIds.contains(item.getId())).collect(Collectors.toList());
        }
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(cartItemList)){
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public int updateQuantity(Long id, Long memberId, Integer quantity) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0)
                .andIdEqualTo(id).andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(cartItem, example);
    }

    @Override
    public int delete(Long memberId, List<Long> ids) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(ids).andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(record, example);
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return productDao.getCartProduct(productId);
    }

    @Override
    public int updateAttr(OmsCartItem cartItem) {
        //删除原购物车信息
        OmsCartItem updateCart = new OmsCartItem();
        updateCart.setId(cartItem.getId());
        updateCart.setModifyDate(new Date());
        updateCart.setDeleteStatus(1);
        cartItemMapper.updateByPrimaryKeySelective(updateCart);
        cartItem.setId(null);
        add(cartItem);
        return 1;
    }

    @Override
    public int clear(Long memberId) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(record,example);
    }
}
