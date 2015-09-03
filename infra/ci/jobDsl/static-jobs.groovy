def repo = 'yatol/yatol'

// develop + master
job('develop_build') {
    scm {
        github repo, 'develop', {
            createTag(false)
        }
    }
    steps {
        gradle('clean build')
    }
}

job('master_build') {
    scm {
        github repo, 'master', {
            createTag(false)
        }
    }
    steps {
        gradle('clean build')
    }
}
