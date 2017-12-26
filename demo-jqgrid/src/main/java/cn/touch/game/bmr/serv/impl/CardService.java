/**
 * 
 */
package cn.touch.game.bmr.serv.impl;

import cn.touch.db.Db;
import cn.touch.game.bmr.db.ICardDao;
import cn.touch.game.bmr.db.IUserDao;
import cn.touch.game.bmr.serv.ICardService;
import cn.touch.game.dto.CardGive;
import cn.touch.game.dto.UserMgr;
import cn.touch.game.entity.*;
import cn.touch.game.utils.BusiUtils;
import cn.touch.game.utils.RoleUtils;
import cn.touchin.db.jdbc.ISQLParser;
import cn.touchin.db.jdbc.PageQuery;
import cn.touchin.db.jdbc.SQLRunner;
import cn.touchin.page.Page;
import cn.touchin.page.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on May 19,
 * 2017.
 */
@Service
@Transactional(readOnly = true)
public class CardService implements ICardService {
	
    @Autowired
    private IUserDao userDao;

    @Autowired
    private ICardDao cardDao;
    
    @Autowired
    protected Db db;

    @Autowired
    protected ISQLParser sqlParser;

	/* (non-Javadoc)
	 * @see cn.touch.game.bmr.serv.ICardService#addCard(cn.touch.game.dto.UserMgr, cn.touch.game.dto.CardGive)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addCard(UserMgr me, CardGive cardGive) {
	       Assert.notNull(cardGive, "加卡信息不能为空");
	        Assert.isTrue(StringUtils.isNotBlank(cardGive.getAccount()), "加卡账号ID为空");
	        //Assert.isTrue(cardGive.getTotalNum() > 0, "加卡数量至少一张");

	        Date now = new Date();
	        //加卡纪录单保存
	        CardGiveRecord cardGiveRecord = new CardGiveRecord();
	        cardGiveRecord.setOrderNumber(BusiUtils.genSerialNumber(me));
	        cardGiveRecord.setPayUserId((Long)me.getId());

	        String account = cardGive.getAccount().trim();

            //此处需要UUID
			User u = null;
			if (StringUtils.isNotBlank(cardGive.getUuid())) {
				u = this.userDao.findByUUID(cardGive.getUuid());
			} else {
				u = this.userDao.findByLoginName(account);
			}
            Assert.notNull(u, "不存在账号ID");
            cardGiveRecord.setToUserId(u.getId());


//	        cardGiveRecord.setCardBuy(cardGive.getBuyNum());
//	        cardGiveRecord.setCardSong(cardGive.getSongNum());
//	        cardGiveRecord.setCardTotal(cardGive.getTotalNum());
//	        cardGiveRecord.setPayMoney(BusiUtils.calculatePayMoney(cardGive.getBuyNum()));
	        
//			if (me.getType() == RoleUtils.TYPE_PROXY_2) {//二级代理才有收钱，其他的是补偿，没钱
//				cardGiveRecord.setCardBuy(cardGive.getBuyNum());
//				cardGive.setSongNum(cardGive.getBuyNum()/BusiUtils.getCardExchangeBase());//十送一
//				cardGiveRecord.setCardSong(cardGive.getSongNum());
//				
//				cardGiveRecord.setPayMoney(BusiUtils.calculatePayMoney(cardGive.getBuyNum()));
//			} else {//一级代理只送卡，不卖卡
				cardGiveRecord.setCardBuy(0);
				cardGiveRecord.setCardSong(cardGive.getBuyNum());
				cardGiveRecord.setCardTotal(cardGive.getTotalNum());
				cardGiveRecord.setPayMoney(0);
//			}
	        
	        cardGiveRecord.setCreateDate(now);
	        this.cardDao.saveObj(cardGiveRecord);

            //受卡代理(库存)的卡
            CardStorage toCardStorage = getCardStorageAndNewIfNullByUserId(cardGiveRecord.getToUserId());
            toCardStorage.setCardRemain(toCardStorage.getCardRemain() + cardGiveRecord.getCardTotal());
            toCardStorage.setCardTotal(toCardStorage.getCardTotal() + cardGiveRecord.getCardTotal());
            cardDao.updateObj(toCardStorage);

            //受卡代理的入库明细
            CardStorageDetail inCardStorageDetail = assembleCardGiveRecord2CardStorageDetail(cardGiveRecord,now,true);
            cardDao.saveObj(inCardStorageDetail);
		
	}
	
    public CardStorage getCardStorageAndNewIfNullByUserId(Long userId) {
        if (userId != null) {
            CardStorage cardStorage = cardDao.find(CardStorage.class, "userId", userId);
            if (cardStorage == null) {
                cardStorage = new CardStorage();
                cardStorage.setUserId(userId);
                cardDao.saveObj(cardStorage);
            }
            return cardStorage;
        }
        return null;
    }
	
    private CardStorageDetail assembleCardGiveRecord2CardStorageDetail(CardGiveRecord cardGiveRecord,Date now,boolean direct_in) {
        CardStorageDetail cardStorageDetail = new CardStorageDetail();
        cardStorageDetail.setOrderNumber(cardGiveRecord.getOrderNumber());

        cardStorageDetail.setGameId(cardGiveRecord.getGameId());
        cardStorageDetail.setGameName(cardGiveRecord.getGameName());
        if (direct_in) {
            cardStorageDetail.setUserId(cardGiveRecord.getToUserId());
            cardStorageDetail.setOtherUserId(cardGiveRecord.getPayUserId());
        } else {
            cardStorageDetail.setUserId(cardGiveRecord.getPayUserId());
            cardStorageDetail.setOtherUserId(cardGiveRecord.getToUserId());
        }

        cardStorageDetail.setCardBuy(cardGiveRecord.getCardBuy());
        cardStorageDetail.setCardSong(cardGiveRecord.getCardBuy());
        cardStorageDetail.setCardTotal(cardGiveRecord.getCardTotal());
        cardStorageDetail.setPayMoney(cardGiveRecord.getPayMoney());

        cardStorageDetail.setType(BusiUtils.CARD_STORAGE_DETAIL_TYPE_JIAKA);
        if (direct_in) {
            cardStorageDetail.setDirection(BusiUtils.CARD_STORAGE_DETMAIL_DIRECTION_IN);
        } else {
            cardStorageDetail.setDirection(BusiUtils.CARD_STORAGE_DETMAIL_DIRECTION_OUT);
        }

        cardStorageDetail.setCreateDate(now);
        return cardStorageDetail;
    }

    @Override
    public Page findPage(UserMgr me, Pagination page) {
        return this.cardDao.findPage(SaleCard.class,page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SaleCard save(SaleCard card) {
        if (card.getId() != null) {
            return update(card);
        } else {
            return insert(card);
        }
    }

    private SaleCard update(SaleCard cardDto) {
        SaleCard dbCard = this.cardDao.find(SaleCard.class,Restrictions.eq("id",cardDto.getId()));
        dbCard.setCardBuy(cardDto.getCardBuy());
        dbCard.setCardSong(cardDto.getCardSong());
        dbCard.setName(cardDto.getName());
        dbCard.setRemark(cardDto.getRemark());
        dbCard.setStatus(cardDto.getStatus());
        dbCard.setCss(cardDto.getCss());
        dbCard.setSort(cardDto.getSort());
        this.cardDao.updateObj(dbCard);
        return dbCard;
    }

    private SaleCard insert(SaleCard cardDto) {
        SaleCard dbCard = new SaleCard();
        BeanUtils.copyProperties(cardDto, dbCard);
        dbCard.setUuid(java.util.UUID.randomUUID().toString());
        this.cardDao.saveObj(dbCard);
        return dbCard;
    }

	/* (non-Javadoc)
	 * @see cn.touch.game.bmr.serv.ICardService#findUserStoragePage(cn.touch.game.dto.UserMgr, cn.touchin.page.Pagination)
	 */
	@Override
	public Pagination findUserStoragePage(UserMgr me, Pagination page) {
		//Pagination p = this.cardDao.findPage(User.class, page);
        PageQuery pq =
                sqlParser
                        .createPageQuery(new SQLRunner(page)
                                .select(" u.id, t.col_card_remain,t.col_card_sold,t.col_card_song,t.col_card_total,col_city, col_country,col_headimgurl, col_idcard, col_language,col_loginname, col_manager_id, col_nickname,col_openid, col_province,col_realname, col_sex, col_status,col_type, col_unionid, col_u_time,col_uuid, col_wxaccount, col_wxmobile ")
                                .from(" mr_user u left join mr_card_storage t on u.id = t.col_userid ")
                                .where(" u.col_type<="+RoleUtils.TYPE_PROXY_1)
                        		);
//                                .setParameters(
//                                        new Object[] { }));
        
        page.setRows(db.find(pq.getPageSql(), pq.getPageParams()));
//        page.setRows(db.find(UserStorage.class, pq.getPageSql(), pq.getPageParams()));
        page.setTotal(((Long) db.findBy(pq.getCountSql(), 1, pq.getCountParams())).intValue());
        return page;
	}

