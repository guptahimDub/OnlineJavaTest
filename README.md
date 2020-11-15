# OnlineJavaTest

## ToDoList Application
A TODO list is web application to track the day to day tasks of users.

## Supported Functionalities
- Registration of new users.
- Login for existing users.  
- Validation and hence present the message when incorrect information is provided.
- Add Task and Remove Task.
- Supports the checking/unchecking of tasks.
- Users can view their existing tasks after login.
- It will show the last updated time for every task.
- The user’s information gets stored in an in-memory H2 database (UserInfo and TaskInfo tables).
 

### Prerequisites
- [Java 8]
- [Lombok Java Library]
- [Tomcat 8.5] 

## Technologies Used

- Spring Framework
- Spring Data JPA
- H2 Database
- Spring MVC Test framework and JUnits
- Lombok Java Library
- CSS and HTML
- AJAX JavaScript
- Maven

## Installation Steps
1. Clone the git repository.
2. JavaProject consists of two folder server (server-side code) and client (client-side code).
3. Build the project using Maven.

## Build Projects

1. server side build 

`mvn clean install` - under JavaProject/server

2. client side build 

`mvn clean install` - under JavaProject/client

3. war file will be created under the target folder - client\target\onlineTodoList.war

##Usage
Deploy the generated war file on the Tomcat server. The application can be accessible using the below URL.

`http://localhost:8080/onlineTodoList/`

### Steps To Follow
1. Create User (for new users)/Login (for existing users)
2. Now write your tasks and press Click to Add.
3. Check/Uncheck the task.
4. Delete the task.
