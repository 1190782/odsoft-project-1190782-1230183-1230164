pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {
        stage('Clean') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean'
                    } else {
                        bat 'mvn clean'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn compile'
                    } else {
                        bat 'mvn compile'
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn package -DskipTests'
                    } else {
                        bat 'mvn package -DskipTests'
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

        stage('Checkstyle') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn checkstyle:checkstyle -Dcheckstyle.failOnViolation=false -Dmaven.test.failure.ignore=true'
                    } else {
                        bat 'mvn checkstyle:checkstyle -Dcheckstyle.failOnViolation=false -Dmaven.test.failure.ignore=true'
                    }
                }
            }
        }

        stage('Publish Checkstyle Report') {
            steps {
                publishHTML([
                    reportDir: 'target/checkstyle',
                    reportFiles: 'checkstyle-result.xml',
                    reportName: 'Checkstyle Report',
                    keepAll: true,
                    allowMissing: false,
                    alwaysLinkToLastBuild: true
                ])
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
                        sh 'cp target/psoft-g1-0.0.1-SNAPSHOT.jar /path/to/deploy'
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
