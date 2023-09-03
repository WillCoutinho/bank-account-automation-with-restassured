# Bank Accounts Automation with Rest-Assured Framework

Walkthrough to install, set up and run the scenarios with Rest-Assured framework.
Used in this project Rest-Assured, JUnit5, Maven and Hamcrest for assertions. 

Scenarios for the API [Seu Barriga's Bank Accounts](http://seubarriga.wcaquino.me).

##
### Requirements for Windows 10
Install the latest [**Java JDK**](https://www.java.com/pt_BR/download/) and search for ``Start Menu > Type 'Environment' > click on 'Edit Environment Variables' > Environment Variables``.
> Oracle Guide: [**Enviroment Variables**](https://docs.oracle.com/en/database/oracle/machine-learning/oml4r/1.5.1/oread/creating-and-modifying-environment-variables-on-windows.html#GUID-DD6F9982-60D5-48F6-8270-A27EC53807D0)

- Create a new user variable called ``JAVA_HOME`` and set the path that Java were installed;
- Create a new variable at ``Path`` adding the value ``%JAVA_HOME%\bin``.

Download [**Apache Maven**](https://maven.apache.org/download.cgi), unzip at any folder (e.g.: /Downloads).
Create a new variable at ``Path`` and set the path that Apache Maven were unzipped (e.g.: ``C:\apache-maven-{version.number}\bin``)
> Maven installation guide: [**Installing Apache Maven**](https://maven.apache.org/install.html)

Install the latest [**GIT**](https://git-scm.com/download/win) to clone the repository.

##
### Requirements for Linux (Ubuntu)
Install the latest [**Java JDK**](https://www.java.com/pt_BR/download/), create environment variable ``JAVA_HOME`` and set the path that Java were installed.\
Set the folder ``\bin`` in Path environment.
> Oracle guide to config Java path on Linux [Enviroment Variables](https://www.java.com/pt_BR/download/help/path.xml)  

Download [**Apache Maven**](https://maven.apache.org/download.cgi) and unzip in a folder.\
Set the folder ``\bin`` in Path environment
> Maven installation guide: [**Installing Apache Maven**](https://maven.apache.org/install.html)  


Install **GIT**: ``sudo apt install git``  

##
### Clone the Repository and Install Packages

In your terminal (with admin privileges), run this command to clone project, install all dependencies and run the tests

```bash
git clone https://github.com/WillCoutinho/bank-account-automation-with-restassured && cd bank-account-automation-with-restassured && mvn install 
```

To run the suite tests use the command: ``mvn test``  

##

#### Git Repository

GitHub: [_Bank Accounts Automation with Rest-Assured Framework_](https://github.com/WillCoutinho/bank-account-automation-with-restassured)

##

#### Packages

- [Rest-Assured](https://rest-assured.io/) - API automation framework
- [JUnit5](https://junit.org/junit5/docs/current/user-guide/) - Test framework
- [Apache Maven](https://maven.apache.org/index.html) - Build tool
- [Maven Surefire](https://maven.apache.org/surefire/maven-surefire-plugin/) - Plugin for unit tests
- [JetBrains Annotations](https://www.jetbrains.com/help/idea/annotating-source-code.html) - Set of Java annotations
