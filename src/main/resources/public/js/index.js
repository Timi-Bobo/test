layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    /**
     * 监听表单的提交
     */
    form.on('submit(login)', function(data){

        //data获取的是表单中的元素所以没有id
        console.log(data);
        console.log(data.field.userIdStr);
        /*获取到表单的元素*/
        var fieldData = data.field;
        if (fieldData.username == null || fieldData.username.trim() == ""){
            layer.msg("用户名不能为空！！");//弹出层
            return false;
        }
        if (null == fieldData.password || fieldData.password.trim() == ""){
            layer.msg("用户密码不能为空！！");/*弹出层，弹出提示信息*/
            return false;
        }


        //发送ajax
        $.ajax({
            type: "post",
            url: ctx + "/user/login",
            /*传入的参数*/
            data: {
                userName: fieldData.username,
                userPwd: fieldData.password
            },


            /*回调函数
            * 返回的是模型对象
            * */
            success: function (result) {
                console.log(result.result);
                /**
                 * 判断状态码  200登陆成功  跳转到index首页
                 *             其他登陆失败  跳转到登陆页面
                 */

                // 判断是否成功
                if (result.code != 200) {

                    /**
                     * 登录失败
                     *      设置提示信息
                     */
                    layer.msg(result.msg);
                } else {
                    /**
                     * 登录成功
                     * 判断记住我的多选框是否被选中
                     *      被选中，设定cookie的存活时间
                     */
                    var userInfo = result.result;
                    layer.msg("登录成功！", function () {
                        if ($("#rememberMe").prop("checked")){

                            $.cookie("userIdStr", userInfo.userIdStr,{expires:7});
                            $.cookie("userName", userInfo.userName,{expires:7});
                            $.cookie("trueName", userInfo.userPwd,{expires:7});

                        }else {
                            $.cookie("userIdStr", userInfo.userIdStr);
                            $.cookie("userName", userInfo.userName);
                            $.cookie("trueName", userInfo.userPwd);
                        }
                        console.log(userInfo.userIdStr);
                        // 登录成功后，跳转到首页
                        window.location.href = ctx + "/main";
                    });
                }


            }

        });

        return false; //判断之后，说明都不为空，但是依然需要阻止发送请求，阻止表单跳转。


    });






});