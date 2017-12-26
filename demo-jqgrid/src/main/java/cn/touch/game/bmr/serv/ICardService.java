package cn.touch.game.bmr.serv;

import java.util.List;

import cn.touch.game.dto.CardGive;
import cn.touch.game.dto.UserMgr;
import cn.touch.game.entity.SaleCard;
import cn.touchin.page.Page;
import cn.touchin.page.Pagination;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on 2017/5/8.
 */
public interface ICardService {
    /**
     * 给一级代理加卡
     *
     * @param me
     * @param cardGive
     */
    void addCard(UserMgr me, CardGive cardGive);

    public Page findPage(UserMgr me, Pagination page);

    SaleCard save(SaleCard card);

	/**
	 * @param me
	 * @param page
	 * @return
	 */
    Pagination findUserStoragePage(UserMgr me, Pagination page);

	/**
	 * @param me
	 * @param page
	 * @param proxType TODO
	 * @return
	 */
	Pagination queryProxyCardExchangeDetail(UserMgr me, Pagination page, int proxType);

	/**
	 * @param me
	 * @param page
	 * @param proxType TODO
	 * @return
	 */
	Pagination queryProxyCardExchangeStat(UserMgr me, Pagination page, int proxType);

	Pagination queryCardOrders(UserMgr me, Pagination page);

	/**
	 * @param ids
	 */
	int deleteCard(List<Long> ids);
}
