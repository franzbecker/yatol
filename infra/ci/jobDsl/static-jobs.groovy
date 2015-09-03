def repo = 'yatol/yatol'

['develop', 'master'].each { branch ->

    // build job
    job("${branch}_build") {
        scm {
            github repo, branch, {
                createTag(false)
            }
        }
        triggers {
            scm 'H/3 * * * *'
        }
        steps {
            gradle 'build' // TODO add 'clean'
        }
        publishers {
            jacocoCodeCoverage {
                execPattern '**/**.exec'
            }
            publishCloneWorkspace("")
            downstream "${branch}_integrationTest"
        }
    }

    // integrationTestDocker job
    job("${branch}_integrationTestDocker") {
        steps {
            // gradle 'clean build'
        }
    }

}
