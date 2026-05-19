pipeline {
    agent any
    environment {
        DOCKER_HUB_CREDENTIALS = 'dockerhub_credentials'
        DOCKER_IMAGE = 'oxms16/seteedy'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Building image') {
            steps {
                script {
                    docker.build("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                }
            }
        }

        stage('Upload image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_HUB_CREDENTIALS) {
                        docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").push()
                        docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").push('latest')
                    }
                }
            }
        }

        stage('Run containers') {
            steps {
                script {
                    sh 'docker stop seteedy-container-8082 seteedy-container-8083 seteedy-container-8084 || true'
                    sh 'docker rm seteedy-container-8082 seteedy-container-8083 seteedy-container-8084 || true'
                    docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").run('--name seteedy-container-8082 -d -p 8082:8080')
                    docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").run('--name seteedy-container-8083 -d -p 8083:8080')
                    docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").run('--name seteedy-container-8084 -d -p 8084:8080')
                    sh 'docker ps --filter "name=seteedy-container"'
                }
            }
        }
    }
}
