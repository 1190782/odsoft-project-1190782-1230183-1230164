pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
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

         stage('Run Unit Tests') {
             parallel {
                 stage('Mutation Tests') {
                     steps {
                         script {
                             if (isUnix()) {
                                 sh 'mvn test -Dtest=pt.psoft.g1.psoftg1.unitTests.mutationTests.**.*Tests'
                             } else {
                                 bat 'mvn test -Dtest=pt.psoft.g1.psoftg1.unitTests.mutationTests.**.*Tests'
                             }
                         }
                     }
                 }
                 stage('Opaque and Transparent Tests') {
                     steps {
                         script {
                             if (isUnix()) {
                                 sh 'mvn test -Dtest=pt.psoft.g1.psoftg1.unitTests.opaqueAndTransparentTests.**.*Test'
                             } else {
                                 bat 'mvn test -Dtest=pt.psoft.g1.psoftg1.unitTests.opaqueAndTransparentTests.**.*Test'
                             }
                         }
                     }
                 }
             }
         }

        stage('Jacoco Report') {
            steps {
                jacoco execPattern: '**/target/jacoco.exec',
                       classPattern: '**/target/classes',
                       sourcePattern: '**/src/main/java',
                       inclusionPattern: '**/*.class'
            }
        }

        stage('Publish JaCoCo Report') {
            steps {
                publishHTML([
                    reportDir: 'target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report',
                    keepAll: true,
                    allowMissing: false,
                    alwaysLinkToLastBuild: true
                ])
            }
        }

        stage('Integration Testing') {
            parallel {
                stage('Controllers Testing') {
                    steps {
                        script {
                            if (isUnix()) {
                                sh 'mvn verify -Dtest=pt.psoft.g1.psoftg1.integrationTests.controllers.**.*Test -Dskip.unit.tests=true'
                            } else {
                                bat 'mvn verify -Dtest=pt.psoft.g1.psoftg1.integrationTests.controllers.**.*Test -Dskip.unit.tests=true'
                            }
                        }
                    }
                }
                stage('Services Testing') {
                    steps {
                        script {
                            if (isUnix()) {
                                sh 'mvn verify -Dtest=pt.psoft.g1.psoftg1.integrationTests.services.**.*Test -Dskip.unit.tests=true'
                            } else {
                                bat 'mvn verify -Dtest=pt.psoft.g1.psoftg1.integrationTests.services.**.*Test -Dskip.unit.tests=true'
                            }
                        }
                    }
                }
                stage('Repository Testing') {
                    steps {
                        script {
                            if (isUnix()) {
                                sh 'mvn verify -Dtest=pt.psoft.g1.psoftg1.integrationTests.repository.**.*Test -Dskip.unit.tests=true'
                            } else {
                                bat 'mvn verify -Dtest=pt.psoft.g1.psoftg1.integrationTests.repository.**.*Test -Dskip.unit.tests=true'
                            }
                        }
                    }
                }
            }
        }

        stage('Report Results') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn site'
                    } else {
                        bat 'mvn site'
                    }
                }
            }
        }

        stage('Publish Site Report') {
            steps {
                publishHTML([
                    reportDir: 'target/site',
                    reportFiles: 'index.html',
                    reportName: 'Project Site',
                    keepAll: true,
                    allowMissing: false,
                    alwaysLinkToLastBuild: true
                ])
            }
        }

        stage('Deploy Local') {
            steps {
                script {
                    if (isUnix()) {
                        withCredentials([sshUserPrivateKey(credentialsId: 'TOKEN_SSH_ID', keyVariable: 'SSH_KEY')]) {
                            sh 'scp -o StrictHostKeyChecking=no -i $SSH_KEY target/psoft-g1-0.0.1-SNAPSHOT.jar root@vs1215.dei.isep.ipp.pt:/opt/myapp/psoft-g1/'
                        }
                    } else {
                        bat 'copy target\\psoft-g1-0.0.1-SNAPSHOT.jar C:\\deploy'
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed. Cleaning workspace...'
            cleanWs()
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
