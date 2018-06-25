pipelineJob("CI_Job") {
	triggers {
		scm('H/5 * * * *')
	}
	definition {
		cpsScm {
			scm {
				git {
					remote {
						url("https://github.com/Kontafer/jenktest")
						credentials("jenkins-github")
					}
					branch("refs/tags/*")
				}
			}
			scriptPath("Jenkins/CI_job.groovy")
		}
	}
}

pipelineJob("CD_job") {
    triggers {
	upstream('CI_job')
    }
    parameters {
	gitParam('CONTAINER_TAG') {
	    description('Select tag of image')
	    type('TAG')
	    sortMode('DESCENDING_SMART')
	    defaultValue('latest')
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url("https://github.com/Kontafer/jenktest")
                        credentials("jenkins-github")
                    }
                    branch("refs/tags/*")
                }
            }
            scriptPath("Jenkins/CD_job.groovy")
        }
    }
}
