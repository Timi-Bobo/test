package com.kwb.crm.dao;

import com.kwb.crm.base.BaseMapper;
import com.kwb.crm.vo.Salechance;

import java.util.List;
import java.util.Map;

public interface SaleChanceMapper extends BaseMapper<Salechance,Integer> {
    /**
     * 查询所有销售人员名字
     * @return
     */
    public List<Map<String, Object>> queryAllSales();
}