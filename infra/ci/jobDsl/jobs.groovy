repo = 'yatol/yatol'
githuburl = 'https://github.com/yatol/yatol.git'

// Create jobs for static branches
['develop', 'master'].each { branch ->

    // define jobs
    def buildJob = defaultBuildJob(branch, true)
    def intTestJob = job("${branch}_integrationTestDocker")
    def acceptanceJobLinux = job("${branch}_acceptanceTestLinuxDocker")
    def acceptanceJobWindows = job("${branch}_acceptanceTestWindowsDocker")

    // configure build job
    buildJob.with {
        publishers {
            downstreamParameterized {
                trigger(intTestJob.name) {
                    predefinedProp('commit', '${GIT_COMMIT}')
                }
            }
        }
    }

    // configure integrationTestDocker job
    intTestJob.with {
        scm {
            git (githuburl, '${commit}', gitConfigure('${commit}', true))
        }
        steps {
            gradle 'integrationTestDocker'
        }
        publishers {
	        downstreamParameterized {
	            trigger(acceptanceJobLinux.name) {
	                predefinedProp('commit', '${GIT_COMMIT}')
	            }
	        }
          archiveJunit '**/app/**/build/test-results/*.xml'
	      }
    }

  	acceptanceJobLinux.with {
          scm {
              git (githuburl, '${commit}', gitConfigure('${commit}', true))
          }
          steps {
              gradle 'runAcceptanceTestLinux'
          }
          publishers {
  	        downstreamParameterized {
  	            trigger(acceptanceJobWindows.name) {
  	                predefinedProp('commit', '${GIT_COMMIT}')
  	            }
  	        }
  	      }
          configure(fitNesseConfigure('**/acceptanceTest/YatolTests/latestResult.xml'))
    }

  	acceptanceJobWindows.with {
          scm {
              git (githuburl, '${commit}', gitConfigure('${commit}', true))
          }
          steps {
              gradle 'runAcceptanceTestWindows'
          }
          if (branch == 'master') {
              publishers {
                  downstream 'master_release'
              }
          }
          configure(fitNesseConfigure('**/acceptanceTest/YatolTests/latestResult.xml'))
  	}
}

job('master_release') {
    scm {
        cloneWorkspace('master_build')
    }
    steps {
        gradle 'release'
        gradle 'pushDockerImage'
    }
}

// Create jobs for feature branches
def branchApi = new URL("https://api.github.com/repos/$repo/branches")
def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())

branches.findAll { it.name.startsWith('feature/') }.each { branch ->
    defaultBuildJob(branch.name, false)
}

/**
 * Defines how a default build job should look like.
 */
def defaultBuildJob(String branch, boolean clean) {
    def jobName = "${branch}_build".replaceAll('/', '_')
    def buildJob = job(jobName) {
        description "Performs a build on branch: $branch"
        scm {
              git (
                  githuburl,
                  branch,
                  gitConfigure(branch, true)
              )
        }
        triggers {
            scm 'H/3 * * * *'
        }
        steps {
            if (clean) {
                gradle 'clean build'
            } else {
                gradle 'build'
            }
        }
        publishers {
            jacocoCodeCoverage {
                execPattern '**/**.exec'
            }
            archiveJunit '**/app/**/build/test-results/*.xml'
        }
    }
    return buildJob
}

/**
 * Git configuration section.
 */
def gitConfigure(branchName, skippingTag) {
    { node ->
        // checkout to local branch
        node / 'extensions' / 'hudson.plugins.git.extensions.impl.LocalBranch' / localBranch(branchName)

        // checkout to local branch
        node / 'skipTag'(skippingTag)
    }
}

/**
 * FitNesse plugin configuration section.
 */
def fitNesseConfigure(path) {
    { project ->
      project / 'publishers' / 'hudson.plugins.fitnesse.FitnesseResultsRecorder' {
        'fitnessePathToXmlResultsIn'(path)
      }
    }
}
