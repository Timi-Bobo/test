package com.kwb.crm.Model;

/**
 * 返回前台的用户模型对象
 */
public class UserModel {
    private String userIdStr;//id
    private String userName;//账户
    private String userPwd;//密码

    public UserModel() {
    }

    public UserModel(String userIdStr, String userName, String userPwd) {
        this.userIdStr = userIdStr;
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userIdStr='" + userIdStr + '\'' +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                '}';
    }
}
