package com.pwc.portal.service.impl;

import com.pwc.model.OmsCartItem;
import com.pwc.model.PmsProductFullReduction;
import com.pwc.model.PmsProductLadder;
import com.pwc.model.PmsSkuStock;
import com.pwc.portal.dao.PortalProductDao;
import com.pwc.portal.domain.CartPromotionItem;
import com.pwc.portal.domain.PromotionProduct;
import com.pwc.portal.service.OmsPromotionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

//TODO
@Service
public class OmsPromotionServiceImpl implements OmsPromotionService {
    @Autowired
    private PortalProductDao portalProductDao;
    
    @Override
    public List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList) {
        List<Long> productIdList = new ArrayList<>();
        HashMap<Long, List<OmsCartItem>> listHashMap = new HashMap<>();
        for (OmsCartItem item : cartItemList) {
            productIdList.add(item.getProductId());
            List<OmsCartItem> itemList = listHashMap.get(item.getProductId());
            if(itemList == null){
                itemList = new ArrayList<>();
                itemList.add(item);
                listHashMap.put(item.getProductId(), itemList);
            }else{
                itemList.add(item);
            }
        }
        List<PromotionProduct> promotionProductList = portalProductDao.getPromotionProductList(productIdList);
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        for (Map.Entry<Long, List<OmsCartItem>> entry : listHashMap.entrySet()) {
            Long productId = entry.getKey();
            PromotionProduct promotionProduct = getPromotionProductById(productId, promotionProductList);
            List<OmsCartItem> itemList = entry.getValue();
            if(promotionProduct != null){
                Integer promotionType = promotionProduct.getPromotionType();
                if(promotionType == 1){
                    for (OmsCartItem item : itemList) {
                        CartPromotionItem cartPromotionItem = new CartPromotionItem();
                        BeanUtils.copyProperties(item,cartPromotionItem);
                        cartPromotionItem.setPromotionMessage("单品促销");
                        //商品原价-促销价
                        PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                        BigDecimal originalPrice = skuStock.getPrice();
                        //单品促销使用原价
                        cartPromotionItem.setPrice(originalPrice);
                        cartPromotionItem.setReduceAmount(originalPrice.subtract(skuStock.getPromotionPrice()));
                        cartPromotionItem.setRealStock(skuStock.getStock()-skuStock.getLockStock());
                        cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
                        cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
                        cartPromotionItemList.add(cartPromotionItem);
                    }
                }else if(promotionType == 3){
                    int count = getCartItemCount(itemList);
                    PmsProductLadder ladder = getProductLadder(count, promotionProduct.getProductLadderList());
                    if(ladder!=null){
                        for (OmsCartItem item : itemList) {
                            CartPromotionItem cartPromotionItem = new CartPromotionItem();
                            BeanUtils.copyProperties(item,cartPromotionItem);
                            String message = getLadderPromotionMessage(ladder);
                            cartPromotionItem.setPromotionMessage(message);
                            //商品原价-折扣*商品原价
                            PmsSkuStock skuStock = getOriginalPrice(promotionProduct,item.getProductSkuId());
                            BigDecimal originalPrice = skuStock.getPrice();
                            BigDecimal reduceAmount = originalPrice.subtract(ladder.getDiscount().multiply(originalPrice));
                            cartPromotionItem.setReduceAmount(reduceAmount);
                            cartPromotionItem.setRealStock(skuStock.getStock()-skuStock.getLockStock());
                            cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
                            cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
                            cartPromotionItemList.add(cartPromotionItem);
                        }
                    }else{
                        handleNoReduce(cartPromotionItemList,itemList,promotionProduct);
                    }
                }else if (promotionType == 4) {
                    //满减
                    BigDecimal totalAmount= getCartItemAmount(itemList,promotionProductList);
                    PmsProductFullReduction fullReduction = getProductFullReduction(totalAmount,promotionProduct.getProductFullReductionList());
                    if(fullReduction!=null){
                        for (OmsCartItem item : itemList) {
                            CartPromotionItem cartPromotionItem = new CartPromotionItem();
                            BeanUtils.copyProperties(item,cartPromotionItem);
                            String message = getFullReductionPromotionMessage(fullReduction);
                            cartPromotionItem.setPromotionMessage(message);
                            //(商品原价/总价)*满减金额
                            PmsSkuStock skuStock= getOriginalPrice(promotionProduct, item.getProductSkuId());
                            BigDecimal originalPrice = skuStock.getPrice();
                            BigDecimal reduceAmount = originalPrice.divide(totalAmount, RoundingMode.HALF_EVEN).multiply(fullReduction.getReducePrice());
                            cartPromotionItem.setReduceAmount(reduceAmount);
                            cartPromotionItem.setRealStock(skuStock.getStock()-skuStock.getLockStock());
                            cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
                            cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
                            cartPromotionItemList.add(cartPromotionItem);
                        }
                    }else {
                        handleNoReduce(cartPromotionItemList,itemList,promotionProduct);
                    }
                }
            }else {
                handleNoReduce(cartPromotionItemList,itemList,promotionProduct);
            }
        }
        return cartPromotionItemList;
    }

    private PromotionProduct getPromotionProductById(Long productId, List<PromotionProduct> promotionProductList) {
        for (PromotionProduct promotionProduct : promotionProductList) {
            if (productId.equals(promotionProduct.getId())) {
                return promotionProduct;
            }
        }
        return null;
    }

    private PmsSkuStock getOriginalPrice(PromotionProduct promotionProduct, Long productSkuId) {
        for (PmsSkuStock skuStock : promotionProduct.getSkuStockList()) {
            if (productSkuId.equals(skuStock.getId())) {
                return skuStock;
            }
        }
        return null;
    }

    private int getCartItemCount(List<OmsCartItem> itemList) {
        int count = 0;
        for (OmsCartItem item : itemList) {
            count += item.getQuantity();
        }
        return count;
    }

    private PmsProductLadder getProductLadder(int count, List<PmsProductLadder> productLadderList) {
        //按数量从大到小排序
        productLadderList.sort(new Comparator<PmsProductLadder>() {
            @Override
            public int compare(PmsProductLadder o1, PmsProductLadder o2) {
                return o2.getCount() - o1.getCount();
            }
        });
        for (PmsProductLadder productLadder : productLadderList) {
            if (count >= productLadder.getCount()) {
                return productLadder;
            }
        }
        return null;
    }

    private String getLadderPromotionMessage(PmsProductLadder ladder) {
        StringBuilder sb = new StringBuilder();
        sb.append("打折优惠：");
        sb.append("满");
        sb.append(ladder.getCount());
        sb.append("件，");
        sb.append("打");
        sb.append(ladder.getDiscount().multiply(new BigDecimal(10)));
        sb.append("折");
        return sb.toString();
    }

    private void handleNoReduce(List<CartPromotionItem> cartPromotionItemList, List<OmsCartItem> itemList,PromotionProduct promotionProduct) {
        for (OmsCartItem item : itemList) {
            CartPromotionItem cartPromotionItem = new CartPromotionItem();
            BeanUtils.copyProperties(item,cartPromotionItem);
            cartPromotionItem.setPromotionMessage("无优惠");
            cartPromotionItem.setReduceAmount(new BigDecimal(0));
            PmsSkuStock skuStock = getOriginalPrice(promotionProduct,item.getProductSkuId());
            if(skuStock!=null){
                cartPromotionItem.setRealStock(skuStock.getStock()-skuStock.getLockStock());
            }
            cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
            cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
            cartPromotionItemList.add(cartPromotionItem);
        }
    }
    
    private BigDecimal getCartItemAmount(List<OmsCartItem> itemList, List<PromotionProduct> promotionProductList) {
        BigDecimal amount = new BigDecimal(0);
        for (OmsCartItem item : itemList) {
            //计算出商品原价
            PromotionProduct promotionProduct = getPromotionProductById(item.getProductId(), promotionProductList);
            PmsSkuStock skuStock = getOriginalPrice(promotionProduct,item.getProductSkuId());
            amount = amount.add(skuStock.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return amount;
    }
    
    private PmsProductFullReduction getProductFullReduction(BigDecimal totalAmount,List<PmsProductFullReduction> fullReductionList) {
        //按条件从高到低排序
        fullReductionList.sort(new Comparator<PmsProductFullReduction>() {
            @Override
            public int compare(PmsProductFullReduction o1, PmsProductFullReduction o2) {
                return o2.getFullPrice().subtract(o1.getFullPrice()).intValue();
            }
        });
        for(PmsProductFullReduction fullReduction:fullReductionList){
            if(totalAmount.subtract(fullReduction.getFullPrice()).intValue()>=0){
                return fullReduction;
            }
        }
        return null;
    }

    private String getFullReductionPromotionMessage(PmsProductFullReduction fullReduction) {
        StringBuilder sb = new StringBuilder();
        sb.append("满减优惠：");
        sb.append("满");
        sb.append(fullReduction.getFullPrice());
        sb.append("元，");
        sb.append("减");
        sb.append(fullReduction.getReducePrice());
        sb.append("元");
        return sb.toString();
    }
}
