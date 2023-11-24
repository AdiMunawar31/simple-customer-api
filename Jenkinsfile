pipeline {
    agent any
    tools {
        maven 'jenkins-maven'
    }

    environment {
        BUILD_NUMBER_ENV = "${env.BUILD_NUMBER}"
        TEXT_SUCCESS_BUILD = "[#${env.BUILD_NUMBER}] Project Name : ${JOB_NAME} is Success"
        TEXT_FAILURE_BUILD = "[#${env.BUILD_NUMBER}] Project Name : ${JOB_NAME} is Failure"
    }

    stages {
        stage('Git Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/AdiMunawar31/simple-customer-api']])
                bat 'mvn clean install'
                echo 'Git Checkout Completed'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube') {
                        bat ''' sonar:sonar -Dsonar.projectKey=rnd-springboot-3.0 -Dsonar.projectName='rnd-springboot-3.0' -Dsonar.host.url=http://localhost:9000 '''
                        echo 'SonarQube Analysis Completed'
                    }
                }
            }
        }

        stage("Quality Gate") {
            steps {
                waitForQualityGate abortPipeline: true
                echo 'Quality Gate Completed'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat 'docker build -t adimunawar31/rnd-springboot-3.0 .'
                    echo 'Build Docker Image Completed'
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhub-password')]) {
                        bat ''' docker login -u adimunawar31 -p "%dockerhub-password%" '''
                    }
                    bat 'docker push adimunawar31/rnd-springboot-3.0'
                }
            }
        }

        stage ('Docker Run') {
            steps {
                script {
                    bat 'docker run -d --name rnd-springboot-3.0 -p 8099:8080 adimunawar31/rnd-springboot-3.0'
                    echo 'Docker Run Completed'
                }
            }
        }

    }
    post {
        always {
            bat 'docker logout'
        }
    }
}