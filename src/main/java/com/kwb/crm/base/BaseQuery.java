package com.kwb.crm.base;

/**
 * 分页所需要的参数   ，
 */
public class BaseQuery {
    private Integer page=1;//显示第几页
    private Integer limit=1;//每页显示几条

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
