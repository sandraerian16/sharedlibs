def printHello(def name){
    println("hello ${name}")
}

def appendMessageToGitHubFile(String repoUrl, String branch, String filePath, String message) {
    def tempWorkspace = checkout scm

    // Construct the file path in the workspace
    def workspaceFilePath = "${tempWorkspace}/${filePath}"

    // Append the message to the file
    writeFile file: workspaceFilePath, text: "${readFile(workspaceFilePath)}\n${message}"

    // Commit and push the changes
    sh """
        cd ${tempWorkspace}
        git add ${filePath}
        git commit -m "Append message to ${filePath}"
        git push origin ${branch}
    """
}
