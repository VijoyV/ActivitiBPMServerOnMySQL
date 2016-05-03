package com.bpmdev.activiti.response;

import java.util.Map;

public class UserActionResponse {

	private String requestedUserId;
	private String requestedAction;
	private String actionStatus;
	private String processKeyDefinition;
	private String processInstanceId;
	private String taskId;
	private String taskKeyDefinition;
	
	private Map<String, Object> variables;
	
	public String getRequestedUserId() {
		return requestedUserId;
	}
	public void setRequestedUserId(String requestedUserId) {
		this.requestedUserId = requestedUserId;
	}
	public String getRequestedAction() {
		return requestedAction;
	}
	public void setRequestedAction(String requestedAction) {
		this.requestedAction = requestedAction;
	}
	public String getActionStatus() {
		return actionStatus;
	}
	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}
	public String getProcessKeyDefinition() {
		return processKeyDefinition;
	}
	public void setProcessKeyDefinition(String processKeyDefinition) {
		this.processKeyDefinition = processKeyDefinition;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskKeyDefinition() {
		return taskKeyDefinition;
	}
	public void setTaskKeyDefinition(String taskKeyDefinition) {
		this.taskKeyDefinition = taskKeyDefinition;
	}
	
	
	public Map<String, Object> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
	
	@Override
	public String toString() {
		return "UserActionResponse [requestedUserId=" + requestedUserId + ", requestedAction=" + requestedAction
				+ ", actionStatus=" + actionStatus + ", processKeyDefinition=" + processKeyDefinition
				+ ", processInstanceId=" + processInstanceId + ", taskId=" + taskId + ", taskKeyDefinition="
				+ taskKeyDefinition + "]";
	}
	
}
