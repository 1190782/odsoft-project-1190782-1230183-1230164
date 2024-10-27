pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {
        stage('Build') {
            steps {
                script {
                    bat 'mvn clean install'
                }
            }
        }
    }
}