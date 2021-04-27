pipeline {
    agent none
    stages {
        stage('Build Jar') {
            agent {
                docker {
                    // use maven image to build app
                    image 'maven:3-alpine'
                    // volume mapping to cache maven plugins
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Image') {
            steps {
                script {
                	app = docker.build("anishst/selenium-docker")
                }
            }
        }
        stage('Push Image') {
            steps {
                script {
                    // upload to docker hub: https://hub.docker.com/repository/docker/anishst/selenium-docker
                    // docker_hub = jenkins dockerhub credentials; need to setup in Jenkins host
			        docker.withRegistry('https://registry.hub.docker.com', 'docker_hub') {
                        // push using Jenkins build number and latest tags
			        	app.push("${BUILD_NUMBER}")
			            app.push("latest")
			        }
                }
            }
        }
    }
}