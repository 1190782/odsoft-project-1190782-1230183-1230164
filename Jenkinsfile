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
               stage('Service Testing') {
                   steps {
                       script {
                           bat 'mvn verify -Dtest=pt.psoft.g1.psoftg1.bookmanagement.model.*'
                       }
                   }
               }

               stage('Database Testing') {
                   steps {
                       script {
                           bat 'mvn verify -Dtest=pt.psoft.g1.psoftg1.lendingmanagement.*'
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