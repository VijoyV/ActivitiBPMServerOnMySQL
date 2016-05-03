# ActivitiBPMServerOnMySQL

This is a Activiti Spring Boot Application with embedded Tomcat Servlet Container talking to MySQL DB

## Pre-requisite

Should have a MySQL Server running with an empty Database 'activiti'. This should be able to accessed by a user called 'activiti' and password 'acti$viti'.

Download the source code to Eclipse or Spring Tool Suite (STS) as a maven project or add a maven behaviour. 

Then build so that all required dependencies will be downloaded.

You may build this as satndalone jar file and run on any machine as a Standalone Application. Assume that the above mentioned MySQL server and database is up and running. Tomcat Listening Http Port is 9090

## REST Endpoints

Your client applications (or REST Plug-Ins like POSTMAN ) may interact with this Activiti BPM Server (running stand-alone server) via REST. Use admin/admin as request sending client userid/password with Base64 Authentication in Http Header. 
The following custom and activiti supplied REST URLs are explained below to build a client application by you. 


### 1. To See the deployed process definitions - The Blue Print


>> GET http://localhost:9090/repository/process-definitions

>> GET http://localhost:9090/repository/process-definitions?key=DataMappingApprovalProcess


### 2. To create a user in a group 

>> POST http://localhost:9090/createUserInGroup

Json Request

{

    "userId" : "vijoyv", 
    "password" : "vij$123", 
    "firstName" : "Vijoy", 
    "lastName" : "Vallachira",
    "email" : "vijoye@gmail.com", 
    "groupId"    : "requester"
    
}


Another One

{

    "userId" : "hariram", 
    "password" : "hari$ram", 
    "firstName" : "Hari Ram", 
    "lastName" : "A",
    "email" : "talk2hari@gmail.com", 
    "groupId"    : "approver"

}

Json Response

{

    "status": "success"
}

### 3. To verify created user OR to athenticate a user.

>> POST http://localhost:9090/authenticateUser

Json Request
{

    "userId" : "vijoyv", 
    "password" : "vij$123"
    
}

JSon Response (If Success)

{

    "userId": "vijoyv",
    "groupId": "requester"
  
}

JSon Response (If authentication failed)

{ 

    "userId": "vijoyv",
    "groupId": "NoGroup"
    
}

### 4.  To create and start a DataMapping process instance

>> POST http://localhost:9090/createDataMapping

JSON REQUEST

{

    "userId" : "vijoyv", 
    "processName" : "DataMappingApprovalProcess", 
    "dataMappingId" : "100100"
    
}

**NOTE:**
1. By this REST Call, the process will be started and first task will be submitted as well. And the state move to 'Approve' state
2. ** dataMappingId** is ** client system generated ** and passed in as a json parameter. Activiti stores it as a process variable and one could get this id back from Activiti in subsequent requests.

### 5. To verify some some status/state/diagrams from Activiti Engine.

>> GET http://localhost:9090/runtime/tasks
>> GET http://localhost:9090/runtime/process-instances/{id}/diagram
>> GET http://localhost:9090/runtime/process-instances/{id}/variables
>> GET http://localhost:9090/runtime/tasks/{id}/variables


### 6. To Claim a Task 

>> POST http://localhost:9090/claimTask

Json Request

{
    "userId" : "hariram",   --> As Approver
    "taskId" : "7509"       --> Will change as it is used from user inbox.
}

**NOTE:**
1. Claiming will come from User InBox List and with Actual Task ID
2. The client system must have pulled a user inbox from activiti soon after he/she logs in.
3. Claiming is required to lock the activiti task with the caiming user and no other user inbox will display that items who had subsequently logged in or refreshed his/her list.

>> 7. To Submit a Claimed Task

>> POST http://localhost:9090/submitTask

Json Request

{
    "userId" : "hariram",   --> As Approver
    "taskId" : "7509"       --> Will change as it is used from user inbox.
}

**NOTE:**
1. Only Claimed task can be submitted.
2. 

