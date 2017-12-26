package cn.touch.demo.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.concurrent.Executors;

/**
 * Created by <a href="mailto:88052350@qq.com">touchnan</a> on 2016/5/10.
 */
public class ActivitiProcess {
    public static void main(String[] args) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        TaskService taskService = processEngine.getTaskService();
        ManagementService managementService = processEngine.getManagementService();
        IdentityService identityService = processEngine.getIdentityService();
        HistoryService historyService = processEngine.getHistoryService();
        FormService formService = processEngine.getFormService();



        Deployment d =  repositoryService.createDeployment()
//                .addZipInputStream().addZipInputStream().addClasspathResource().addInputStream()
                .deploy();
        d.getId();
        repositoryService.createDeploymentQuery().deploymentId("").deploymentName("").deploymentNameLike("").list();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId("").latestVersion().singleResult();
        processDefinition.getId();
    }

    public static void main2(String[] args) {
        /*-
        ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(String resource);
        ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(String resource, String beanName);
        ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(InputStream inputStream);
        ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(InputStream inputStream, String beanName);

        ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();

        */

        /*-
            All these ProcessEngineConfiguration.createXXX() methods
            return a ProcessEngineConfiguration that can further be tweaked if needed.
            After calling the buildProcessEngine() operation, a ProcessEngine is created
         */
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
                .setAsyncExecutorEnabled(true)
                .setAsyncExecutorActivate(false)
                .buildProcessEngine();
    }
}
