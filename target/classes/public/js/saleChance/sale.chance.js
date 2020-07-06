layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 营销机会管理页面的js
     */



    /**
     * 营销机会列表展示
     *
     *      监视提交的数据
     *              绑定后台查询javaBean对象与表单数据
     *
     *              render  重新渲染
     */
    var  tableIns = table.render({
        elem: '#saleChanceList', // 表格绑定的ID
        url : ctx + '/sale_chance/list', // 访问数据的地址
        cellMinWidth : 95, //单元格最小的宽度
        page : true, // 开启分页
        height : "full-125",//高度
        limits : [1,10,15,20,25],//每页显示的数量
        limit : 10,//默认显示的个数
        toolbar: "#toolbarDemo",//绑定工具栏  也就是操作和删除两个按钮
        id : "saleChanceListTable",//数据表格的id
        //type:列的类型  field：与数据库字段保持一致  title：在表格最上面显示的类型  给用户看
                        //align：显示位置是否是居中   templet：数据库查询的数据不是我们想要的，添加一个函数进行判断一下
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},

            //这边直接设置这个uname显示不出来的  与javabean的字段名字不一样  然而因为数据库中saleChance的表中字段只有user表的id所以需要连表查询
            {field: 'uname', title: '指派人', align:'center'},

            {field: 'assignTime', title: '分配时间', align:'center'},
            {field: 'state', title: '分配状态', align:'center',templet:function(d){
                    return formatterState(d.state);
            }},
            {field: 'devResult', title: '开发状态', align:'center',templet:function (d) {
                    return formatterDevResult(d.devResult);
            }},
            {title: '操作', templet:'#saleChanceListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    /**
     * 格式化分配状态
     *  0 - 未分配
     *  1 - 已分配
     *  其他 - 未知
     * @param state
     * @returns {string}
     */
    function formatterState(state){
        if(state==0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if(state==1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态
     *  0 - 未开发
     *  1 - 开发中
     *  2 - 开发成功
     *  3 - 开发失败
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value){
        if(value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if(value==1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if(value==2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if(value==3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }

    }

    /**
     * 多条件查询
     *      使用jquery方式绑定点击时间然后传入请求到后台
     */
    $(".search_btn").click(function () {
        //alert(1);
        //表格重载事件  reload  重新加载
        tableIns.reload({
            where:{
                //传入后台的参数自动绑定到SaleChanceQuery对象
                customerName:$("[name='customerName']").val(),// 客户名称
                createMan:$("[name='createMan']").val(), // 创建⼈
                state:$("#state").val(),//分配状态  前端是一个下拉框  指点.val（）即可获取选中的值
            },
            page:{
                curr: 1 //重新从第 1 页开始
            }
        })
    });




    /**
     *
     * 监听头部工具栏
     *
     * 绑定点击时间，弹出添加的页面
     *      数据表格的头部工具栏
     *      监听表头添加工具栏
     *
     *
     *      data：包含了表格的所有信息，保护分页等信息
     */
    table.on('toolbar(saleChances)', function (data) {
        var event = data.event;

        var checkStatus = table.checkStatus(data.config.id);//选中的id 数组  也可以判断是否全选
        // 判断用户行为
        switch (event) {
            case "add":
                // 营销机会添加操作 打开对话框
                openAddOrUpdateSaleChanceDialog();
                break;
            case"del":
                //删除营销机会记录  传入id  和数组
                console.log(checkStatus);  //包含数组和一些其他信息  对象中的data中为id的数组
                openAddOrdeleteSaleChanceDialog(checkStatus.data);
                break;
        }
    });

    /**
     * 行监听事件
     */
    table.on('tool(saleChances)',function (data) {
        var event = data.event;
        //获取当行的数据
        var rowData = data.data;
        //得到id
        var dataid = rowData.id;
        console.log(dataid);

        //判断事件类型
        if (event =="edit"){
            //更新操作
            openAddOrUpdateSaleChanceDialog(dataid);

        }else if (event =="del"){
            //删除操作
            layer.confirm("你确定要删除选中的记录吗？",{
                btn:["确认","取消"]
            },function (index) {

                $.ajax({
                    type:"post",
                    url:ctx + "/sale_chance/delete",
                    data:{
                        ids:dataid
                    },
                    success:function (result) {
                        if (result.code != 200) {
                            layer.msg(result.msg, {icon: 5});
                        } else {
                            layer.msg("营销机会数据删除成功！", {icon: 6});
                            // 加载表格
                            tableIns.reload();
                        }
                    }
                });


            })

        }
    });

    /**
     * 删除操作
     *  点击弹框的确认之后
     */
    //删除按钮点击后没有走数组的长度判断
    function openAddOrdeleteSaleChanceDialog(data) {
        //判断是否要删除记录
        console.log("数组长度:"+data.length);
        //判断有无选择数据
        if (data == null ||  data.length < 1){
            layer.msg("请选择您要删除的记录！！！",{icon: 5})
            return;
        }
        //弹出层  判断是否确认删除
        layer.confirm("您确定要删除选中的记录吗",{
            btn:["确定","取消"],
        },function (index) {
            //关闭确认栏  不管确认还是取消都要关闭弹出框
            layer.close(index);

            var ids = "ids=";
            // 遍历数组  遍历数组  要判断是不是最后一个数组，最后一个需要手动拼上去
            for (var i = 0; i < data.length; i++) {

                var id = data[i].id;

                // 判断是否是最后一个元素
                if (i == data.length-1) {
                    ids += id;
                } else {
                    ids += id + "&ids=";
                }
            }
            console.log(ids);
            /**
             *  把拼接好的字符串发送到后台
             */
            $.ajax({
                type:"post",
                url:ctx + "/sale_chance/delete",
                data:ids, // 传递的参数是数组
                success:function (result) {
                    if (result.code != 200) {
                        layer.msg(result.msg, {icon: 5});
                    } else {
                        layer.msg("营销机会数据删除成功！", {icon: 6});
                        // 加载表格
                        tableIns.reload();
                    }
                }
            });
        })

    }


    /**
     * 头监听打开的页面和行监听编辑的页面是一样的
     * 打开 添加/修改 营销机会的页面
     */
    function openAddOrUpdateSaleChanceDialog(dataid) {
        var title = "<h2>营销机会管理-机会添加</h2>";
        var url = ctx + "/sale_chance/Forward";//数据添加的视图页面
        //修改需要传入id  null & 空字符串  往boolean转都是true
        if (dataid){
            title = "<h2>营销机会管理-机会更新</h2>";//更i性能需要前台通过隐藏域传入id
            url = url + "?id=" + dataid;//给如果时更新操作的时候个url重新赋值  把id传进去

        }

        layui.layer.open({
            title:title,//标题
            type: 2,//弹出层类型
            content: url,//加载路径
            area:["500px","620px"],
            maxmin:true
        })
    }
});
