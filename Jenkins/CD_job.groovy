def CONTAINER_NAME = "cicd"
def DOCKER_HUB_USER = "kontafer"
def APP_HTTP_PORT = "5050"
def HOST = "local"

node {
	stage('Initialize') {
		def dockerHome = tool 'myDocker'
		env.PATH = "${dockerHome}/bin:${env.PATH}"
	}

	stage('Stop working Image') {
		sleep 5
		sh "docker stop $CONTAINER_NAME"
	}
	
	stage('Take and run Image from DockerHub') {
		sh "docker pull $DOCKER_HUB_USER/$CONTAINER_NAME:${env.CONTAINER_TAG}"
		sh "docker run -d --rm -p $APP_HTTP_PORT:$APP_HTTP_PORT --name $CONTAINER_NAME docker.io/$DOCKER_HUB_USER/$CONTAINER_NAME:${env.CONTAINER_TAG}"
	}
}
