/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.touch.game.bmr.controller.admin;

import cn.touch.game.bmr.serv.IUserService;
import cn.touch.game.controller.BmrController;
import cn.touch.game.entity.User;
import cn.touch.game.utils.RoleUtils;
import cn.touchin.page.Pagination;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on May 2, 2017.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BmrController {
    
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/users", method = {RequestMethod.GET})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public String users() throws Exception {
        return "user/users";
    }

    @RequestMapping(value = "/admin_prox1", method = {RequestMethod.POST,RequestMethod.GET})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object adminAndProx1() throws Exception {
        return userService.findAmdinAndProx1(me());
    }

    @RequestMapping(value = "/listUsers", method = {RequestMethod.POST,RequestMethod.GET})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object listUsers(@ModelAttribute Pagination page) throws Exception {
        return userService.findPage(me(),page);
    }

    @RequestMapping(value = "/saveUser", method = {RequestMethod.POST})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object saveUser(@ModelAttribute User user) throws Exception {
        userService.save(user);
        return user;
    }
    
    @RequestMapping(value = "/deleteUser", method = {RequestMethod.POST})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object deleteUser(@ModelAttribute("id") List<Long> ids,@ModelAttribute("oper") String oper) throws Exception {
    	//oper:del,oper:edit,oper:add
    	if ("del".equals(oper)) {
    		userService.deleteCard(ids);
    	} 
        return "ok";
    }

    @RequestMapping(value = "/msp", method = {RequestMethod.POST})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object msp(@ModelAttribute("passwd") String passwd, @ModelAttribute("newPasswd") String newPasswd) throws Exception {
        int count = userService.resetSelfPasswd(me(), passwd, passwd);
        return "ok";
    }
}
