# ODSOFT Project 2024/2025 - CI/CD and Automated Testing

## Introduction
This project focuses on automating CI/CD and testing processes for a library management system. The system offers REST endpoints to manage information on books, genres, authors, readers, and loans. This project phase aims to improve the system's variability, configurability, reliability, and automation.

The main goals include implementing a CI/CD pipeline in Jenkins, covering build, packaging, code analysis, test execution, and deployment for local and remote environments. The project also explores advanced testing practices, including unit, integration, acceptance, and mutation testing, focusing on code coverage and reporting the results.

## Project Goals
1. **CI/CD Automation**  
   Configure and integrate Jenkins for a complete pipeline, including:
   - Version control (SCM)
   - Build and packaging (Build and Package)
   - Artifact generation
   - Static code analysis (e.g., SonarQube)

   In this section we will explain our thinking proccess and what we decided in order the results the team had.

2. **Automated Testing**
   - **Unit Tests**: Divided into black-box and white-box testing of domain classes.
   - **Mutation Testing**: Checking test effectiveness via mutation testing.
   - **Integration Tests**: Verifying interactions between controllers, services, and repositories.
   - **Acceptance Testing**: Validating expected system behavior.

3. **Performance and Documentation**
   - Document the system's initial state (design through reverse engineering).
   - Critically analyze the pipeline's performance over time, providing evidence of improvements.

## Project Structure
- **src/main/java**: Contains the system implementation.
- **src/test/java**: Contains unit, integration, and mutation tests.
- **Jenkinsfile**: Configuration file for the Jenkins CI/CD pipeline.
- **Documentation**: File detailing all the team decisions during the project.

## CI/CD Pipeline
A pipeline automates the software development process, ensuring code is consistently built, tested, and deployed. It integrates continuous integration (CI) and continuous delivery (CD) practices, reducing manual steps, catching bugs earlier. This leads to faster, more reliable deployments and higher code quality.

1. **Source Control Management (SCM)**  
   We started by implement the Source Control Management. This stage manages changes to source code over time. It keaps tracking of the code revision. It helps team collaboration by avoiding conflicts through branching and merging.
   It will mainly insure that the last code is built and tested automatically identifying issues earlier.
   For this step, we developed a stage that has the following command:

   ```groovy
    stage('SCM') {
            steps {
                checkout scm
            }
        }
   ```

   The checkout scm command will retrieve the source code from the version control system. In this case the team used Git. It will ensure that the pipeline is dealing with the last code version and everything is up to date.

2. **Build and Packaging**  
   In this tage, the team focused on compiling the source code and creating a deployable package. The build, has usual, will resolves dependencies, ensuring that the libraries and components are included in the final artifact. This step is one of the most importants because it will transform the source code into an application possible to be run. 
   The team implement a stage dedicated to building and packaging the application:

   ```groovy
    stage('Build and Packaging') {
        steps {
            script {
                if (isUnix()) {
                    sh 'mvn clean package'
                } else {
                    bat 'mvn clean package'
                }
            }
        }
    }
   ```

   The **mvn clean package** command invokes Maven which will first clean previous builds and then compiles the source code. This ensure that a fresh build is done everytime the pipeline is run.

