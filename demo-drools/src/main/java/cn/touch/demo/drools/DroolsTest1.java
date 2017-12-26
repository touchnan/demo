/**
 * 
 */
package cn.touch.demo.drools;

import java.util.ArrayList;
import java.util.List;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Jun 29,
 * 2017.
 * 
 * http://www.cnblogs.com/tom-lau/p/6913315.html
 */
public class DroolsTest1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// load up the knowledge base
			KieServices ks = KieServices.Factory.get();
			KieContainer kContainer = ks.getKieClasspathContainer();
			
			KieSession kSession = kContainer.newKieSession("ksession-rules");
			
			// go !
			Message message = new Message();
			message.setMessage("Hello World");
			message.setStatus(Message.HELLO);
			System.out.println(String.format("执行规则前message对象的变化如下：message.message=%s,message.status=%d",
					message.getMessage(), message.getStatus()));
			Message message2 = new Message();
			message2.setMessage("liuyinghui");
			message2.setStatus(Message.HELLO);
			List<Message> listMsg = new ArrayList<Message>();
			listMsg.add(message2);
			listMsg.add(message);
			for (Message msg : listMsg) {
				kSession.insert(msg);
				kSession.fireAllRules();
			}
			System.out.println(String.format("执行规则后message对象的变化如下：message.message=%s,message.status=%d",
					message.getMessage(), message.getStatus()));

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
