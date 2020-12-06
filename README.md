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
   
### Assumptions when considering time cost
1. If the journey starts during peak hour, all subsequent stations are calculated with peak hour timings.  
   This is also true for normal and night hour services.
2. When calculating the routes, all stations are assumed to be officially opened.
3. As night hour ends when morning peak hour begins, to prevent a case of overlapping service or missing out on a min (usually at 6am), some fine tuning of the hours is made.
   - Night hour: 2200hrs to 0559hrs, with both start and end time inclusive.
   - Peak hours: 0600hrs to 0859 and 1800hrs to 2059hrs, with both start and end time inclusive.
   