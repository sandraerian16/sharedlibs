def scripts = """
    pipeline {
        agent any

        stages {
            stage('Hello') {
                steps {
                    echo 'Hello World'
                }
            }
        }
    }
"""
pipelineJob("dslJob") {
    definition {
        cps {
            script(scripts)
        }
    }
}
