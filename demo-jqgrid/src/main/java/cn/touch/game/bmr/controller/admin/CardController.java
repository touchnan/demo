package cn.touch.game.bmr.controller.admin;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.touch.game.bmr.serv.ICardService;
import cn.touch.game.controller.BmrController;
import cn.touch.game.dto.CardGive;
import cn.touch.game.entity.SaleCard;
import cn.touch.game.utils.RoleUtils;
import cn.touchin.page.Pagination;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2017/5/21.
 */
@Controller
@RequestMapping("/card")
public class CardController extends BmrController {
    @Autowired
    private ICardService cardService;
    
    @RequestMapping(value = "/cards", method = {RequestMethod.GET})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public String cards() throws Exception {
        return "card/cards";
    }

    @RequestMapping(value = "/listCards", method = {RequestMethod.POST,RequestMethod.GET})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object listCards(@ModelAttribute Pagination page) throws Exception {
        return cardService.findPage(me(),page);
    }

    @RequestMapping(value = "/saveCard", method = {RequestMethod.POST})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object saveCard(@ModelAttribute("id") String id, @ModelAttribute SaleCard card,@ModelAttribute("oper") String oper) throws Exception {
    	//oper:del,oper:edit,oper:add
    	cardService.save(card);
        return card;
    }
    
    @RequestMapping(value = "/deleteCard", method = {RequestMethod.POST})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object deleteCard(@ModelAttribute("id") List<Long> ids,@ModelAttribute("oper") String oper) throws Exception {
    	//oper:del,oper:edit,oper:add
    	if ("del".equals(oper)) {
    		cardService.deleteCard(ids);
    	} 
        return "ok";
    }
    
    
    @RequestMapping(value = "/storage", method = {RequestMethod.GET})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public String storage() throws Exception {
        return "card/storage";
    }
    
    @RequestMapping(value = "/listStoragers", method = {RequestMethod.POST,RequestMethod.GET})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object listStoragers(@ModelAttribute Pagination page) throws Exception {
        return cardService.findUserStoragePage(me(),page);
    }
    
    @RequestMapping(value = "/addCard", method = {RequestMethod.POST})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object saveCard(@ModelAttribute CardGive cardGive) throws Exception {
        cardService.addCard(me(), cardGive);
        return "ok";
    }
    
    
    @RequestMapping(value = "/report_detail/{proxtype}", method = {RequestMethod.GET})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public String reportDetail(Model model, @PathVariable("proxtype") int proxType) throws Exception {
    	model.addAttribute("proxy_type", proxType);
    	return "card/reportDetail";
    }
    
    @RequestMapping(value = "/report_list_detail/{proxtype}", method = {RequestMethod.POST})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object reportListDetail(@ModelAttribute Pagination page, @PathVariable("proxtype") int proxType) throws Exception {
    	return cardService.queryProxyCardExchangeDetail(me(),page, proxType);
    }
    
    @RequestMapping(value = "/report_stat/{proxtype}", method = {RequestMethod.GET})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public String reportStat(Model model, @PathVariable("proxtype") int proxType) throws Exception {
    	model.addAttribute("proxy_type", proxType);
        return "card/reportStat";
    }
    
    @RequestMapping(value = "/report_list_stat/{proxtype}", method = {RequestMethod.POST})
    @RequiresRoles(value={RoleUtils.ROLE_CODE_ADMIN})
    public @ResponseBody Object reportListStat(@ModelAttribute Pagination page, @PathVariable("proxtype") int proxType) throws Exception {
    	return cardService.queryProxyCardExchangeStat(me(),page, proxType);
    }
}
