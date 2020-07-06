package com.kwb.crm.controller;

import com.kwb.crm.base.BaseController;
import com.kwb.crm.base.ResultInfo;
import com.kwb.crm.query.SaleChanceQuery;
import com.kwb.crm.service.SaleChanceService;
import com.kwb.crm.service.UserService;
import com.kwb.crm.utils.LoginUserUtil;
import com.kwb.crm.vo.Salechance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private  UserService userService;

    /**
     * 刚进入营销页面
     *
     * 返回视图的方式访问指定页面
     * @return
     */
    @RequestMapping("index")
    public ModelAndView SaleChance(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("saleChance/sale_chance");
        return modelAndView;
    }
    /**
     *刚进入页面所发出的请求
     *
     * 多条件查询 和 开发计划页面显示的查询
     *
     * 多条件查询数据
     * @param query
     * @return
     *
     *
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectSaleChanceByParam(SaleChanceQuery query ,Integer flag,HttpServletRequest request ){

        //判断 flag是否为空 空的话为多条件查询，否则是显示开发计划
        if (flag != null && flag == 1 ){
            //获取id
            //操作指派人为本次登陆账户的营销计划
            Integer id = LoginUserUtil.releaseUserIdFromCookie(request);//通过cookie获取到id
            //设置指派人
            query.setAssignMan(id);
            System.out.println(query.getDevReasult());
        }


        return saleChanceService.selectSaleChanceByParams(query);

    }




    /**
     * 添加操作
     *      需要获取cookie中的id查询数据库
     *          获取对应的真是名字
     *          设置创建人名字
     * @param salechance
     * @return
     */
    @PostMapping("addSaleChance")
    @ResponseBody
    public ResultInfo addSaleChance(Salechance salechance , HttpServletRequest request){
        System.out.println("进入后台。。。");
        //获取cookie中的id
        Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
        //根据id查询到真实名字
        String username = userService.selectByPrimaryKey(id).getUserName();
        //将真实名字设置到salechance当中
        salechance.setCreateMan(username);
        saleChanceService.addSaleChance(salechance);
        //BanseController中的一个方法，直接调用
        return success("添加成功！！！");
    }


    /**
     * 转发添加的视图
     *      更改 / 编辑的视图
     *
     *      查询到对象 设置到请求域
     *
     * @return
     */
    @RequestMapping("Forward")
    public String addOrUpdateSaleChancePage(Integer id , Model model){
        //判断id是否为空  非空的话查询数据  返回model前台来去
        if (id!=null && id!= 0){
            Salechance salechance = saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance",salechance);
        }
        return "saleChance/add_update";
    }

    /**
     * 更新数据
     */
    @PostMapping("updateSaleChance")
    @ResponseBody
    public ResultInfo uodateSaleChance(Salechance salechance , HttpServletRequest request){
            //获取cookie中的id
            Integer id = LoginUserUtil.releaseUserIdFromCookie(request);
            //根据id查询到真实名字
            String username = userService.selectByPrimaryKey(id).getUserName();
            //将真实名字设置到salechance当中
            salechance.setCreateMan(username);
            saleChanceService.updateSalechance(salechance);
            return success("更新成功！！！");
    }

    /**
     * 查询所有销售的名字
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return saleChanceService.queryAllSalences();

    }



    /**
     * 删除操作的后台操作
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){


        saleChanceService.deleteSaleChance(ids);

        return success("success");
    }
}
