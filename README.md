# ActivitiBPMServerOnMySQL

This is a Activiti Spring Boot Application with embedded Tomcat Servlet Container talking to MySQL DB

<h2> Pre-requisite </h2>

Should have a MySQL Server running with an empty Database 'activiti'. This should be able to accessed by a user called 'activiti' and password 'acti$viti'.

Download the source code to Eclipse or Spring Tool Suite (STS) as a maven project or add a maven behaviour. 

Then build so that all required dependencies will be downloaded.

You may build this as satndalone jar file and run on any machine as a Standalone Application. Assume that the above mentioned MySQL server and database is up and running. Tomcat Listening Http Port is 9090

<h2> REST Endpoints </h2>

Your client applications may interact with this Activiti BPM Server (running stand-alone server)  via REST. The following custom REST URLs are developed with defined input/output. 

<h3> >>  To See the deployed process definitions - The Blue Print</h3>

>>  To See the deployed process definitions - The Blue Print
GET http://localhost:9090/repository/process-definitions
GET http://localhost:9090/repository/process-definitions?key=DataMappingApprovalProcess


