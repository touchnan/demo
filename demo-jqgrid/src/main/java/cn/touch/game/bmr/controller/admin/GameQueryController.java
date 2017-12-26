package cn.touch.game.bmr.controller.admin;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.touch.game.bmr.serv.ICardService;
import cn.touch.game.controller.BmrController;
import cn.touch.game.utils.RoleUtils;
import cn.touchin.page.Pagination;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2017/5/19.
 */
@Controller
@RequestMapping("/query")
public class GameQueryController extends BmrController {
    @Autowired
    private ICardService cardService;

    @RequestMapping(value = "/query/{qtype}", method = {RequestMethod.GET, RequestMethod.POST})
    @RequiresRoles(value = {RoleUtils.ROLE_CODE_ADMIN})
    public String stat(@PathVariable("qtype") String qtype) throws Exception {
        return "query/" + qtype;
    }

    @RequestMapping(value = "/card_orders", method = {RequestMethod.POST})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object cardOrdres(@ModelAttribute Pagination page) throws Exception {
        return cardService.queryCardOrders(me(),page);
    }

}
