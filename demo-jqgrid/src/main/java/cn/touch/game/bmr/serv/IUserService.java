/**
 *
 */
package cn.touch.game.bmr.serv;

import java.util.List;

import cn.touch.game.dto.UserMgr;
import cn.touch.game.entity.User;
import cn.touch.security.shiro.ITouchSubjectDao;
import cn.touchin.page.Page;
import cn.touchin.page.Pagination;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on May 2, 2017.
 */
public interface IUserService extends ITouchSubjectDao {
//    public List<User> findAll();

    public User save(User u);

    public User login(User user);

//    public User findUser(Long id);


    public Page findPage(UserMgr me,Pagination page);

    /**
     *
     * @param me
     * @param passwd
     * @param newPasswd
     * @return
     */
    int resetSelfPasswd(UserMgr me, String passwd, String newPasswd);

	/**
	 * @param me
	 * @return
	 */
	public List<User> findAmdinAndProx1(UserMgr me);

	/**
	 * @param ids
	 */
	public int deleteCard(List<Long> ids);
}
