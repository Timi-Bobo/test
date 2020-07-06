layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    /**
     * 监听表单的提交
     */
    form.on('submit(saveBtn)', function(data){
        console.log(data);


        /*对传入的密码进行判断*/
        /*发送ajax请求*/
        $.ajax({
            type:"post",
            url:ctx + "/user/updatePassword",
            data:{
                oldPwd:data.field.old_password,
                newPwd:data.field.new_password,
                configPwd:data.field.again_password
                },
            dataType:"json",
            success:function (result) {
                console.log("判断");
                if (result.code == 200){

                    layer.msg("用户修改密码成功，系统将于5秒后退出...")
                    //退出系统清除cookies
                    $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                    $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                    $.removeCookie("trueName",{domain:"localhost",path:"/crm"});

                    window.parent.location.href = ctx + "/index";
                }else{
                    layer.msg(data.msg);
                }
            }
        });




    });






});