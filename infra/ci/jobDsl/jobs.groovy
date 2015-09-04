repo = 'yatol/yatol'

// Create jobs for static branches
['develop', 'master'].each { branch ->

    // define jobs
    def buildJob = defaultBuildJob(branch, true)
    def intTestJob = job("${branch}_integrationTestDocker")

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
            github repo, branch, {
                createTag(false)
                localBranch(${branch})
            }
            git(
                gitCheckoutToLocalBranch(${branch})
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
 * Prevents detached head state after checkout by setting the local branch name.
 */
def gitCheckoutToLocalBranch(branchName) {
    { node ->
        // checkout to local branch
        node / 'extensions' << 'hudson.plugins.git.extensions.impl.LocalBranch' {
            localBranch branchName
        }
    }
}
