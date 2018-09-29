package cn.touch.demo.skill.tips;

import cn.touch.demo.skill.tips.proxy.spring.cglib.SpringCglibProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SkillTipsApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void cglibProxy() {
		SpringCglibProxy.main(null);
	}
}
