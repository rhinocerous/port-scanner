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
    },
    {
        "id": 51,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 197,
                "scanId": 51,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 198,
                "scanId": 51,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 199,
                "scanId": 51,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 200,
                "scanId": 51,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 201,
                "scanId": 51,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            }
        ],
        "delta": null,
        "created": 1420347944
    },
    {
        "id": 50,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 192,
                "scanId": 50,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 193,
                "scanId": 50,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 194,
                "scanId": 50,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 195,
                "scanId": 50,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 196,
                "scanId": 50,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            }
        ],
        "delta": null,
        "created": 1420347846
    },
    {
        "id": 49,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 187,
                "scanId": 49,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 188,
                "scanId": 49,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 189,
                "scanId": 49,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 190,
                "scanId": 49,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 191,
                "scanId": 49,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            }
        ],
        "delta": null,
        "created": 1420347422
    },
    {
        "id": 48,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 182,
                "scanId": 48,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 183,
                "scanId": 48,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 184,
                "scanId": 48,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 185,
                "scanId": 48,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 186,
                "scanId": 48,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            }
        ],
        "delta": null,
        "created": 1420347377
    },
    {
        "id": 47,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 177,
                "scanId": 47,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 178,
                "scanId": 47,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 179,
                "scanId": 47,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 180,
                "scanId": 47,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 181,
                "scanId": 47,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            }
        ],
        "delta": null,
        "created": 1420347350
    },
    {
        "id": 46,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 172,
                "scanId": 46,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 173,
                "scanId": 46,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 174,
                "scanId": 46,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 175,
                "scanId": 46,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 176,
                "scanId": 46,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            }
        ],
        "delta": null,
        "created": 1420347283
    },
    {
        "id": 45,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 167,
                "scanId": 45,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 168,
                "scanId": 45,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 169,
                "scanId": 45,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 170,
                "scanId": 45,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 171,
                "scanId": 45,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            }
        ],
        "delta": null,
        "created": 1420347153
    },
    {
        "id": 44,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 162,
                "scanId": 44,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 163,
                "scanId": 44,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 164,
                "scanId": 44,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 165,
                "scanId": 44,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 166,
                "scanId": 44,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            }
        ],
        "delta": null,
        "created": 1420347049
    },
    {
        "id": 43,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 157,
                "scanId": 43,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 158,
                "scanId": 43,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 159,
                "scanId": 43,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 160,
                "scanId": 43,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 161,
                "scanId": 43,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            }
        ],
        "delta": null,
        "created": 1420346951
    },
    {
        "id": 42,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 152,
                "scanId": 42,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 153,
                "scanId": 42,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 154,
                "scanId": 42,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 155,
                "scanId": 42,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 156,
                "scanId": 42,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            }
        ],
        "delta": null,
        "created": 1420346774
    },
    {
        "id": 39,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 140,
                "scanId": 39,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 141,
                "scanId": 39,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 142,
                "scanId": 39,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 143,
                "scanId": 39,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 144,
                "scanId": 39,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            }
        ],
        "delta": null,
        "created": 1420344000
    },
    {
        "id": 37,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 133,
                "scanId": 37,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 134,
                "scanId": 37,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 135,
                "scanId": 37,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 136,
                "scanId": 37,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 137,
                "scanId": 37,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            }
        ],
        "delta": null,
        "created": 1420343877
    },
    {
        "id": 33,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 122,
                "scanId": 33,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 123,
                "scanId": 33,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 124,
                "scanId": 33,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 125,
                "scanId": 33,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 126,
                "scanId": 33,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            }
        ],
        "delta": null,
        "created": 1420332932
    },
    {
        "id": 32,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 117,
                "scanId": 32,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 118,
                "scanId": 32,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 119,
                "scanId": 32,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 120,
                "scanId": 32,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 121,
                "scanId": 32,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            }
        ],
        "delta": null,
        "created": 1420332851
    },
    {
        "id": 31,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 112,
                "scanId": 31,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 113,
                "scanId": 31,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 114,
                "scanId": 31,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 115,
                "scanId": 31,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 116,
                "scanId": 31,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            }
        ],
        "delta": null,
        "created": 1420332187
    },
    {
        "id": 30,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 107,
                "scanId": 30,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 108,
                "scanId": 30,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 109,
                "scanId": 30,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 110,
                "scanId": 30,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 111,
                "scanId": 30,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            }
        ],
        "delta": null,
        "created": 1420331859
    },
    {
        "id": 29,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 102,
                "scanId": 29,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 103,
                "scanId": 29,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 104,
                "scanId": 29,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            },
            {
                "id": 105,
                "scanId": 29,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 106,
                "scanId": 29,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            }
        ],
        "delta": null,
        "created": 1420331787
    },
    {
        "id": 28,
        "hostId": 10,
        "inactivePortCount": 995,
        "validHost": "boldride.com",
        "ports": [
            {
                "id": 97,
                "scanId": 28,
                "port": 25,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtp"
            },
            {
                "id": 98,
                "scanId": 28,
                "port": 587,
                "protocol": "tcp",
                "state": "closed",
                "service": "submission"
            },
            {
                "id": 99,
                "scanId": 28,
                "port": 443,
                "protocol": "tcp",
                "state": "closed",
                "service": "https"
            },
            {
                "id": 100,
                "scanId": 28,
                "port": 465,
                "protocol": "tcp",
                "state": "closed",
                "service": "smtps"
            },
            {
                "id": 101,
                "scanId": 28,
                "port": 80,
                "protocol": "tcp",
                "state": "open",
                "service": "http"
            }
        ],
        "delta": null,
        "created": 1420331737
    }
]
   ```

