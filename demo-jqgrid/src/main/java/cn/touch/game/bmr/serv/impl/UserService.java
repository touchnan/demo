/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.touch.game.bmr.serv.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.hibernate.criterion.Restrictions;
import org.nutz.lang.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.touch.game.bmr.db.IUserDao;
import cn.touch.game.bmr.serv.IUserService;
import cn.touch.game.dto.UserMgr;
import cn.touch.game.entity.User;
import cn.touch.game.utils.RoleUtils;
import cn.touch.security.crypto.Encoder;
import cn.touch.security.shiro.TouchPrincipal;
import cn.touch.security.shiro.TouchUsernamePasswordToken;
import cn.touchin.page.Page;
import cn.touchin.page.Pagination;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on May 2, 2017.
 */
@Service
@Transactional(readOnly = true)
public class UserService implements IUserService {

    @Autowired()
    private IUserDao userDao;

    @Autowired()
    private Encoder encode;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User save(User u) {
        if (u.getId() != null) {
            return update(u);
        } else {
            return insert(u);
        }
    }

    private User update(User uDto) {
        User dbUser = this.userDao.find(uDto.getId());
        if (StringUtils.isNotBlank(uDto.getPasswd())) {
            dbUser.setPasswd(encode.encode(uDto.getPasswd().trim()));
            //dbUser.setPasswd(uDto.getPasswd());
        }

        dbUser.setLoginName(uDto.getLoginName());
        dbUser.setNickname(uDto.getNickname());
        dbUser.setWxaccount(uDto.getWxaccount());
        dbUser.setWxmobile(uDto.getWxmobile());
        
        dbUser.setRealname(uDto.getRealname());
        dbUser.setIdcard(uDto.getIdcard());
        dbUser.setManagerId(uDto.getManagerId());
        dbUser.setType(uDto.getType());
        

        dbUser.setUpdateDate(new Date());

        this.userDao.updateObj(dbUser);
//        if (true) throw new RuntimeException("update no impl");
        return dbUser;
    }

    private User insert(User uDto) {
        User dbUser = new User();

        BeanUtils.copyProperties(uDto, dbUser);

        if (StringUtils.isNotBlank(uDto.getPasswd())) {
            dbUser.setPasswd(encode.encode(uDto.getPasswd().trim()));
        }
        dbUser.setUuid(java.util.UUID.randomUUID().toString());
        this.userDao.save(dbUser);
        return dbUser;
    }

    @Override
    public User login(User user) {
        User u = userDao.findByLoginName(user.getLoginName());
        if (u != null) {
            if (encode.canDecode(u.getPasswd())) {
                if (encode.isValid(user.getPasswd().trim(), u.getPasswd())) {
                    return u;
                }
            }
        }
        return null;
    }

//    @Override
//    public User findUser(Long id) {
//    	 User u = userDao.find(id);
//    	 Hibernate.initialize(u);
//         return u;
//    }


    /* (non-Javadoc)
     * @see cn.touch.security.shiro.ITouchSubjectDao#getAuthorizationInfo(cn.touch.security.shiro.TouchPrincipal)
     */
    @Override
    public SimpleAuthorizationInfo getAuthorizationInfo(TouchPrincipal principal) {
        UserMgr u = (UserMgr) principal;
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (u != null) {
            switch (u.getType()) {
                case RoleUtils.TYPE_MANGER:
                    for (String p : RoleUtils.PERSSIONS_CODE_ADMIN) {
                        info.addStringPermission(p);
                    }
                    info.addRole(RoleUtils.ROLE_CODE_ADMIN);
                    break;
                case RoleUtils.TYPE_PROXY_1:
                    for (String p : RoleUtils.PERSSIONS_CODE_PROXY_1) {
                        info.addStringPermission(p);
                    }
                    info.addRole(RoleUtils.ROLE_CODE_PROXY_1);
                    break;
                case RoleUtils.TYPE_PROXY_2:
                    for (String p : RoleUtils.PERSSIONS_CODE_PROXY_2) {
                        info.addStringPermission(p);
                    }
                    info.addRole(RoleUtils.ROLE_CODE_PROXY_2);
                    break;
                default:
                    break;
            }
        }
        return info;
    }

    /* (non-Javadoc)
     * @see cn.touch.security.shiro.ITouchSubjectDao#getAuthorizationInfo(cn.touch.security.shiro.TouchUsernamePasswordToken)
     */
    @Override
    public TouchPrincipal getAuthenticationInfo(TouchUsernamePasswordToken userToken) {
//        User u = userDao.findByLoginName(userToken.getUsername());
        User u = userDao.find(User.class, Restrictions.eq("loginName", userToken.getUsername())
        		,Restrictions.eq("type", RoleUtils.TYPE_MANGER) );
        return new UserMgr(u, true);
    }

    @Override
    public Page findPage(UserMgr me, Pagination page) {
        return this.userDao.findPage(User.class, page);
    }
    
    

    /* (non-Javadoc)
	 * @see cn.touch.game.bmr.serv.IUserService#findAmdinAndProx1(cn.touch.game.dto.UserMgr)
	 */
	@Override
	public List<User> findAmdinAndProx1(UserMgr me) {
		// TODO Auto-generated method stub
		return this.userDao.findList(User.class, Restrictions.ge("type", RoleUtils.TYPE_PROXY_1));
	}

	@Override
    public int resetSelfPasswd(UserMgr me, String passwd, String newPasswd) {
        User u = userDao.find((Long) me.getId());
        if (u != null) {
            if (encode.canDecode(u.getPasswd())) {
                if (encode.isValid(passwd, u.getPasswd())) {
                    return userDao.exec("UPDATE " + User.class.getName() + " o SET o.passwd=? WHERE o.id=?",
                            encode.encode(newPasswd), (Long) me.getId());
                }
            }
        }
        throw new RuntimeException("旧密码错误, 无权修改密码!");
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
    public int resetPasswd(UserMgr me, List<Long> uIds, String nPasswd) {
        if (uIds != null && !uIds.isEmpty() && !Strings.isBlank(nPasswd)) {
            return userDao.exec("UPDATE " + User.class.getName() + " o SET o.passwd=:passwd WHERE o.id in (:ids)"
                    , encode.encode(nPasswd), uIds);
        }
        return 0;
    }

	/* (non-Javadoc)
	 * @see cn.touch.game.bmr.serv.IUserService#deleteCard(java.util.List)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteCard(List<Long> ids) {
		return userDao.deleteByIds(User.class, ids);
	}
	
	
}
