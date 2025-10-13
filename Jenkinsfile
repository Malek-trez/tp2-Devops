pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-credentials-id')
        IMAGE_NAME = 'yourdockerhubusername/tp2-devops-app'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/Malek-trez/tp2-Devops'
            }
        }

        stage('Build with Maven') {
            steps {
                dir('tp2-devops') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Get Maven version
                    def versionRaw = sh(
                        script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
                        returnStdout: true
                    )
                    // Remove ANSI/control characters
                    def version = versionRaw.replaceAll("\\p{Cntrl}", "").trim()
                    
                    // Create Docker tag
                    def tag = "${version}-${env.BUILD_NUMBER}"
                    
                    // Build Docker image
                    sh "docker build -t ${IMAGE_NAME}:${tag} tp2-devops"
                    sh "docker tag ${IMAGE_NAME}:${tag} ${IMAGE_NAME}:latest"
                    
                    // Save tag for later stages
                    env.IMAGE_TAG = tag
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                script {
                    sh "echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin"
                    sh "docker push ${IMAGE_NAME}:${env.IMAGE_TAG}"
                    sh "docker push ${IMAGE_NAME}:latest"
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
