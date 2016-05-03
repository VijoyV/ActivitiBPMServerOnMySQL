package com.bpmdev.activiti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bpmdev.activiti.response.UserActionResponse;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ActivitiRestController {

	private static final Logger log = LoggerFactory.getLogger(ActivitiRestController.class);

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private TaskService taskService;

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/createUserInGroup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String createUserInGroup(@RequestBody Map<String, String> data) {

		log.info("Inside createUserInGroup :: Input Data == " + data.values().toString());
		String userId = data.get("userId");
		String password = data.get("password");
		String firstName = data.get("firstName");
		String lastName = data.get("lastName");
		String email = data.get("email");
		String groupId = data.get("groupId");

		User newUser = identityService.newUser(userId);
		newUser.setPassword(password);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);

		identityService.saveUser(newUser);

		identityService.createMembership(newUser.getId(), groupId);

		String jsonString = ("{ \"status\" : \"success\" }");

		log.info("Completing createUserInGroup :: returns == " + jsonString);

		return jsonString;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/authenticateUser", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String authenticateUser(@RequestBody Map<String, String> data) {

		log.info("Inside authenticateUser :: Input Data == " + data.values().toString());

		String userId = data.get("userId");
		String password = data.get("password");

		boolean isAuthenticated = identityService.checkPassword(userId, password);

		String userGroupId = null;

		if (isAuthenticated) {
			userGroupId = identityService.createGroupQuery().groupMember(userId).singleResult().getId();
		} else {
			userGroupId = "NoGroup";
		}

		String jsonString = ("{ \"userId\" : \"" + userId + "\" , \"groupId\" : \"" + userGroupId + "\"}");

		log.info("Completing authenticateUser :: returns == " + jsonString);

		return jsonString;

	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/createDataMapping", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String startProcess(@RequestBody Map<String, String> data) {

		log.info("Inside createDataMapping :: Input Data == " + data.values().toString());

		String userId = data.get("userId");
		String processName = data.get("processName");
		String dataMappingId = data.get("dataMappingId");

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("dataMappingId", dataMappingId);

		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processName, variables);

		Task currentActiveTask = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId())
				.singleResult();

		currentActiveTask.setAssignee(userId);

		// "complete (taskId)" Means - Complete Task
		taskService.complete(currentActiveTask.getId());

		UserActionResponse uar = new UserActionResponse();

		uar.setRequestedUserId(currentActiveTask.getAssignee());
		uar.setRequestedAction("createDataMapping");
		uar.setActionStatus("success");
		uar.setProcessKeyDefinition(currentActiveTask.getProcessDefinitionId());
		uar.setProcessInstanceId(currentActiveTask.getProcessInstanceId());
		uar.setTaskId(currentActiveTask.getId());
		uar.setTaskKeyDefinition(currentActiveTask.getTaskDefinitionKey());
		uar.setVariables(getProcessVariables(currentActiveTask.getProcessInstanceId()));

		log.info("Completing createDataMapping :: returns == " + uar.toString());

		return getJsonStringFromPOJO(uar);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/getUserInbox", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getUserInbox(@RequestBody Map<String, String> data) {

		log.info("Inside getUserInbox :: Input Data == " + data.values().toString());

		String userId = data.get("userId");
		
		List<Task> userClaimableTasks = taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
		
		List<UserActionResponse> userActionResponses = new ArrayList<UserActionResponse>();
		
		
		for (Task task : userClaimableTasks) {
			UserActionResponse uar = new UserActionResponse();
			
			uar.setRequestedUserId(userId);
			uar.setRequestedAction("getUserInbox");
			uar.setActionStatus("success");
			uar.setProcessKeyDefinition(task.getProcessDefinitionId());
			uar.setProcessInstanceId(task.getProcessInstanceId());
			uar.setTaskId(task.getId());
			uar.setTaskKeyDefinition(task.getTaskDefinitionKey());
			uar.setVariables(getProcessVariables(task.getProcessInstanceId()));
			userActionResponses.add(uar);
	    }

		log.info("Completing getUserInbox :: returns == " + userActionResponses.toString());

		return getJsonStringFromPOJO(userActionResponses);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/claimTask", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String claimTask(@RequestBody Map<String, String> data) {

		log.info("Inside claimTask :: Input Data == " + data.values().toString());

		String userId = data.get("userId");
		String taskId = data.get("taskId");

		Task claimingTask = taskService.createTaskQuery().taskId(taskId).singleResult();

		taskService.claim(claimingTask.getId(), userId);

		UserActionResponse uar = new UserActionResponse();

		uar.setRequestedUserId(userId);
		uar.setActionStatus("success");
		uar.setRequestedAction("claimTask");
		uar.setProcessKeyDefinition(claimingTask.getProcessDefinitionId());
		uar.setProcessInstanceId(claimingTask.getProcessInstanceId());
		uar.setTaskId(claimingTask.getId());
		uar.setTaskKeyDefinition(claimingTask.getTaskDefinitionKey());
		uar.setVariables(getProcessVariables(claimingTask.getProcessInstanceId()));
		
		log.info("Completing claimTask :: returns == " + uar.toString());

		return getJsonStringFromPOJO(uar);

	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/submitTask", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public String submitTask(@RequestBody Map<String, String> data) {

		log.info("Inside submitTask :: Input Data == " + data.values().toString());

		String userId = data.get("userId");
		String taskId = data.get("taskId");
		String dataMappingId = data.get("dataMappingId");
		String approved = data.get("approved");
		String userComment = data.get("userComment");

		Task submittingTask = taskService.createTaskQuery().taskId(taskId).taskAssignee(userId).singleResult();

		UserActionResponse uar = new UserActionResponse();

		if (submittingTask == null) {
			uar.setRequestedUserId(userId);
			uar.setRequestedAction("submitTask"); // Need To Find a generic way
													// of getting it from
													// request.
			uar.setActionStatus("failed");
		} else {
			log.info("Submitting Task == " + submittingTask.getId() + " And Assignee == "
					+ submittingTask.getAssignee());

			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("approved", approved);
			variables.put("userComment", ("[" + userId + "]" + userComment));

			// "complete (taskId, variables)" Means - submit Task with variables
			taskService.complete(submittingTask.getId(), variables);

			// NOTE: The moment the task is is completed, all variables and
			// execution details disappears.
			// We have to relay on history service to retrieve those
			// process/task variables

			uar.setRequestedUserId(userId);
			uar.setActionStatus("success");
			uar.setRequestedAction("submitTask"); 
			uar.setProcessKeyDefinition(submittingTask.getProcessDefinitionId());
			uar.setProcessInstanceId(submittingTask.getProcessInstanceId());
			uar.setTaskId(submittingTask.getId());
			uar.setTaskKeyDefinition(submittingTask.getTaskDefinitionKey());
			uar.setVariables(getProcessVariables(submittingTask.getProcessInstanceId()));

			log.info("Completing submitTask :: returns == " + uar.toString());
		}

		return getJsonStringFromPOJO(uar);

	}

	private String getJsonStringFromPOJO(Object obj) {

		String jsonnString = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			jsonnString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			System.out.println(jsonnString);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonnString;
	}
	
	private Map <String, Object> getProcessVariables(String processId)
	{
		Map<String, Object> processVariables = runtimeService.getVariables(processId);
		
		return processVariables;
	}
	
}