def CONTAINER_NAME = "cicd"
def DOCKER_HUB_USER = "kontafer"
def APP_HTTP_PORT = "80"
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
	
	stage('Take and run image from DockerHub') {
		sh "docker pull $DOCKER_HUB_USER/$CONTAINER_NAME:$env.CONTAINER_TAG"
	}
}
