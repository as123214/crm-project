package com.jgc.crm.web.controller;

import com.jgc.crm.commns.constent.Contents;
import com.jgc.crm.commns.domain.ReturnObject;
import com.jgc.crm.commns.utils.DateUtils;
import com.jgc.crm.commns.utils.HSSFUtils;
import com.jgc.crm.commns.utils.UUIDUtils;
import com.jgc.crm.service.UserService;
import com.jgc.crm.settings.transaction.model.User;
import com.jgc.crm.workbench.domain.Activity;
import com.jgc.crm.workbench.domain.ActivityRemark;
import com.jgc.crm.workbench.service.ActivityRemarkService;
import com.jgc.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemark;
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.queryUsers();
        request.setAttribute("users",users);

        return "workbench/activity/index";
    }

    /**
     * 创建市场活动
     */
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody Object saveCreateActivity(Activity activity, HttpSession session){
        System.out.println(activity);

        User user = (User) session.getAttribute(Contents.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        activity.setCreateTime(DateUtils.formateTime(new Date()));
        ReturnObject returnObject=new ReturnObject();

        try{
            //调用service层方法，保存市场活动
            int i = activityService.saveCreateActivity(activity);

            if(i>0){
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后再试。。。");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnObject;
    }

    /**
     * 根据条件查询市场活动
     */
    @RequestMapping(value = {"/workbench/activity/queryActivity.do"},method = RequestMethod.POST)
    public @ResponseBody Object queryActivityByCondition(String name,String owner,String startDate,String endDate,Integer pageSize,Integer pageNo){
        System.out.println(pageNo);
        System.out.println(pageSize);
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageSize",pageSize);
        map.put("beginNo",(pageNo-1)*pageSize);
        System.out.println((pageNo-1)*pageSize);
        //调用service层方法,获取查询结果
        List<Activity> activities = activityService.queryActivityByConditionFromPageNo(map);
        int  totalRows= activityService.selectCountOfActivityCondition(map);
        //封装返回结果
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("activityList",activities);
        resMap.put("totalRows",totalRows);
        return resMap;


    }
    /**
     * 根据id值删除市场活动
     */
    @RequestMapping(value = {"/workbench/activity/deleteActivity.do"},method = RequestMethod.POST)
    public @ResponseBody Object deleteActivity(String[] id) throws Exception{
        ReturnObject returnObject=new ReturnObject();
        System.out.println("删除方法");
        //调用service层方法删除数据
        try {
            int ret = activityService.deleteActivityByIds(id);

            System.out.println("try");
            if (ret!=0){
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("catch");

        }
        return returnObject;
    }
    @RequestMapping("/workbench/activity/queryActivityById.do")
    public @ResponseBody Object queryActivityById(String id){
        //调用service层方法查询市场活动
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }
    @RequestMapping(value = {"/workbench/activity/saveEditActivity.do"},method = RequestMethod.POST)
    public @ResponseBody Object saveEditActivity(Activity activity,HttpSession session){
        //从session作用域中获取登录用户
        User user = (User) session.getAttribute(Contents.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        activity.setCreateBy(user.getId());
        //调用service层方法,获取更新结果
        ReturnObject returnObject = new ReturnObject();
        try{
            int i = activityService.saveEditActivity(activity);
            if(i!=0){
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后再试");
        }

    return returnObject;
    }

    @RequestMapping("/workbench/activity/faileDownload.do")
    public void faileDownload(HttpServletResponse response) throws Exception{
        //设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //获取响应流
        OutputStream op=response.getOutputStream();
        //设置响应头
        response.addHeader("Content-Disposition","attachment;filename=Activity.xls");
        //获取所有市场活动
        List<Activity> activityList=activityService.queryAllActivity();
        Activity activity=null;


        //使用apache-poi将数据生成excel文件
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("市场活动");
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("id");
        cell=row.createCell(1);
        cell.setCellValue("owner");
        cell=row.createCell(2);
        cell.setCellValue("name");
        cell=row.createCell(3);
        cell.setCellValue("start_date");
        cell=row.createCell(4);
        cell.setCellValue("end_date");
        cell=row.createCell(5);
        cell.setCellValue("cost");
        cell=row.createCell(6);
        cell.setCellValue("description");
        cell=row.createCell(7);
        cell.setCellValue("create_time");
        cell=row.createCell(8);
        cell.setCellValue("create_by");
        cell=row.createCell(9);
        cell.setCellValue("edit_time");
        cell=row.createCell(10);
        cell.setCellValue("edit_by");
        //判断条件，市场活动不为空，市场活动个数大于0
        if(activityList!=null && activityList.size()>0){
            for(int i=0;i<activityList.size();i++){
                activity=activityList.get(i);
                row=sheet.createRow(i+1);
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell=row.createCell(2);
                cell.setCellValue(activity.getName());
                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }

            wb.write(op);
            wb.close();
            op.flush();
        }
    }
    @RequestMapping(value = "/workbench/activity/queryActivityByIds.do")
    public void queryActivityByIds(String[] id,HttpServletResponse response) throws Exception{
        //调用service层方法，根据传递过的id查询市场活动
        List<Activity> activityList = activityService.queryActivityByIds(id);
        //设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //获取响应流
        OutputStream op=response.getOutputStream();
        //设置响应头
        response.addHeader("Content-Disposition","attachment;filename=Activity.xls");
        //利用apache-poi创建execl文件并返回给浏览器
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("市场活动");
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("id");
        cell=row.createCell(1);
        cell.setCellValue("owner");
        cell=row.createCell(2);
        cell.setCellValue("name");
        cell=row.createCell(3);
        cell.setCellValue("start_date");
        cell=row.createCell(4);
        cell.setCellValue("end_date");
        cell=row.createCell(5);
        cell.setCellValue("cost");
        cell=row.createCell(6);
        cell.setCellValue("description");
        cell=row.createCell(7);
        cell.setCellValue("create_time");
        cell=row.createCell(8);
        cell.setCellValue("create_by");
        cell=row.createCell(9);
        cell.setCellValue("edit_time");
        cell=row.createCell(10);
        cell.setCellValue("edit_by");
        Activity activity=null;
        if(activityList!=null && activityList.size()>0){
            for(int i=0;i<activityList.size();i++){
                activity=activityList.get(i);
                row=sheet.createRow(i+1);
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell=row.createCell(2);
                cell.setCellValue(activity.getName());
                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
            wb.write(op);
            wb.close();
            op.flush();
         }
    }
    @RequestMapping(value="/workbench/activity/saveActivityByFile.do",method = RequestMethod.POST)
    public @ResponseBody Object saveActivityByFile(MultipartFile myfile,HttpSession session)throws Exception{
        User user=(User) session.getAttribute(Contents.SESSION_USER);

        //使用apache-poi解析文件
        InputStream is=myfile.getInputStream();
        HSSFWorkbook wb=new HSSFWorkbook(is);
        HSSFSheet sheet=wb.getSheetAt(0);
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell=row.getCell(0);
        List<Activity> activityList=new ArrayList<>();
        String uuId="";
        for(int i=1;i<=sheet.getLastRowNum();i++){
            Activity activity=new Activity();
            activity.setOwner(user.getName());
            activity.setCreateBy(user.getId());
            activity.setCreateTime(DateUtils.formateTime(new Date()));
            activity.setId(UUIDUtils.getUUID());
            uuId=UUIDUtils.getUUID();
            activity.setId(uuId);
            System.out.println(uuId);
            row=sheet.getRow(i);
            for(int j=0;j<row.getLastCellNum();j++) {
                cell = row.getCell(j);
                String cellValue = HSSFUtils.getCellValue(cell);
                if(j==0){
                    activity.setName(HSSFUtils.getCellValue(cell));
                }else if(j==1){
                    activity.setStartDate(HSSFUtils.getCellValue(cell));
                }else if(j==2){
                    activity.setEndDate(HSSFUtils.getCellValue(cell));
                }else if(j==3){
                    activity.setCost(HSSFUtils.getCellValue(cell));
                }else if(j==4){
                    activity.setDescription(HSSFUtils.getCellValue(cell));
                }

            }
            System.out.println(activity);
            activityList.add(activity);
        }
        System.out.println(activityList);
        ReturnObject ret=new ReturnObject();
        try {
            int i = activityService.saveActivityByFile(activityList);
            ret.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);
            ret.setRetData(i);
        }catch (Exception e){
            e.printStackTrace();
            ret.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
            ret.setMessage("系统繁忙");
        }
        return ret;
    }
    @RequestMapping("/workbench/activity/queryActivityForDetail.do")
    public String queryActivityForDetail(String id,HttpServletRequest request){
        //调用service层方法，查询市场活动详细信息
        List<ActivityRemark> activityRemarks = activityRemark.queryActivityRemarksByActivityId(id);
        Activity activity = activityService.queryActivityForDetailById(id);
        request.setAttribute("activity",activity);
        request.setAttribute("activityRemarks",activityRemarks);
        System.out.println(activityRemarks);
        return "workbench/activity/detail";
    }

    }
