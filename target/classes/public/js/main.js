layui.use(['element', 'layer', 'layuimini','jquery','jquery_cookie'], function () {
    var $ = layui.jquery,
        layer = layui.layer,
        $ = layui.jquery_cookie($);
    /**
     * 因为退出登陆时主页面的元素所以
     * 需要在这个页面的js里边进行操作
     */
    // 菜单初始化
    $('#layuiminiHomeTabIframe').html('<iframe width="100%" height="100%" frameborder="0"  src="welcome"></iframe>')

        $(".login-out").click(function () {
            alert("1");
            $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
            $.removeCookie("userName",{domain:"localhost",path:"/crm"});
            $.removeCookie("trueName",{domain:"localhost",path:"/crm"});

            window.parent.location.href = ctx+"/index";
        })




});