/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.touch.game.bmr.db;

import cn.touch.game.db.IBaseDao;
import cn.touch.game.entity.User;

import java.util.Set;

/**
 *
 * @author Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on May 2, 2017.
 */
public interface IUserDao extends IBaseDao{
    
    public User save(User u);
    
    public User find(Long id);
    
    /**
     * @param clazz
     * @param entities
     */
    public void deletes(Class<?> clazz, Set<?> entities);

	/**
	 * @param loginName
	 * @return
	 */
	public User findByLoginName(String loginName);

	public User findByUUID(String uuid);

}
