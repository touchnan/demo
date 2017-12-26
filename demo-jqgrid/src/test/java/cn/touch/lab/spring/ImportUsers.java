/*
 * test.cn.touch.zkytxl.vo.ImportUsers.java
 * Jul 27, 2014 
 */
package cn.touch.lab.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.touch.game.bmr.serv.IUserService;
import cn.touch.game.entity.SaleCard;
import cn.touch.game.entity.User;
import cn.touch.game.utils.RoleUtils;

/**
 * 
 * @author <Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on
 *         May 2, 2017.
 * 
 */
public class ImportUsers {

	private static ApplicationContext cx = new ClassPathXmlApplicationContext("spring/context.xml");
	protected static IUserService userService = (IUserService) cx.getBean(IUserService.class);
	private static String filename = "src/test/resources/aa.xls";

	public static void main(String[] args) {
		try {
			initUsers();
			System.out.println("Data init success.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("人员初始化失败");
		} finally {
			System.exit(0);
		}

	}

	/**
	 * @throws Exception @throws
	 * 
	 */
	private static void initUsers() throws Exception {
		/*
		 * File file = FileUtils.getFile(filename);
		 * System.out.println(file.getAbsolutePath()); readExcel(file);
		 */

		User u = new User();
		u.setLoginName("admin");
		u.setPasswd("a123456");
		u.setType(RoleUtils.TYPE_MANGER);
		u.setUuid(java.util.UUID.randomUUID().toString());
		userService.save(u);
		
		User u2 = new User();
		u2.setLoginName("lx");
		u2.setPasswd("123456");
		u2.setType(RoleUtils.TYPE_PROXY_1);
		u2.setManagerId(u.getId());
		u2.setUuid(java.util.UUID.randomUUID().toString());
		userService.save(u2);
		
//		u = new User();
//		u.setLoginName("touchnan");
//		u.setWxaccount("wx_touchnan");
//		u.setPasswd("123456");
//		u.setType(RoleUtils.TYPE_PROXY_1);
//		u.setUuid(java.util.UUID.randomUUID().toString());
//		userService.save(u);
		
	}

}
