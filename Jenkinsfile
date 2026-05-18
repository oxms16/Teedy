pipeline {
    agent any

    parameters {
        string(name: 'DOCKER_IMAGE', defaultValue: 'your-dockerhub-username/teedy-app', description: 'Docker Hub repository, for example: username/teedy-app')
        string(name: 'DOCKER_HUB_CREDENTIALS', defaultValue: 'dockerhub_credentials', description: 'Jenkins credentials ID for Docker Hub')
    }

    environment {
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Validate configuration') {
            steps {
                script {
                    if (!params.DOCKER_IMAGE?.trim() || params.DOCKER_IMAGE == 'your-dockerhub-username/teedy-app') {
                        error 'Set DOCKER_IMAGE to your Docker Hub repository, for example: username/teedy-app'
                    }
                    if (!params.DOCKER_HUB_CREDENTIALS?.trim()) {
                        error 'Set DOCKER_HUB_CREDENTIALS to your Jenkins Docker Hub credentials ID'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Build Docker image') {
            steps {
                script {
                    docker.build("${params.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                }
            }
        }

        stage('Push Docker image') {
            steps {
                script {
                    retry(3) {
                        docker.withRegistry('https://index.docker.io/v1/', params.DOCKER_HUB_CREDENTIALS) {
                            docker.image("${params.DOCKER_IMAGE}:${env.DOCKER_TAG}").push()
                            docker.image("${params.DOCKER_IMAGE}:${env.DOCKER_TAG}").push('latest')
                        }
                    }
                }
            }
        }

        stage('Run containers') {
            steps {
                script {
                    [8082, 8083, 8084].each { port ->
                        sh "docker stop teedy-container-${port} || true"
                        sh "docker rm teedy-container-${port} || true"
                        docker.image("${params.DOCKER_IMAGE}:${env.DOCKER_TAG}").run("--name teedy-container-${port} -d -p ${port}:8080")
                    }
                    sh 'docker ps --filter "name=teedy-container"'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/target/**/*.war', fingerprint: true
            archiveArtifacts artifacts: '**/target/**/*.jar', fingerprint: true, allowEmptyArchive: true
            junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
        }
    }
}
