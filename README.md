# Port Scanner Application

This is a demonstration project which scans ports using NMAP and saves history in mysql database. The project is written in Java 7 utilizing Play Framework v2.3.7 with jQuery front end, and was developed on Windows. 

## Installation
The following installation procedure assumes that you already have MySQL installed and running on localhost:3306. 

#### Dependencies
These dependencies need to be running on your system before running the port scanner application. All of these packages are avaialbe for Mac, Windows, and Linux operating systems. OS-specific installation instructions can be found on the respective websites.

 * NMAP: http://nmap.org/download.html
 * Java 7 Runtime Environment: http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
 * Play Framework: https://www.playframework.com/download

#### Checking out the code
Once "activator run" command works on your system (meaning you have java and play set up correctly), and issuing the nmap command on command line returns some results, you are ready to check out the code. Simple clone this repository into an easily-accessible location on the local machine. 

#### Installing the schema
The port scanner MySQL schema is located in file sql/schema.sql relative to the codebase root directory, or alternately can be found here: https://raw.githubusercontent.com/rhinocerous/port-scanner/master/sql/schema.sql. Use this sql file to create the database for port scanner.

#### Configuring the application
There is a file called conf/application.conf () which contains the application configuration for the port scanner application. Within this file on line 40 there is a database connection string which must be modified to work with the system onto which it is being installed. 
```
db.default.url="mysql://root:roscoe@localhost/port_scanner?characterEncoding=UTF-8"
```
In this example the username is root, password is roscoe, host is localhost, and database name is port_scanner. Update these values as needed. 

#### Running the code
Once configured you can start the port scanner application from the command line by navigating to the git root directory and issuing the following command.
`activator run
The first time the project is run it will take a while to compile as Activator downloads and installs a bunch of dependencies. Eventually the console will note that the server is online and listening on 0.0.0.0:9000. At that point the application can be seen on port 9000 of whatever system you are on. For example I am running the project on my local Windows machine and I can use the app by going to http://localhost:9000 in my browser.


