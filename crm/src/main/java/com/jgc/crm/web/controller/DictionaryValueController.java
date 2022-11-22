package com.jgc.crm.web.controller;

import com.jgc.crm.service.UserService;
import com.jgc.crm.settings.transaction.model.User;
import com.jgc.crm.workbench.domain.DictionaryValue;
import com.jgc.crm.workbench.service.DictionaryValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class DictionaryValueController {
    @Autowired
    private DictionaryValueService dictionaryValueService;
    @Autowired
    private UserService userService;
    @RequestMapping("/workbench/clue/queryAllDicValue.do")
    public String queryAllDicValue(HttpServletRequest request){
        List<User> users = userService.queryUsers();
        List<DictionaryValue> appellation = dictionaryValueService.selectDictionaryValueById("appellation");
        List<DictionaryValue> clueState = dictionaryValueService.selectDictionaryValueById("clueState");
        List<DictionaryValue> source = dictionaryValueService.selectDictionaryValueById("source");
        request.setAttribute("users",users);
        request.setAttribute("appellation",appellation);
        request.setAttribute("clueState",clueState);
        request.setAttribute("source",source);
        return "workbench/clue/index";
    }
}
