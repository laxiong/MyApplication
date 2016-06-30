package com.laxiong.entity;

/**
 * Created by xiejin on 2016/6/28.
 * Types OrderInfo.java
 * 服务器反馈的数据
 */
public class OrderInfo {
    private String merchantId;
    private String receiveUrl;
    private String orderNo;
    private String orderAmount;
    private String orderDatetime;
    private String productName;
    private String cardNo;
    private String key;

    /**
     * * @param merchantId
     * @param receiveUrl 反馈的网址
     * @param orderNo 订单号
     * @param orderAmount 订单金额
     * @param orderDatetime 订单时间
     * @param productName 产品名称
     * @param cardNo 卡号
     * @param key 密钥
     */
    public OrderInfo(String merchantId, String receiveUrl, String orderNo, String orderAmount, String orderDatetime, String productName, String cardNo, String key) {
        this.merchantId = merchantId;
        this.receiveUrl = receiveUrl;
        this.orderNo = orderNo;
        this.orderAmount = orderAmount;
        this.orderDatetime = orderDatetime;
        this.productName = productName;
        this.cardNo = cardNo;
        this.key = key;
    }

    public OrderInfo() {
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getReceiveUrl() {
        return receiveUrl;
    }

    public void setReceiveUrl(String receiveUrl) {
        this.receiveUrl = receiveUrl;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderDatetime() {
        return orderDatetime;
    }

    public void setOrderDatetime(String orderDatetime) {
        this.orderDatetime = orderDatetime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
