package com.jgc.crm.settings.web.controller;

import com.jgc.crm.commns.constent.Contents;
import com.jgc.crm.commns.domain.ReturnObject;
import com.jgc.crm.commns.utils.DateUtils;
import com.jgc.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jgc.crm.settings.transaction.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
//        src/main/webapp/WEB-INF/pages/settings/qx/user/login.jsp
        //转发到登录界面
        return "settings/qx/user/login" ;
    }

    @Autowired
private ReturnObject returnObject;
    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpSession session, HttpServletResponse response){
    //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        System.out.println(1111);
        //调用service层方法,查询用户
        User user=userService.querySelectByLoginActAndLoginPwd(map);
        System.out.println(user);
        //根据查询结果,生成响应信息
        if(user==null){
            //登录失败,用户名或密码错误
            returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        }else {
            String nowSte= DateUtils.formateDateTime(new Date());
            if(nowSte.compareTo(user.getExpireTime())>0){
                //登录失败,账号已过期
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if("0".equals(user.getLockState())) {
                //登录失败,状态被锁定
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号状态被锁定");
            }else if(user.getAllowIps().contains(request.getRemoteAddr())){
                System.out.println(request.getRemoteAddr());
                System.out.println(user.getAllowIps());
                System.out.println();
                //登录失败,ip地址受限
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("用户ip地址被限制");
            }else{
                //登录成功
                returnObject.setCode(Contents.RETURN_OBJECT_CODE_SUCCESS);

                //在session中存入user
                session.setAttribute(Contents.SESSION_USER,user);

                //如果要记住密码，就往外写cookie

               if ("true".equals(isRemPwd)){
                   Cookie c1=new Cookie("loginAct",loginAct);
                   Cookie c2=new Cookie("loginPwd",loginPwd);
                   c1.setMaxAge(10*24*60*60);
                   response.addCookie(c1);


                   c1.setMaxAge(10*24*60*60);
                   response.addCookie(c2);
               }else {
                   Cookie c1=new Cookie("loginAct","1");
                   Cookie c2=new Cookie("loginPwd","1");
                   c1.setMaxAge(0);
                   c2.setMaxAge(0);
               }



            }
        }

    return returnObject;}
    @RequestMapping("/settings/qx/logout.do")
    public String logOut(HttpServletResponse response,HttpSession session){

        //销毁cookie
        Cookie c1=new Cookie("loginAct","1");
        Cookie c2=new Cookie("loginPwd","1");
        c1.setMaxAge(0);
        c2.setMaxAge(0);
        response.addCookie(c1);
        response.addCookie(c2);
        //清除session
        session.invalidate();
        //跳转主页面
        return "redirect:/";
    }
}
