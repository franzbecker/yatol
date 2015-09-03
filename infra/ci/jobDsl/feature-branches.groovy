import groovy.json.JsonSlurper

def repo = 'yatol/yatol'

// Create feature branches
def branchApi = new URL("https://api.github.com/repos/$repo/branches")
def branches = new groovy.json.JsonSlurper().parse(branchApi.newReader())

branches.findAll { it.name.startsWith('feature/') }.each { branch ->
    def jobName = "${branch.name}_build".replaceAll('/', '_')

    job(jobName) {
        scm {
            github repo, branch.name, {
                createTag(false)
            }
        }
        steps {
            gradle('build')
        }
    }
}