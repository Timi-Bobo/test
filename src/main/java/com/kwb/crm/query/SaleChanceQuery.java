package com.kwb.crm.query;

import com.kwb.crm.base.BaseQuery;

/**
 * 查询类
 *      查询条件的封装
 */
public class SaleChanceQuery extends BaseQuery {

    private String customerName; // 客户名称
    private String createMan; // 创建⼈
    private String state; // 分配状态

    private  Integer assignMan;//指派人
    private  Integer devResult;//开发状态

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public SaleChanceQuery() {
    }

    public SaleChanceQuery(String customerName, String createMan, String state, Integer assignMan, Integer devResult) {
        this.customerName = customerName;
        this.createMan = createMan;
        this.state = state;
        this.assignMan = assignMan;
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public Integer getDevReasult() {
        return devResult;
    }

    public void setDevReasult(Integer devReasult) {
        this.devResult = devReasult;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SaleChanceQuery{" +
                "customerName='" + customerName + '\'' +
                ", createMan='" + createMan + '\'' +
                ", state='" + state + '\'' +
                ", assignMan=" + assignMan +
                ", devResult=" + devResult +
                '}';
    }
}
