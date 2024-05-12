pipelineJob("dslJob2") {
    parameters{
        booleanParam("arrive",true,"check params")
    }
    definition {
        cps {
            script(readFileFromWorkspace('vars/newjob.groovy'))
        }
    }
}
