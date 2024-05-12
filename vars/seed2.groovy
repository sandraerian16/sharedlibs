pipelineJob("dslJob2") {
    definition {
        cps {
           // script(readFileFromWorkspace('vars/newjob.groovy'))
            script {
                def params = [
                    param1: 'value1',
                    param2: 'value2'
                ]
                // Pass parameters along with the script execution
                readFileFromWorkspace('vars/newjob.groovy').call(params)
            }
        }
    }
}
