/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.touch.game.bmr.db.impl;

import cn.touch.game.bmr.db.IUserDao;
import cn.touch.game.db.impl.BaseDao;
import cn.touch.game.entity.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 
 * @author Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on May 2, 2017.
 */
@Repository
public class UserDao extends BaseDao implements IUserDao {

    @Override
    public User save(User u) {
        return saveOrUpdateObj(u);
    }

    @Override
    public User find(Long id) {
        return (User) getSession().load(User.class, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.touch.zkytxl.db.ITxlDao#deletes(java.lang.Class, java.util.Set)
     */
    @Override
    public void deletes(Class<?> clazz, Set<?> entities) {
        Session s = getSession();
        for (Object o : entities) {
            s.delete(o);
        }
    }

	/* (non-Javadoc)
	 * @see cn.touch.game.bmr.db.IUserDao#findByLoginName(java.lang.String)
	 */
	@Override
	public User findByLoginName(String loginName) {
	   return find(User.class,"loginName",loginName);
	}

    @Override
    public User findByUUID(String uuid) {
        return super.find(User.class,"uuid",uuid);
    }
}
