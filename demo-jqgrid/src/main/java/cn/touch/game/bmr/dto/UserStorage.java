/**
 * 
 */
package cn.touch.game.bmr.dto;

import cn.touch.game.entity.User;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Jun 9, 2017.
 */
public class UserStorage extends User {
    private long cardRemain;//我的库卡卡数
    private long cardSold;//我卖出卡数
    private long cardSong;//我送出卡数
    private long cardTotal;//我的总卡数
	/**
	 * @return the cardRemain
	 */
	public long getCardRemain() {
		return cardRemain;
	}
	/**
	 * @param cardRemain the cardRemain to set
	 */
	public void setCardRemain(long cardRemain) {
		this.cardRemain = cardRemain;
	}
	/**
	 * @return the cardSold
	 */
	public long getCardSold() {
		return cardSold;
	}
	/**
	 * @param cardSold the cardSold to set
	 */
	public void setCardSold(long cardSold) {
		this.cardSold = cardSold;
	}
	/**
	 * @return the cardSong
	 */
	public long getCardSong() {
		return cardSong;
	}
	/**
	 * @param cardSong the cardSong to set
	 */
	public void setCardSong(long cardSong) {
		this.cardSong = cardSong;
	}
	/**
	 * @return the cardTotal
	 */
	public long getCardTotal() {
		return cardTotal;
	}
	/**
	 * @param cardTotal the cardTotal to set
	 */
	public void setCardTotal(long cardTotal) {
		this.cardTotal = cardTotal;
	}
}
