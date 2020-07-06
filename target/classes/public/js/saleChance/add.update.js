layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;





    /**
     * 监听表头
     * 其实也就是绑定表单  然后
     *
     * data  指的时表单数据
     */

    form.on('submit(addOrUpdateSaleChance)',function (data) {
        console.log(data);

        //提交数据时的加载层
        var index = layer.msg("数据提交中，请稍后。。。",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });
        //获取id
        var paramData = data.field;
        console.log(paramData);
        //添加操作 不许要id
        var url = ctx + "/sale_chance/addSaleChance";

        console.log("id："+paramData.id);

        if (paramData.id != null && paramData.id.trim() !="" ){
            //更新操作 需要id
            url = ctx + "/sale_chance/updateSaleChance";
        }

        $.post(url,paramData,function (result) {
                //判断是否关闭成功
            if (result.code==200){
                // 关闭加载层
                layer.close(index);
                // 提示用户成功
                layer.msg("操作成功！", {icon: 6});
                // 关闭所有的iframe层
                layer.closeAll("iframe");
                // 刷新父页面，重新渲染表格数据
                parent.location.reload();
            }else {
                layer.msg("操作失败",{icon:5});
            }
        });




        return false;
    });
    /**
     * 打开这个页面然后加载的时候发送ajax  提前把下拉框提交进去
     * 发送ajax请求，加载下拉框
     */
    $.ajax({
        type:"get",
        url: ctx + "/sale_chance/queryAllSales",
        data:{},
        success:function (data) {
            // console.log(data);
            for (var i = 0; i < data.length; i++) {
                // 判断指派人是否为空
                var saleChanceName = $("#saleChanceName").val();
                var opt;
                // 如果指派人不为空，且与当前循环到的选项值相等
                if (saleChanceName != null && saleChanceName == data[i].userId) {
                    opt = '<option value="'+data[i].userId+'" selected>'+data[i].userName+'</option>';
                } else {
                    opt = '<option value="'+data[i].userId+'">'+data[i].userName+'</option>';
                }
                // 将下拉选项追加到下拉框中
                $("#assignMan").append(opt);
            }
            // 重新渲染下拉框内容
            layui.form.render("select");
        }
    });





    /**
     * 绑定点击事件
     * 关闭弹出层  关闭当前添加才做的弹框
     */
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name)//获取到弹出层的索引
        parent.layer.close(index);//执行关闭
    });

});