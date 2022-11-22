package com.jgc.crm.web.controller;

import com.jgc.crm.commns.constent.Contents;
import com.jgc.crm.commns.domain.ReturnObject;
import com.jgc.crm.commns.utils.DateUtils;
import com.jgc.crm.commns.utils.UUIDUtils;
import com.jgc.crm.settings.transaction.model.User;
import com.jgc.crm.workbench.domain.*;
import com.jgc.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    @Autowired
    private DictionaryValueService dictionaryValueService;
    @RequestMapping("/workbench/clue/saveClue.do")
    public @ResponseBody Object saveClue(Clue clue, HttpSession session){
        //封装参数
       User user=(User) session.getAttribute(Contents.SESSION_USER);
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formateTime(new Date()));
        clue.setId(UUIDUtils.getUUID());
        //调用service层方法，创建线索
        ReturnObject returnObject=new ReturnObject();
        try{
            int i=clueService.saveClue(clue);
            if(i!=0){
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后再试！");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后再试！");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/queryAllClue.do")
    public @ResponseBody Object queryAllClue(Clue clue,Integer pageSize,Integer pageNo){
    //调用service层方法,根据条件查询线索和总条数
        Integer beginNo=(pageNo-1)*pageSize;
        Map<String,Object> map=new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("beginNo",beginNo);
        map.put("clue",clue);
        List<Clue> clues = clueService.queryClueByCondition(map);
        int totaoRows = clueService.queryTotaoRows();
        //封装返回参数
        Map<String,Object> retMap=new HashMap<>();
        retMap.put("clues",clues);
        retMap.put("totalRows",totaoRows);
        return retMap;
    }
    @RequestMapping("/workbench/clue/toClueDetail.do")
    public String toClueDetail(String id, HttpServletRequest request){
        //调用service层方法，查询线索明细、线索备注及与线索相关联市场活动
        Clue clue = clueService.queryClueById(id);
        List<ClueRemark> clueRemarks = clueRemarkService.queryAllClueRemarkByClueId(id);
        List<Activity> activityList = activityService.queryActivityForClueId(id);
        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarks",clueRemarks);
        request.setAttribute("activityList",activityList);
        return "workbench/clue/detail";

    }
    @RequestMapping("/workbench/clue/saveClueRemark.do")
    public @ResponseBody Object saveClueRemark(ClueRemark clueRemark)throws Exception{
        //封装参数
        clueRemark.setCreateTime(DateUtils.formateTime(new Date()));
        clueRemark.setId(UUIDUtils.getUUID());
        clueRemark.setEditFlag(Contents.REMARK_EDIT_FLAG_NO_EDITED);
        //调用service层方法，保存线索备注
        ReturnObject ret = new ReturnObject();
        try{
            int i = clueRemarkService.saveClueRemark(clueRemark);
            if(i!=0){
                ret.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
                ret.setRetData(clueRemark);
            }else {
                ret.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                ret.setMessage("系统忙,请稍后再试");
            }
        }catch (Exception e){
            ret.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
            ret.setMessage("系统忙,请稍后再试");
        }
        return ret;
    }
    @RequestMapping("/workbench/clue/deleteClurRemark.do")
    public @ResponseBody Object deleteClurRemark(String id)throws Exception{
        //调用service层方法，删除线索备注
        ReturnObject returnObject = new ReturnObject();
        try{
            int i=clueRemarkService.deleteClueRemarkById(id);
            if (i!=0){
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后再试");
            }
        }catch (Exception e){
            returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后再试");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/queryActivityForDetail.do")
    public @ResponseBody Object queryActivityForDetail(String clueId,String activityName){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        //调用service层方法，查询线索未关联市场活动
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);

        return activityList;

    }
    @RequestMapping("/workbench/clue/saveClueRelation.do")
    public @ResponseBody Object saveClueRelation(String[] id,String clueId)throws Exception{
        ClueActivityRelation clueActivityRelation =new ClueActivityRelation();
        List<ClueActivityRelation> clueActivityRelations =new ArrayList<>();
        System.out.println(id);
        System.out.println(clueId);
        //封装参数
        for (String aid:id){
            clueActivityRelation.setActivityId(aid);
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setId(UUIDUtils.getUUID());
            clueActivityRelations.add(clueActivityRelation);
        }
        //调用servic层方法，关联市场活动、获取关联的市场活动
        ReturnObject ret=new ReturnObject();
        try {
            int i = clueActivityRelationService.saveClueRelation(clueActivityRelations);
            if (i>0){
                //获取关联的市场活动
                List<Activity> activityList = activityService.queryActivityFroDetailByIds(id);
                ret.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
                ret.setRetData(activityList);
            }else {
                ret.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                ret.setMessage("系统忙,请稍后再试");
            }
        }catch (Exception e){
            ret.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
            ret.setMessage("系统忙,请稍后再试");
        }
        return ret;
    }
    @RequestMapping("/workbench/clue/saveUnbund.do")
    public @ResponseBody Object saveUnbund(ClueActivityRelation clueActivityRelation){
        //调用service层方法，删除市场活动关联
        ReturnObject returnObject = new ReturnObject();
        try{
            int ret=clueActivityRelationService.deleteClueRelation(clueActivityRelation);
            if(ret>0){
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后再试！");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后再试！");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        //调用servic层方法查询显示明细、阶段字典值
        Clue clue = clueService.queryClueById(id);
        List<DictionaryValue> stage = dictionaryValueService.selectDictionaryValueById("stage");
        //将数据存入request作用域
        request.setAttribute("clue",clue);
        request.setAttribute("stage",stage);
        return "workbench/clue/convert";
    }
}
