# ActivitiEmbeddedEngineAndREST
! Activiti Embedded Engine and REST Access:

This is the experimental code which customize the spring-boot-activiti example ( https://dzone.com/articles/getting-started-activiti-and ) 
for Hiring Process to make work for any process deployed to the Activiti BPM Engine and expose key methods as REST APIs.

At his point of time, you can just download this project into Eclipse or Spring STS IDE as a spring boot application. I believe, the POM will indicate it as a Spring Boot application and downlaod all dependencies. 

Just download it from git repository and import to STS as maven project (or convert it as a maven
project). Run as spring boot application.
 
1. That will create an Actviti BPM Engine with In-Memory H2 Database.
2. Creates a user called admin/admin
3. deploy the embedded bpmn 2.0 complaint xml file (Here it is a leave application process).

Then open a REST Client like Postman and execute the following commands to see the process is getting executed task by task.

GET	http://localhost:8080/mappings (Gives all REST EndPoints)

//1. Get the deployed process definitions from the Activiti Engine

GET http://localhost:8080/repository/process-definitions

//2. Verify it with obtaining a diagram

GET http://localhost:8080/repository/process-definitions/LeaveApplicationProcess:1:7/image

//3. Kick Start the Process 'Leave Application Process' with the following POST statement and JSON Request Body (Parameters).

POST http://localhost:8080/LeaveApplicationProcess

JSON Request Body

{

    "name"          : "Vijoy Vallachira", 
    "email"         : "vijoye@gmail.com", 
    "noOfDays"      : "4", 
    "startDate"     : "2016-04-07"

}

// 4. Locate and Execute Task 'Apply Leave'

POST http://localhost:8080/ApplyLeave

JSON Request Body
{

    "processInstanceId" : "8", 
    "loanApplicantId" :   "1", 
    "taskDefinitionKey" : "applyLeaveTask"

}

// 5. See the current Stage of the Process.

GET http://localhost:8080/runtime/process-instances/8

GET http://localhost:8080/runtime/process-instances/8/diagram

NOTE: This is still a work in progress.
