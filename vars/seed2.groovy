pipelineJob("dslJob2") {
    definition {
        cps {
            script(readFileFromWorkspace('vars/newjob.groovy'))
        }
    }
}
