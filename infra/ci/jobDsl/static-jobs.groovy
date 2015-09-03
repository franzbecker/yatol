def repo = 'yatol/yatol'
def defaultCron = 'H/3 * * * *'

job('develop_build') {
    scm {
        git "https://github.com/${repo}.git", 'develop', {
            createTag(false)
        }
    }
    triggers {
        scm defaultCron
    }
    steps {
        gradle 'clean build'
    }
    publishers {
        // publishCloneWorkspace
        downstream 'develop_integrationTest'
    }
}

job('develop_integrationTest') {

}

job('master_build') {
    scm {
        git "https://github.com/${repo}.git", 'master', {
            createTag(false)
        }
    }
    triggers {
        scm defaultCron
    }
    steps {
        gradle 'clean build'
    }
}