	/* (non-Javadoc)
	 * @see cn.touch.game.bmr.serv.ICardService#queryProxyCardExchangeDetail(cn.touch.game.dto.UserMgr, cn.touchin.page.Pagination)
	 */
	@Override
	public Pagination queryProxyCardExchangeDetail(UserMgr me, Pagination page, int proxType) {
        SQLRunner sqlRunner = null;
		if (proxType == RoleUtils.TYPE_PROXY_2){
            sqlRunner = new SQLRunner(page)
                    .select(" r.col_card_buy AS totalCardBuy, r.col_card_song AS totalCardSong , r.col_pay_money totalMoney, r.col_buy_time buyDate, concat_ws('|',r.col_name,r.col_remark) remark,u.col_nickname,u.col_loginname,u.col_realname,u.col_uuid,u.id,u.col_headimgurl,u.col_manager_id ")
                    .from(" mr_sale_card_order r,mr_user u ")
                    .where(" r.col_user_id=u.id AND u.col_type=? AND r.col_status=?  ")
                    .orderBy(" r.col_buy_time DESC ")
                    .setParameters(
                            new Object[]{RoleUtils.TYPE_PROXY_2, BusiUtils.STATUS_DEALT});
        } else if (proxType == RoleUtils.TYPE_PROXY_1){
            sqlRunner = new SQLRunner(page)
                    .select(" r.col_card_buy AS totalCardBuy, r.col_card_song AS totalCardSong , r.col_pay_money totalMoney, r.col_buy_time buyDate, concat_ws('|',r.col_name,r.col_remark) remark,u.col_nickname,u.col_loginname,u.col_realname,u.col_uuid,u.id,u.col_headimgurl,u.col_manager_id ")
                    .from(" mr_sale_card_order r,mr_user u,mr_user uu ")
                    .where(" r.col_user_id=uu.id AND uu.col_manager_id=u.id and u.col_type=? AND r.col_status=?  ")
                    .orderBy(" r.col_buy_time DESC ")
                    .setParameters(
                            new Object[]{RoleUtils.TYPE_PROXY_1, BusiUtils.STATUS_DEALT});
		}
        PageQuery pq = sqlParser.createPageQuery(sqlRunner);
        page.setRows(db.find(pq.getPageSql(), pq.getPageParams()));
        page.setTotal(((Long) db.findBy(pq.getCountSql(), 1, pq.getCountParams())).intValue());
        return page;
	}

