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
        stage('Prepare Version') {
            steps {
                script {
                    // Read version from pom.xml using grep/sed (works on Linux agents)
                    def versionRaw = sh(
                        script: "grep -m1 '<version>' tp2-devops/pom.xml | sed -E 's/.*<version>(.*)<\\/version>.*/\\1/'",
                        returnStdout: true
                    )
                    // Remove control characters and trim whitespace
                    def version = versionRaw.replaceAll("\\p{Cntrl}", "").trim()

                    // Build Docker tag
                    env.IMAGE_TAG = "${version}-${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${IMAGE_NAME}:${env.IMAGE_TAG} tp2-devops"
                    sh "docker tag ${IMAGE_NAME}:${env.IMAGE_TAG} ${IMAGE_NAME}:latest"
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
