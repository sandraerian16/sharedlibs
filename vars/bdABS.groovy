import groovy.xml.XmlParser
import java.nio.file.*

def moduleChecker(String startDir) {
    static String resultPath
    static List<String> pomFileList = []
    def scriptPath = Paths.get(System.getProperty('user.dir')).toAbsolutePath().toString()
    resultPath = Paths.get(scriptPath, 'results').toString()
    Files.createDirectories(Paths.get(resultPath))

    println("Starting from dir: $startDir")
    println("Generating results into: $resultPath")

    // Find all pom files with a "java" parent directory
    findAllPomFiles(Paths.get(startDir).toAbsolutePath().toString(), pomFileList, 'java')

    File missingModulesFile = new File(Paths.get(resultPath, 'missing_modules.txt').toString())
    missingModulesFile.createNewFile()

    pomFileList.each { pomFile ->
            def dirs = getSubdirListForFile(pomFile)
            def dirSet = dirs.collect { Paths.get(it).fileName.toString() as String } as Set

            def modules = getModulesFromPomFile(pomFile)
            def moduleSet = modules.collect { it as String } as Set

            dirSet.each { dir ->
                if (!(dir in moduleSet)) {
                def message = "Pom file '$pomFile' is missing module '$dir'\n"
                println(message)
                missingModulesFile.append(message)
                }
            }
    }

    println("pom module check finished. Results are written into '$resultPath'")
}

void findAllPomFiles(String path, List<String> pomFileList, String parent) {
    findPomFiles("$path", pomFileList, parent)
}

void findPomFiles(String path, List<String> pomFileList, String parent) {
    new File(path).eachFile { file ->
            if (file.isDirectory() && !Files.isSymbolicLink(file.toPath())) {
            findPomFiles(file.path, pomFileList, parent)
            } else if (file.name == 'pom.xml') {
            if (file.parentFile.name == parent) {
                pomFileList << file.path
            }
            }
    }
}

List<String> getSubdirListForFile(String filePath) {
    def dirs = []
    def path = Paths.get(filePath).toAbsolutePath().getParent()
    Files.list(path).forEach { subPath ->
            if (Files.isDirectory(subPath) && !Files.isSymbolicLink(subPath)) {
            dirs << subPath.toString()
            }
    }
    return dirs
}

List<String> getModulesFromPomFile(String pomFileName) {
    def pom = new XmlParser().parse(new File(pomFileName))
    def modules = pom.depthFirst().findAll { it.name() == 'module' }.collect { it.text() }
    def profiles = pom.depthFirst().findAll { it.name() == 'profile' && it.id.text() == 'test' }
    def testModules = profiles.collectMany { profile -> profile.depthFirst().findAll { it.name() == 'module' }.collect { it.text() } }

    List<String> allModules = []
    modules.forEach({ module -> allModules.add(module.toString()) })
    testModules.forEach({ module -> allModules.add(module.toString()) })

    return allModules
}

