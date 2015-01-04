# Port Scanner Application

This is a demonstration project which scans ports using NMAP and saves history in mysql database. The project is written in Java 7 utilizing Play Framework v2.3.7 with jQuery front end, and was developed on Windows. 

## Development notes
 * On my Windows 8 laptop I noticed a problem with NMAP when you try to scan more than two or three hosts at once. I am running the requests in parallel so I am not sure if this is a bug caused by the java promises or by NMAP itself not being able to run concurrently.
 * Dependency injection in this project is managed with Guice, in case you wondered what all those "@inject" annotations are for.
 * The UI is not great, I used simple jQuery UI accordions and loaded everything via ajax. if this was a production project I would have liked to add pagination links for the scan history and a better way to navigate those items. I spent much more time building the API than the front end.
 * I didn't get enough time to make proper unit tests or add enough documentation to my interfaces. I realize these things are important.

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
There is a file called conf/application.conf (https://github.com/rhinocerous/port-scanner/blob/master/conf/application.conf) which contains the application configuration for the port scanner application. Within this file on line 40 there is a database connection string which must be modified to work with the system onto which it is being installed. 
```
db.default.url="mysql://root:roscoe@localhost/port_scanner?characterEncoding=UTF-8"
```
In this example the username is root, password is roscoe, host is localhost, and database name is port_scanner. Update these values as needed. 

#### Running the code
Once configured you can start the port scanner application from the command line by navigating to the git root directory and issuing the following command.
`activator run
The first time the project is run it will take a while to compile as Activator downloads and installs a bunch of dependencies. Eventually the console will note that the server is online and listening on 0.0.0.0:9000. At that point the application can be seen on port 9000 of whatever system you are on. For example I am running the project on my local Windows machine and I can use the app by going to http://localhost:9000 in my browser.

## API Documentation
#### Scan hosts
 * endpoint: /scan
 * request method: POST
 * required header: "Content-Type:application/json"
 * required parameters (one or the other):
   *  host: JSON object with single hostname or IP address to scan e.g. {"host":"google.com"}
   *  hosts: JSON array of hostnames or IP addresses to scan e.g. {"hosts":["google.com","74.125.224.39"]}
   *  (if both host and hosts are sent, hosts will be ignored)
 * JSON response:
    
    ```
    [
    {
        "id": 13,
        "ip": "74.125.224.39",
        "hostname": "google.com",
        "scans": [
            {
                "id": 77,
                "hostId": 13,
                "inactivePortCount": 998,
                "validHost": "google.com",
                "ports": [
                    {
                        "id": 313,
                        "scanId": 77,
                        "port": 80,
                        "protocol": "tcp",
                        "state": "open",
                        "service": "http"
                    },
                    {
                        "id": 314,
                        "scanId": 77,
                        "port": 443,
                        "protocol": "tcp",
                        "state": "open",
                        "service": "https"
                    }
                ],
                "portsRemoved": [],
                "portsAdded": [],
                "created": 1420354714
            },
            {
                "id": 76,
                "hostId": 13,
                "inactivePortCount": 998,
                "validHost": "google.com",
                "ports": [
                    {
                        "id": 311,
                        "scanId": 76,
                        "port": 443,
                        "protocol": "tcp",
                        "state": "open",
                        "service": "https"
                    },
                    {
                        "id": 312,
                        "scanId": 76,
                        "port": 80,
                        "protocol": "tcp",
                        "state": "open",
                        "service": "http"
                    }
                ],
                "portsRemoved": null,
                "portsAdded": null,
                "created": 1420354496
            }
        ],
        "lastScan": 1420354714,
        "created": 1420343872
    }
]
    ```
#### Get scan history by hostname
 * endpoint: /host/:hostname?:page=1&:count=10
 * request method: GET
 * request parameters 
   * hostname: an IP or hostname that was previously looked up
   * page: desired page of results (defaults to 1)
   * count: desired items per page of results (defaults to 10)
 * JSON response:
  
   ```
   [
    {
        "id": 56,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 222,
                "scanId": 56,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 223,
                "scanId": 56,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 224,
                "scanId": 56,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 225,
                "scanId": 56,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 226,
                "scanId": 56,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            }
        ],
        "delta": null,
        "created": 1420348224
    },
    {
        "id": 52,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 202,
                "scanId": 52,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 203,
                "scanId": 52,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 204,
                "scanId": 52,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 205,
                "scanId": 52,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 206,
                "scanId": 52,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            }
        ],
        "delta": null,
        "created": 1420347998
    }
]
   ```

