pipelineJob("dslJob2") {
    definition {
        cps {
            script(readFileFromWorkspace('./newjob.groovy'))
        }
    }
}
