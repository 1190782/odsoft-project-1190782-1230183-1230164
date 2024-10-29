pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        DEPLOY_DIR = isUnix() ? '/path/to/deploy' : 'C:\\deploy'
        ARTIFACT_NAME = 'psoft-g1-0.0.1-SNAPSHOT.jar'
    }

    stages {
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

        stage('Unit Testing') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
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
                stage('Bookmanagement Testing') {
                    steps {
                        script {
                            if (isUnix()) {
                                sh 'mvn verify -Dtest=pt.psoft.g1.psoftg1.bookmanagement.model.BookTest'
                            } else {
                                bat 'mvn verify -Dtest=pt.psoft.g1.psoftg1.bookmanagement.model.BookTest'
                            }
                        }
                    }
                }

                stage('Readermanagement Testing') {
                    steps {
                        script {
                            if (isUnix()) {
                                sh 'mvn verify -Dtest=pt.psoft.g1.psoftg1.readermanagement.model.BirthDateTest'
                            } else {
                                bat 'mvn verify -Dtest=pt.psoft.g1.psoftg1.readermanagement.model.BirthDateTest'
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
                       sh "cp target/${ARTIFACT_NAME} ${DEPLOY_DIR}"
                   } else {
                       bat "copy target\\${ARTIFACT_NAME} ${DEPLOY_DIR}"
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
