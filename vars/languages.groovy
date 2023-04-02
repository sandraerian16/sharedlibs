import groovy.json.JsonSlurper
def call(){
    def url = new URL(
            "https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.6.0&packaging=jar&jvmVersion=11&groupId=com.example&artifactId=demo&name=demo&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.demo")
    HttpURLConnection connection = (HttpURLConnection) url.openConnection()
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Accept", "application/json")
    connection.connect()
    if (connection.responseCode == 200 || connection.responseCode == 201) {
          def json = new JsonSlurper().parseText(connection.getInputStream().getText())
        def languages = json.language.values
        println(json)
        List<String> langList = new ArrayList<>()

        for (i in 0..<languages.size()) {
            langList.add(languages[i].name)
        }
        return lanList
    } else {
        println("error connection to url")
    }
}
class language{
    String id
    String name
}
