repo = 'yatol/yatol'
githuburl = 'https://github.com/yatol/yatol.git'

// Create jobs for static branches
['develop', 'master'].each { branch ->

    // define jobs
    def buildJob = defaultBuildJob(branch, true)
    def intTestJob = job("${branch}_integrationTestDocker")
    def acceptanceJobLinux = job("${branch}_acceptanceTestLinuxDocker")

    // configure build job
    buildJob.with {
        publishers {
            publishCloneWorkspace('', '', 'Successful', 'TAR', true, null)
            downstream intTestJob.name
        }
    }

    // configure integrationTestDocker job
    intTestJob.with {
        scm {
            cloneWorkspace(buildJob.name)
        }
        steps {
            gradle 'integrationTestDocker'
        }
        publishers {
           downstream acceptanceJobLinux.name
        }
    }

	acceptanceJobLinux.with {
        scm {
            cloneWorkspace(buildJob.name)
        }
        steps {
            gradle 'runAcceptanceTestLinux'
        }
        if (branch == 'master') {
            publishers {
                downstream 'master_release'
            }
        }
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
        }
    }
    return buildJob
}

/**
 * Git configure section.
 */
def gitConfigure(branchName, skippingTag) {
    { node ->
        // checkout to local branch
        node / 'extensions' / 'hudson.plugins.git.extensions.impl.LocalBranch' / localBranch(branchName)

        // checkout to local branch
        node / 'skipTag'(skippingTag)
    }
}
