def CONTAINER_NAME = "cicd"
def CONTAINER_TAG = 'latest'
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
	
	stage('Buildddd') {
 		IMAGE_NAME = DOCKER_HUB_USER + "/" + CONTAINER_NAME + ":" + CONTAINER_TAG
		sh "docker build -t $DOCKER_HUB_USER/$CONTAINER_NAME:$CONTAINER_TAG --pull --no-cache ."
		echo "Image $IMAGE_NAME build complete"
	}





docker inspect cicd --format='{{.State.Status}}'



	stage('Unit tests'){
		status = sh(returnStdout: true, script: "docker inspect $CONTAINER_NAME --format='{{.State.Status}}'").trim()
		if (statis.compareTO('running')) {
			currentBuild.result = 'FAILED'
			sh "exit ${status}"
		} 
	}

	stage('Push to dockerhub') {
		withCredentials([usernamePassword(credentialsId: 'dockerHubAccount', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
			sh "docker login -u $DOCKER_HUB_USER -p $PASSWORD"
			sh "docker tag $CONTAINER_NAME:$CONTAINER_TAG $IMAGE_NAME"
			sh "docker push $IMAGE_NAME"
			echo "Image $IMAGE_NAME push complete"
			IMAGE_NAME_LATEST = DOCKER_HUB_USER + "/" + CONTAINER_NAME + ":latest"
			sh "docker tag $CONTAINER_NAME:$CONTAINER_TAG $IMAGE_NAME_LATEST"
			sh "docker push $IMAGE_NAME_LATEST"
			echo "Image $IMAGE_NAME_LATEST update complete"
		}
	}
}		
