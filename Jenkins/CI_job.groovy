def CONTAINER_NAME = "cicd"
def CONTAINER_TAG = ''
def DOCKER_HUB_USER = "kontafer"
def APP_HTTP_PORT = "5050"
def IMAGE_NAME = ''

node {
	stage('Initialize') {
		def dockerHome = tool 'myDocker'
		env.PATH = "${dockerHome}/bin:${env.PATH}"
	}

	stage('Checkout') {
		deleteDir()
		checkout scm
	}
	
	stage('Buildingg') {
 		
	commit = getCommit()
    if (commit) {
    	desc = sh(script: "git describe --tags ${commit}", returnStdout: true)?.trim()
		if (isTag(desc)) {
			return desc
			$CONTAINER_TAG = desc
        }
    }
    return null
	
		IMAGE_NAME = DOCKER_HUB_USER + "/" + CONTAINER_NAME + ":" + CONTAINER_TAG
		sh "docker build -t $DOCKER_HUB_USER/$CONTAINER_NAME:$CONTAINER_TAG --pull --no-cache ."
		echo "Image $IMAGE_NAME build complete"
	}

	stage('Unit tests') {
		sh "docker run -d --rm -p $APP_HTTP_PORT:$APP_HTTP_PORT --name $CONTAINER_NAME $IMAGE_NAME"
		sleep 5
		status = sh(returnStdout: true, script: "docker inspect $CONTAINER_NAME --format='{{.State.Status}}'").trim()
		if (status != 'running') {
			currentBuild.result = 'FAILED'
			sh "exit ${status}"
		} 
	}

	stage('Push to dockerhub') {
		withCredentials([usernamePassword(credentialsId: 'dockerHubAccount', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
			sh "docker login -u $DOCKER_HUB_USER -p $PASSWORD"
			sh "docker tag $DOCKER_HUB_USER/$CONTAINER_NAME $DOCKER_HUB_USER/$DOCKER_HUB_USER/$CONTAINER_NAME"
			sh "docker push $DOCKER_HUB_USER/$CONTAINER_NAME:$CONTAINER_TAG"
			echo "Image $IMAGE_NAME push complete"
		}
	}
}		
