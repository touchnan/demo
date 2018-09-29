package cn.touch.demo.skill.tips;

import cn.touch.demo.skill.tips.proxy.spring.cglib.SpringCglibProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkillTipsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillTipsApplication.class, args);

		SpringCglibProxy.main(args);
	}
}
