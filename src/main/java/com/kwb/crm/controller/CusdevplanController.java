package com.kwb.crm.controller;

import com.kwb.crm.base.BaseController;
import com.kwb.crm.service.SaleChanceService;
import com.kwb.crm.vo.Salechance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("cus_dev_plan")
public class CusdevplanController extends BaseController {

    private SaleChanceService saleChanceService;

    /**
     * 进入主页面  开发计划页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "CusDevPlan/cus_dev_plan";
    }

    /**
     * 访问弹出层
     * 返回计划修改的页面
     * @return todo=设置到与对象当中，但是前台获取不到对象，并且页面直接就没了
     *
     * 是因为前台没有设置隐藏于
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String EdiPage(Integer sId ,Model model){
        System.out.println("传入id："+sId);
        // 通过ID查询营销机会数据
        if (sId != null) {
            Salechance saleChance = saleChanceService.selectByPrimaryKey(sId);
            // 设置请求域
            model.addAttribute("saleChance",saleChance);
        }

        return "cusDevPlan/cus_dev_plan_data";
    }






}
