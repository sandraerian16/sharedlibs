import groovy.json.JsonSlurper

def call(){

 def owner = 'sandraerian16'
 def repo = 'apiTask'
 def baseRelease = 'v1.0.0'
 def headRelease = 'v3.0.0'
 def token = "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"

 def connection = new URL(
         "https://api.github.com/repos/${owner}/${repo}/compare/${baseRelease}...${headRelease}").openConnection()
 connection.setRequestProperty("Accept", "application/vnd.github+json")
 connection.setRequestProperty("Authorization", "Bearer ${token}")
 connection.setRequestProperty("X-GitHub-Api-Version", "2022-11-28")
 connection.connect()
def responseCode = connection.getResponseCode()
def responseMessage = connection.getResponseMessage()
println "API response: ${responseCode} ${responseMessage}"
 if(responseCode ==200){
 def json = new JsonSlurper().parseText(connection.getInputStream().getText())
List<String> commitList = new ArrayList<>()
 json.commits.each { commit ->
  commitList.add(commit.commit.message)
 }
  println commitList;}
 else{
  println "Unsuccessful"
}
