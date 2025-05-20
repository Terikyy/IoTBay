# IoTBay

IoTBay is a university project focused on providing an online marketplace for IoT devices. It supports features for customers, staff, and administrators, with a backend written in **Java** and a frontend styled with **CSS**. The project is built with Maven and runs on a **Tomcat server**.

## Features

- **Customer**:
  - User authentication and account management
  - Product browsing and shopping cart functionality
  - Order placement and tracking
- **Staff**:
  - Inventory management
- **Admin**:
  - User management

## Getting Started

### Prerequisites

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) 11 or later
- [Maven](https://maven.apache.org/)
- [Apache Tomcat](https://tomcat.apache.org/) server
- [Visual Studio Code (VS Code)](https://code.visualstudio.com/)

### Setup and Run in VS Code

1. **Clone the Repository**:
   Open a terminal in VS Code and run:
   ```bash
   git clone https://github.com/Terikyy/IoTBay.git
   cd IoTBay
   ```

2. **Open the Project**:
   Launch VS Code and open the `IoTBay` folder.

3. **Install Extensions**:
   Install the following recommended extensions:
   - **Java Extension Pack** (for Java development)
   - **Tomcat for Java** (for deploying to Tomcat)

4. **Configure Tomcat**:
   - Install [Apache Tomcat](https://tomcat.apache.org/) and set it up on your machine.
   - In VS Code, open the Command Palette (`Ctrl+Shift+P`) and search for `Tomcat: Add Tomcat Server`.
   - Point it to your local Tomcat installation directory.

5. **Deploy the Application**:
   - Build the project with Maven:
     ```bash
     mvn clean package
     ```
   - Use the **Tomcat for Java** extension:
     - Right-click on `target/IoTBay.war` in the VS Code Explorer.
     - Select **Run on Tomcat Server**.

6. **Access the Application**:
   Start the Tomcat server and open the application in your browser at:
   ```
   http://localhost:8080/webapp/
   ```
   
## Additional Notes
On resetting a users password, the password is set to null, so any password works, until the password is changed
