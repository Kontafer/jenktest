def CONTAINER_NAME = "cicd"
def DOCKER_HUB_USER = "kontafer"
def APP_HTTP_PORT = "80"
def HOST = "local"

node {
	stage('Initialize') {
		def dockerHome = tool 'myDocker'
		env.PATH = "${dockerHome}/bin:${env.PATH}"
	}

    stage('Stop image') {
        try {
            sh "docker stop $CONTAINER_NAME"
            } catch (error) { 
        }
    }

    stage('Add from docker hub') {
			IMAGE_NAME = DOCKER_HUB_USER + "/" + CONTAINER_NAME + ":" + env.PARAM_GIT
			sh "docker pull $IMAGE_NAME"
			sh "docker run -d --rm -p $APP_HTTP_PORT:$APP_HTTP_PORT --name $CONTAINER_NAME docker.io/$IMAGE_NAME"          
        }
        echo "Image $IMAGE_NAME:${env.PARAM_GIT} was run successfully"
    }
}
