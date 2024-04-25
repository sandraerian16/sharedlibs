pipelineJob("dslJob") {
    definition {
        cps {
            script(readFileFromWorkspace('./newjob.groovy'))
        }
    }
}
