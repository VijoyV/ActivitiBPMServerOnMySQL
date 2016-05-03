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


GET http://localhost:9090/repository/process-definitions

GET http://localhost:9090/repository/process-definitions?key=DataMappingApprovalProcess


### 2. To create a User in a Group 

POST http://localhost:9090/createUserInGroup

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

POST http://localhost:9090/authenticateUser

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

### 4.  To Start/Create the Data Mapping Process

POST http://localhost:9090/createDataMapping

JSON REQUEST

{

    "userId" : "vijoyv", 
    "processName" : "DataMappingApprovalProcess", 
    "dataMappingId" : "100100"
    
}

NOTE: 
By this REST Call, the process will be started and first task will be submitted as well. And the state move to 'Approve'

### 4. See some status
GET http://localhost:8080/runtime/tasks
GET http://localhost:8080/runtime/process-instances/11/diagram
GET http://localhost:8080/runtime/process-instances/11/variables
GET http://localhost:8080/runtime/tasks/19/variables



>> To Claim a Task [Claiming will come from InBox List with Actual Task ID]

POST http://localhost:8080/claimTask

JSON REQUEST

{
    "userId" : "hariram",   --> As Approver
    "taskId" : "7509"       --> Will change
}

>> To Submit a Claimed Task

POST	

