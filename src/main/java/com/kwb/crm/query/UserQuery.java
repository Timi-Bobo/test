package com.kwb.crm.query;

import com.kwb.crm.base.BaseQuery;

public class UserQuery extends BaseQuery {
    private String uname;
    private String email;
    private String phone;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
