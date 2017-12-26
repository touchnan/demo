/**
 * 
 */
package cn.touch.demo.drools;

import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.logger.KnowledgeRuntimeLogger;
import org.kie.internal.logger.KnowledgeRuntimeLoggerFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Jun 29,
 * 2017.
 */
public class DroolsTest {
	public static final void main(String[] args) {
		try {
			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();// 创建会话
			//KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
			// go !
			Message message = new Message();
			message.setMessage("Hello World");
			message.setStatus(Message.HELLO);
			ksession.insert(message);// 插入
			ksession.fireAllRules();// 执行规则
			
			System.out.println(message.getMessage());
			System.out.println(message.getStatus());
			
			//logger.close();// 关闭
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();// 创建规则构建器
		//kbuilder.add(ResourceFactory.newClassPathResource("cn/touch/demo/drools/Sample.drl"), ResourceType.DRL);// 加载规则文件，并增加到构建器
		kbuilder.add(ResourceFactory.newClassPathResource("Sample.drl"), ResourceType.DRL);// 加载规则文件，并增加到构建器

		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {// 编译规则过程中发现规则是否有错误
			for (KnowledgeBuilderError error : errors) {
				System.out.println("规则中存在错误，错误消息如下：");
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();// 创建规则构建库
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());// 构建器加载的资源文件包放入构建库
		return kbase;
	}

}