3. **Static Code Analysis**  
    In this stage, we focused on guarantee the quality and maintainability of the code through the static analysis. This stage will check potential issues, such as code smells, bugs and vulnerabilites. With this step we can improve code quality.

    For this project, we implemented SonarQube and ScheckStyle as our static code analysis.

    ```groovy
    stage('Scan & Checkstyle') {
           parallel {
               stage('Scan') {
                   when { expression { !isUnix() } }  // Run this stage only on Windows
                   steps {
                       withSonarQubeEnv('sq-odsoft') {
                           withCredentials([string(credentialsId: 'sonar', variable: 'SONAR_TOKEN')]) {
                               bat "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar -Dsonar.token=${SONAR_TOKEN} -Dsonar.java.binaries=target\\classes"
                           }
                       }
                   }
               }
               stage('Checkstyle') {
                   steps {
                       script {
                           if (isUnix()) {
                               sh 'mvn checkstyle:checkstyle -Dcheckstyle.failOnViolation=false'
                           } else {
                               bat 'mvn checkstyle:checkstyle -Dcheckstyle.failOnViolation=false'
                           }
                       }
                   }
               }
           }
       }
    ```

    We will discuss in another section why we decided to implement this two steps in parallel but for now we will give a brief explanation of what is happening in this stage.

    Sonarqube is the first detailed stage and is used for continuous inspection of code quality. It analyzes and provides detailed reports on multiple metrics, including code coverage, complexity, duplications, etc. 
    For this step we had to create in the jenkins a credential with the Id='sonar' and use it as a variable to the command to use. 
    The **mvn sonar:sonar** command uses the token and triggers the Sonar analysis. By analysing the compiled classes defined with the locatin _target/classes_ directory, SonarQube will initiathe the analysis and generate the reports.

    With the sonarqube, we implemented CheckStyle to guarantee coding standards and best practices. This tool will check the source code with a set of defined rules. This rules are defined in the checkstyle.xml file:

    ![CheckStyleRules](readmeImages/image.png)

    We only defined one rule: LineLength. This is the only rule defined because the source code had a lot of problems. In order for us to confirm that CheckStyle was working properly, this rule was enough. We can see in the image that the max lenght value is set to 1000. This way, it won't return any error. If we set it to 500, for example, it will return 3 errors. With the usage of the flasgs failOnViolation=false, the pipeline always runs properly even if the Checkstyle detect some needed improvements. This way the user can check the report generated and see what problems must be solved but it won't block the pipeline.

    After this parallel stages we have the CheckStyle report publish:

    ```groovy
    stage('Publish Checkstyle Report') {
            steps {
                publishHTML([
                    reportDir: 'target/site',
                    reportFiles: 'checkstyle.html',
                    reportName: 'Checkstyle Report',
                    keepAll: true,
                    allowMissing: false,
                    alwaysLinkToLastBuild: true
                ])
            }
        }
    ```

    It will generate a checkstyle.html file in the specified directory: target/site.

    In jenkins we can see the SonarQube and CheckStyle report in the left panel of the pipeline run:

    ![SonarQubeAndCheckStyle](readMeImages/image2.png)
   
4. **Test Execution**  
   - **Unit Testing**: Runs unit tests to validate domain classes.
   - **Integration Testing**: Verifies interactions between system components.
   - **Mutation Testing**: Assesses unit tests' robustness.
   - **Coverage Reports**: Generates code coverage reports using JaCoCo.

5. **Deployment**  
   - Automated deployment of the `.jar` artifact to both the local environment and the remote server at ISEP.

## Configurations and Tools Used
- **Jenkins**: Automation of the CI/CD pipeline.
- **SonarQube**: Tool for static code analysis.
- **JaCoCo**: Tool for test coverage.
- **Postman**: Used for API testing and request verification.

## Technical Decisions and Justifications
- **Test Automation**: We chose to run tests in parallel to optimize the pipeline's runtime by separating unit and integration tests.
- **Deployment Environment**: Configured Jenkins to deploy both locally and remotely at ISEP, ensuring flexibility and redundancy.
- **Coverage and Quality**: Leveraged SonarQube for code quality checks to identify improvement areas.

## Results and Analysis
Below is a summary of test results and performance analysis observed during the project:

1. **Test Coverage**: JaCoCo reports indicated satisfactory code coverage, with an increase of X% due to integration and mutation testing.
2. **Code Quality**: SonarQube analysis highlighted improvements in code complexity and duplication.
3. **Pipeline Performance**: The pipeline runtime was optimized by X% throughout the project, reducing build and test parallel execution times.

## Conclusion
This project provided a comprehensive experience in automating development and testing processes using modern tools like Jenkins and SonarQube. Implementing a robust CI/CD pipeline and automated testing proved essential to ensure the library management system's reliability and scalability.

## Contributions
- Student Name 1 - Primary tasks and responsibilities
- Student Name 2 - Primary tasks and responsibilities
- Student Name 3 (optional) - Primary tasks and responsibilities

## References
- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [SonarQube Documentation](https://docs.sonarqube.org/)
- [JaCoCo Documentation](https://www.eclemma.org/jacoco/trunk/doc/)

---

**Note**: For more details on the system execution and usage, please refer to the source code and included documentation files.
