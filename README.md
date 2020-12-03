# Getting Started

### Requirements (Assuming using Ubuntu 16.04)
1. Install JDK 11
   - `sudo apt-get install openjdk-11-jdk`
2. Setting JAVA_HOME
   - Please follow the instructions [here](https://medium.com/@charinin/setting-java-home-environment-variable-in-ubuntu-e355c80e5b6c).
3. Chrome browser.

### Running the program
1. Open the terminal, navigate to the project directory.
   - `cd /trainroutes` 
2. Start the spring boot app.
   - `./mvmw spring-boot:run`
   - Open a browser (Chrome) and go `localhost:8080` to try the program.
3. Run tests.
   - `./mvnw test`
   