	/* (non-Javadoc)
	 * @see cn.touch.game.bmr.serv.ICardService#queryProxyCardExchangeStat(cn.touch.game.dto.UserMgr, cn.touchin.page.Pagination)
	 */
	@Override
	public Pagination queryProxyCardExchangeStat(UserMgr me, Pagination page, int proxType) {

        SQLRunner sqlRunner = null;
        if (proxType == RoleUtils.TYPE_PROXY_2){
            sqlRunner = new SQLRunner(page)
                    .select(" u.id,u.col_loginname,u.col_nickname,u.col_realname,u.col_manager_id,SUM(r.col_card_buy)  totalCardBuy,SUM(r.col_card_song)  totalCardSong , SUM(r.col_pay_money)  totalMoney, MIN(r.col_buy_time)  buyDate ")
                    .from(" mr_sale_card_order r,mr_user u ")
                    .where(" r.col_user_id=u.id AND u.col_type=? AND r.col_status=? ")
                    .groupBy(" DATE_FORMAT(r.col_buy_time,'%Y-%m-%d'),u.id,u.col_loginname,u.col_nickname,u.col_realname,u.col_manager_id ")
                    .setParameters(new Object[] { RoleUtils.TYPE_PROXY_2, BusiUtils.STATUS_DEALT });
        } else if (proxType == RoleUtils.TYPE_PROXY_1){
            sqlRunner = new SQLRunner(page)
                    .select(" u.id,u.col_loginname,u.col_nickname,u.col_realname,u.col_manager_id,SUM(r.col_card_buy)  totalCardBuy,SUM(r.col_card_song)  totalCardSong , SUM(r.col_pay_money)  totalMoney, MIN(r.col_buy_time)  buyDate ")
                    .from(" mr_sale_card_order r,mr_user u,mr_user uu")
                    .where(" r.col_user_id=uu.id AND uu.col_manager_id=u.id AND u.col_type=? AND r.col_status=? ")
                    .groupBy(" DATE_FORMAT(r.col_buy_time,'%Y-%m-%d'),u.id,u.col_loginname,u.col_nickname,u.col_realname,u.col_manager_id ")
                    .setParameters(new Object[] { RoleUtils.TYPE_PROXY_1, BusiUtils.STATUS_DEALT });
        }

/*        PageQuery pq =
                sqlParser
                        .createPageQuery(new SQLRunner(page)
                                .select(" u.id,u.col_loginname,u.col_nickname,u.col_realname,SUM(r.col_card_buy)  totalCardBuy,SUM(r.col_card_song)  totalCardSong , SUM(r.col_pay_money)  totalMoney, MIN(r.col_buy_time)  buyDate ")
                                .from(" mr_sale_card_order r,mr_user u ")
                                .where(" r.col_user_id=u.id AND r.col_status=? ")
                                .orderBy(" GROUP BY DATE_FORMAT(r.col_buy_time,'%Y-%m-%d'),u.id,u.col_loginname,u.col_nickname,u.col_realname ")
                                .setParameters(new Object[] {  BusiUtils.STATUS_DEALT })
                                );*/

        PageQuery pq = sqlParser.createPageQuery(sqlRunner);
        page.setRows(db.find(pq.getPageSql(), pq.getPageParams()));
        page.setTotal(((Long) db.findBy(pq.getCountSql(), 1, pq.getCountParams())).intValue());
        return page;
	}

    @Override
    public Pagination queryCardOrders(UserMgr me, Pagination page) {
        SQLRunner sqlRunner = new SQLRunner(page)
                    .select(" r.col_card_buy totalCardBuy,r.col_status status,r.col_card_song totalCardSong,r.col_pay_money totalMoney,r.col_buy_time buyDate,concat_ws('|',r.col_name,r.col_remark) remark,u.id as uid,u.col_type,u.col_nickname,u.col_loginname,u.col_realname,u.col_uuid,u.id,u.col_headimgurl,u.col_manager_id,u.col_type ")
                    .from(" mr_sale_card_order r,mr_user u ")
                    .where(" r.col_user_id=u.id ")
                    .orderBy(" r.col_buy_time DESC ");
        PageQuery pq = sqlParser.createPageQuery(sqlRunner);
        page.setRows(db.find(pq.getPageSql(), pq.getPageParams()));
        page.setTotal(((Long) db.findBy(pq.getCountSql(), 1, pq.getCountParams())).intValue());
        return page;
    }

	/* (non-Javadoc)
	 * @see cn.touch.game.bmr.serv.ICardService#deleteCard(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteCard(List<Long> ids) {
		return cardDao.deleteByIds(SaleCard.class, ids);
	}
}
