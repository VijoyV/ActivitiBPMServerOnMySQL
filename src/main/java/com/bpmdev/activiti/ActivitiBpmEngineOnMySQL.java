package com.bpmdev.activiti;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.tomcat.util.descriptor.web.ErrorPage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;


@SpringBootApplication
public class ActivitiBpmEngineOnMySQL {

	public static void main(String[] args) {
		SpringApplication.run(ActivitiBpmEngineOnMySQL.class, args);
	}
	
	/*
	 *  Enable this bean when you want to use the MySQL DB as Activiti Persistent DB for first time,
	 *  Also, need to disable H2 MAven dependency 
	 *  and enable MySQL + Tomcat Datasource dependency in POM
	 *  
	 *  Please make sure that an Empty Database is created as 'Activiti' with MySQL server
	 *  And a user like 'activiti' has full access to that db with a password, here 'acti$viti'. 
	 *  
	 */
	
	@Bean
	DataSource database() {
	    
		return DataSourceBuilder.create()
	        .url("jdbc:mysql://localhost:3306/activiti?characterEncoding=UTF-8")
	        .username("activiti").password("acti$viti").driverClassName("com.mysql.jdbc.Driver").build();
	}
	
	/*
	 *  The bean 'EmbeddedServletContainerFactory' is used to change the default HTTP Port of embedded Tomct 
	 *  Refer: Spring Boot Manual
	 *  
	 *  [vijoy vallachira]
	 */
	
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
	    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
	    factory.setPort(9090);
	    factory.setSessionTimeout(10, TimeUnit.MINUTES);
	    return factory;
	}
	
	/*
	 *  The bean 'usersAndGroupsInitializer' will be disabled after initial run. 
	 *  Only need to run to create seed user (admin/admin) and groups (admin/requester/approver)
	 *  
	 *  [vijoy vallachira]
	 */
	
//	@Bean
//	InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {
//
//		return new InitializingBean() {
//			public void afterPropertiesSet() throws Exception {
//
//				// 1- Creating Admin Group with one member admin/admin
//				
//				Group group = identityService.newGroup("admin");  	//id
//				group.setName("AdminGroup");						//name
//				group.setType("security-role");						//type
//				identityService.saveGroup(group);
//				
//				User admin = identityService.newUser("admin");
//				admin.setPassword("admin");
//				identityService.saveUser(admin);
//				
//				identityService.createMembership("admin", "admin");
//				
//				// 2. Creating two assignment groups with no members - requester and approver
//				group = identityService.newGroup("requester");
//				group.setName("RequesterGroup");
//				group.setType("assignment");
//				identityService.saveGroup(group);
//				
//				group = identityService.newGroup("approver");
//				group.setName("ApproverGroup");
//				group.setType("assignment");
//				identityService.saveGroup(group);
//				
//			}
//		};
//	}

}
