package com.laxiong.entity;

/**
 * Created by WangK on 2016/4/11.
 */
public class FinanceInfo {
    /***
     * 理财产品的类别信息
     */
    private String  title ;  //标题
    private int  amount;//剩余
    private int  id ;
    private int  max ;//最大投资额
    private Double  apr ;//年化
    private String  paytype;//随时提现
    private int  limit ; //日期
    private int  type ;//类型
    private int  bird;//是否新手标 1为新手标
    private Double  vip;//vip加息
    private Double  birdapr;//新手标加息量
    private String  date;//时间
    private Double  percent;//百分比
    private Double  present ;
    private Double  accum ;
    private String  rule;
    private int  members;
    private String  remark;
    private String  content;
    private String  min;
    private int  total_menber;
    private String  url;
    private String  key;
    private int  score;
    private int  total_amount;

    public Double getPresent() {
        return present;
    }

    public void setPresent(Double present) {
        this.present = present;
    }

    public Double getAccum() {
        return accum;
    }

    public void setAccum(Double accum) {
        this.accum = accum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Double getApr() {
        return apr;
    }

    public void setApr(Double apr) {
        this.apr = apr;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBird() {
        return bird;
    }

    public void setBird(int bird) {
        this.bird = bird;
    }

    public Double getVip() {
        return vip;
    }

    public void setVip(Double vip) {
        this.vip = vip;
    }

    public Double getBirdapr() {
        return birdapr;
    }

    public void setBirdapr(Double birdapr) {
        this.birdapr = birdapr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public int getTotal_menber() {
        return total_menber;
    }

    public void setTotal_menber(int total_menber) {
        this.total_menber = total_menber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }
}
