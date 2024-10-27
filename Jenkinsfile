pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('SCM') {
            steps {
                git 'https://github.com/1190782/odsoft-project-1190782-1230183-1230164.git'
            }
        }

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