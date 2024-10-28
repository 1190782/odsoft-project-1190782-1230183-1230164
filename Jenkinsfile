pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Build') {
            steps {
                script {
                    bat 'mvn clean package'
                }
            }
        }

        stage('Unit Testing') {
            steps {
                bat 'mvn test'
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
                           bat 'mvn verify -Dtest=pt.psoft.g1.psoftg1.bookmanagement.model.BookTest'
                       }
                   }
               }

               stage('Readermanagement Testing') {
                   steps {
                       script {
                           bat 'mvn verify -Dtest=pt.psoft.g1.psoftg1.readermanagement.model.BirthDateTest'
                       }
                   }
               }
           }
       }

       stage('Report Results') {
           steps {
               script {
                   bat 'mvn site'
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
       stage('Deployment') {
            stage('Deploy to Local') {
                steps {
                    script {
                        bat 'copy target\\psoft-g1-0.0.1-SNAPSHOT.jar C:\\deploy'
                    }
                }
            }
       }
    }

    post {
        always {
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