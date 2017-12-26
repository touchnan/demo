package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestMyProcess {

	private String filename = "src/main/resources/diagrams/MyLeaveProcess.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("myProcess.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		Scanner scanner = new Scanner(System.in);
		System.out.println("申请人：?");
		String value = scanner.nextLine();
		variableMap.put("u", value);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
		
		
		List<Task> tasks = activitiRule.getTaskService().createTaskQuery().list();
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			System.out.println("Processing Task [" + task.getName() + "]");
			variableMap = new HashMap<String, Object>();
			System.out.println("支路值：临界 2 ? ");
			String c = scanner.nextLine();
			variableMap.put("c", c);
			activitiRule.getTaskService().complete(task.getId(),variableMap);
		}
		
		while (processInstance != null && !processInstance.isEnded()) {
			tasks = activitiRule.getTaskService().createTaskQuery().taskCandidateUser("g1").list();
			
			for (int i = 0; i < tasks.size(); i++) {
				Task task = tasks.get(i);
				System.out.println(task.getName() + "任务接受者：?");
				String u = scanner.nextLine();
				activitiRule.getTaskService().claim(task.getId(), u);
				System.out.println("Processing Task [" + task.getName() + "]");
				activitiRule.getTaskService().complete(task.getId());
				
				HistoricActivityInstance endActivity = null;
				List<HistoricActivityInstance> activities = activitiRule.getHistoryService().createHistoricActivityInstanceQuery()
						.processInstanceId(processInstance.getId()).finished().orderByHistoricActivityInstanceEndTime()
						.asc().list();
				for (HistoricActivityInstance activity : activities) {
					if (activity.getActivityType() == "endEvent") {
						endActivity = activity;
					} else {
						System.out.println("-- " + activity.getActivityName() + " [" + activity.getActivityId() + "] "
								+ activity.getDurationInMillis() + " ms");
					}
				}
				if (endActivity != null) {
					System.out.println("-- " + endActivity.getActivityName() + " [" + endActivity.getActivityId() + "] "
							+ endActivity.getDurationInMillis() + " ms");
					System.out.println("COMPLETE " + " ["
							+ processInstance.getProcessDefinitionKey() + "] " + endActivity.getEndTime());
				}				
			}
			
			processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId())
			.singleResult();
		}
		
		scanner.close();
		
	}
}