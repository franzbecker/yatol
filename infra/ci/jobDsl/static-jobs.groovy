def repo = 'yatol/yatol'
def defaultCron = 'H/3 * * * *'

job('develop_build') {
    scm {
        github repo, 'develop', {
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
        github repo, 'master', {
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
