package com.kwb.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kwb.crm.base.BaseMapper;
import com.kwb.crm.base.BaseService;
import com.kwb.crm.dao.SaleChanceMapper;
import com.kwb.crm.enums.DevResult;
import com.kwb.crm.enums.StateStatus;
import com.kwb.crm.query.SaleChanceQuery;
import com.kwb.crm.utils.AssertUtil;
import com.kwb.crm.utils.PhoneUtil;
import com.kwb.crm.vo.Salechance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<Salechance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;


    /**
     * 刚进入页面就显示的数据
     *
     * 查询多条数据
     *      把所有的数据展示到前台页面
     *      多条件查询搜索
     * @param saleChanceQuery
     * @return
     */

    public Map<String,Object>  selectSaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map = new HashMap<String,Object>();
        //开启分页  需要传入显示的页数  和  每页显示的数据条数
       PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //为mapper中查询到的数据进行分页s
        PageInfo<Salechance> pageInfo1 = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        map.put("code",000);
        map.put("msg","success");
        map.put("count",pageInfo1.getTotal());//当前查询的总数量   getSize（）指的是当前页面显示的是几条数据
        map.put("data",pageInfo1.getList());//显示的表
        return map;
    }

    /**
     *
     * 添加操作

     * 营销机会数据添加
     *      1. 参数校验
     *         客户名  非空
     *         联系人  非空
     *         手机号码 非空 格式正确
     *      2. 设置参数的默认值
     *          创建时间 createDate  当前时间
     *          更新时间 updateDate  当前时间
     *          是否有效 isValid     1=有效
     *          分配人
     *              默认是未分配
     *                  分配状态    未分配  0-未分配  1-已分配
     *                  开发状态    未开发  0=未开发
     *                  分配时间    空
     *             如果设置分配人
     *                  分配状态    已分配
     *                  开发状态    开发中    0-未开发 1-开发中 2-开发成功 3-开发失败
     *                  分配时间    当前时间
     *      3. 执行添加操作
     *          如果失败，抛异常
     * @param salechance   需要传入的数据对象
     */
    @Transactional(propagation = Propagation.REQUIRED)//添加事务
    public void addSaleChance(Salechance salechance){
        //校验必填选项
        CheckData(salechance);
        //设置默认参数  未分配       设置响应的开发状态开发状态
        salechance.setState(StateStatus.UNSTATE.getType());//分配状态
        salechance.setDevResult(DevResult.UNDEV.getStatus());//开发状态  分配质检为null


        //如果分配人不为空isBlank（）是指为空
        if (StringUtils.isNotBlank(salechance.getAssignMan())){
             salechance.setState(StateStatus.STATED.getType());//设置分配状态
             salechance.setDevResult(DevResult.DEVING.getStatus());//设置来发状态
            salechance.setAssignTime(new Date());//设置分配时间
        }
        //设置事件
        salechance.setCreateDate(new Date());//创建时间
        salechance.setUpdateDate(new Date() );//更新时间

        //执行添加操作
        AssertUtil.isTrue(saleChanceMapper.insertSelective(salechance)<1,"添加失败");
    }

    /**
     * 非空校验
     *      客户名  非空
     *      联系人  非空
     *      手机号码 非空 格式正确
     * @param salechance
     */
    private void CheckData(Salechance salechance) {
        //等于空的话报错
        AssertUtil.isTrue(salechance.getCustomerName()==null,"客户名不能为空！！！");
        AssertUtil.isTrue(salechance.getLinkMan()==null,"联系人不能为空！！！");
        AssertUtil.isTrue(salechance.getLinkPhone()==null,"联系人号码不能为空！！！");
        //判断手机号码是否符合格式
        AssertUtil.isTrue(!PhoneUtil.isMobile(salechance.getLinkPhone()),"手机号码格式不正确");
    }


    /**
     * 传入一个SaleChance对象
     * 然后通过隐藏于传入id （包括id）
     *
     *
     *   1、参数校验
     *      id记录必须非空
     *      customerName 创建人不能为空
     *      linkMan  联系人不能为空
     *      linkPhone  联系电话非空  并且符合要求
     *   2、设置参数
     *      通过id查询记录
     *          为空  为空抛出异常
     *          非空  非空更给数据到数据库
     *                      表单提交的数据
     *                          判断是否之
     *      更新事件：系统当前时间


     */
    public void updateSalechance(Salechance salechance){
        //参数校验
        AssertUtil.isTrue(salechance.getId() == null , "更新失败！！！");
        //判断要更id查询的数据是否存在，
        Salechance temp = saleChanceMapper.selectByPrimaryKey(salechance.getId());
        AssertUtil.isTrue(temp==null,"待更新的数据不存在！！！");
        //参数的校验
        checkParams(salechance);

        //设置默认值
        salechance.setCreateDate(temp.getCreateDate());//创建时间
        salechance.setUpdateDate(new Date());//更新时间
        //设置分配时间
        salechance.setAssignTime(new Date());

        // 未指派 ————> 已指派
        if (StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(salechance.getAssignMan())){
            salechance.setState(StateStatus.STATED.getType());//分配状态
            salechance.setDevResult(DevResult.DEVING.getStatus());  //设置开发状态
            salechance.setAssignTime(new Date());
            // 已指派 ————> 未指派
        }else if(StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(salechance.getAssignMan())){
            salechance.setState(StateStatus.UNSTATE.getType());//分配状态
            salechance.setDevResult(DevResult.UNDEV.getStatus());  //设置开发状态
            //分配时间为null
            salechance.setAssignTime(null);
            //分配人为空
            salechance.setAssignMan("");
            // 已指派 ————> 已指派  需要判断 之前和更新的时候指派人都不为空  并且两次的指派人还不能一样
        }else if (StringUtils.isNotBlank(salechance.getAssignMan())
                && StringUtils.isNotBlank(temp.getAssignMan())
                && !(temp.getAssignMan()).equals(salechance.getAssignMan())){
            salechance.setAssignTime(new Date());
        }
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(salechance)<1,"更新失败！！！");

    }

    /**
     * 更新操纵的参数校验yi'zhi'pai
     * @param salechance
     */
    private void checkParams(Salechance salechance) {
        AssertUtil.isTrue(salechance.getCustomerName()==null,"用户名不能为空！！！");
        AssertUtil.isTrue(salechance.getLinkMan()==null,"联系人不能为空！！！");
        AssertUtil.isTrue(salechance.getLinkPhone()==null,"联系人号码不能威力那个");
        AssertUtil.isTrue(!PhoneUtil.isMobile(salechance.getLinkPhone()),"联系人号码格式不正确！！！");
    }

    /**
     * 查询所有销售名字
     * @return
     */
    public List<Map<String,Object>> queryAllSalences(){
        return saleChanceMapper.queryAllSales();
    }



    /**
     * 删除操作
     *  传入数组删除记录
     *
     */
    public void deleteSaleChance(Integer[] ids){

        AssertUtil.isTrue(ids == null, "待删除的记录不存在！");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) < 1, "营销机会数据删除失败！");

    }


}
