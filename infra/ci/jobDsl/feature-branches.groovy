def repo = 'yatol/yatol'
def defaultCron = 'H/3 * * * *'

// Create feature branches
def branchApi = new URL("https://api.github.com/repos/$repo/branches")
def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())

branches.findAll { it.name.startsWith('feature/') }.each { branch ->
    def jobName = "${branch.name}_build".replaceAll('/', '_')

    job(jobName) {
        scm {
            git "https://github.com/${repo}.git", branch.name, {
                createTag false
            }
        }
        triggers {
            scm defaultCron
        }
        steps {
            gradle 'build'
        }
    }
}