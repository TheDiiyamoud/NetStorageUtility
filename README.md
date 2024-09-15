# NetStorageUtility

A multi-threaded client/server file transferring/hosting utility over UDP.

## Features

- **Multi-threaded Architecture**: Efficiently handles multiple file transfer requests simultaneously.
- **UDP Protocol**: Utilizes the User Datagram Protocol for fast and lightweight file transfers.
- **File Hosting**: Allows users to host and share files easily over the network.
- **Cross-Platform**: Compatible with various operating systems.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Maven

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/TheDiiyamoud/NetStorageUtility.git
2. Navigate to the project directory and build the project
    ```bash
   cd NetStorageUtility && mvn clean install


### Usage
1. To run the `Server`, navigate to the `server/target` directory, and run:
     ```bash
     java -jar server-{VERSION}-SNAPSHOT.jar
2. Similarly, to run the `Client`, navigate to the `client/target` directory, and run:
     ```bash
     java -jar client-{VERSION}-SNAPSHOT.jar
Note that currently, the server needs to be running before a client is launched.

