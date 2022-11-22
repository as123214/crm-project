package com.jgc.crm.web.controller;

import com.jgc.crm.commns.constent.Contents;
import com.jgc.crm.commns.domain.ReturnObject;
import com.jgc.crm.commns.utils.DateUtils;
import com.jgc.crm.commns.utils.UUIDUtils;
import com.jgc.crm.settings.transaction.model.User;
import com.jgc.crm.workbench.domain.ActivityRemark;
import com.jgc.crm.workbench.domain.ClueRemark;
import com.jgc.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    ActivityRemarkService activityRemarkService;
    @RequestMapping("/workbench/activity/saveActivityRemark.do")
    public @ResponseBody Object saveActivityRemark(ActivityRemark activityRemark, HttpSession session)throws Exception{
        User user=(User) session.getAttribute(Contents.SESSION_USER);
        //封装参数
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setCreateTime(DateUtils.formateTime(new Date()));
        activityRemark.setCreateBy(user.getId());
        activityRemark.setEditFlag(Contents.REMARK_EDIT_FLAG_NO_EDITED);
        //调用service层方法,创建市场活动备注
        ReturnObject ret = new ReturnObject();
        try{
            int i = activityRemarkService.saveActivityRemark(activityRemark);
            if (i!=0) {
                ret.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
                ret.setRetData(activityRemark);
            }else {
                ret.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                ret.setMessage("系统繁忙,稍后再试");
            }
        }catch (Exception e) {
            e.printStackTrace();
            ret.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
            ret.setMessage("系统繁忙,稍后再试");
        }
    return ret;
    }
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    public @ResponseBody Object deleteActivityRemarkById(String id){
        //调用service层方法，删除市场活动备注
        ReturnObject returnObject=new ReturnObject();
       try{
           int i = activityRemarkService.deleteActivityRemarkById(id);
            if(i!=0){
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙,请稍后再试");
            }
       }
       catch (Exception e){
           returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
           returnObject.setMessage("系统繁忙,请稍后再试");
       }
       return returnObject;
    }
    @RequestMapping("/workbench/activity/editActivityRemark.do")
    public @ResponseBody Object editActivityRemark(ActivityRemark activityRemark,HttpSession session)throws Exception{
        User user=(User) session.getAttribute(Contents.SESSION_USER);
        //封装参数
        activityRemark.setEditFlag(Contents.REMARK_EDIT_FLAG_YES_EDITED);
        activityRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        activityRemark.setEditBy(user.getId());
        //调用service层方法,修改市场活动备注
        ReturnObject returnObject=new ReturnObject();
        try {
            int i = activityRemarkService.editActivityRemark(activityRemark);
            if(i!=0){
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(activityRemark);
            }else {
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);

            returnObject.setMessage("系统忙,请稍后再试");

        }
        return returnObject;
    }

}
