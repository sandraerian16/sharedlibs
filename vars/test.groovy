import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

def String createYamlFromJson(def jsonResponse, String rootElement, priority = null) {

    String yamlString = ""

    if (priority) {
        jsonResponse = jsonResponse.findAll { order -> order.prio.toInteger() >= priority.toInteger() }
    }

    jsonResponse.each { order ->
        def rootElementValue = order."${rootElement}"
        println("ROOT ELEMENT: ${rootElement} - ${rootElementValue}")
        yamlString += "${rootElementValue.toString().trim()}:\n"
        order.each { orderItem ->
            if (orderItem.key.toString().trim().equalsIgnoreCase("testutil_data")) {
                orderItem.value.each { testUtilData ->
                    if (testUtilData) {
                        testUtilData.each { testUtilDataItem ->
                            def testUtilDataKeyValue = ""
                            if (testUtilDataItem.key.toString().trim().equalsIgnoreCase("key")) {
                                testUtilDataKeyValue += "  ${testUtilDataItem.value.toString().trim()}:"
                            }
                            if (testUtilDataItem.key.toString().trim().equalsIgnoreCase("value")) {
                                testUtilDataKeyValue += " '${testUtilDataItem.value.toString().trim()}'\n"
                            }
                            yamlString += "${testUtilDataKeyValue}"
                        }
                    }
                }
            } else if (orderItem.key.toString().trim().equalsIgnoreCase("applicationhead_data")) {
                orderItem.value.each { applicationHeadData ->
                    if (applicationHeadData) {
                        applicationHeadData.each { applicationHeadDataItem ->
                            yamlString += "  ${applicationHeadDataItem.key.toString().trim()}: '${applicationHeadDataItem.value.toString().trim()}'\n"
                        }
                    }
                }
            } else {
                yamlString += "  ${orderItem.key.toString().trim()}: '${orderItem.value.toString().trim()}'\n"
            }
        }
    }

    println("YAML")
    println(yamlString)
    return  yamlString
}

def createJsonFromJson(def jsonResponse, String rootElement, priority = null) {


    if (priority) {
        jsonResponse = jsonResponse.findAll { order -> order.prio.toInteger() >= priority.toInteger() }
    }
    def map = [:]
    jsonResponse.each { order ->
        def rootElementValue = order."${rootElement}"
        println("ROOT ELEMENT: ${rootElement} - ${rootElementValue}")
        def orderMap = [:]
        order.each { orderItem ->
            if (orderItem.key.toString().trim().equalsIgnoreCase("testutil_data")) {
                orderItem.value.each { testUtilData ->
                    if (testUtilData) {
                        testUtilData.each { testUtilDataItem ->
                            def key = ""
                            def value = ""
                            if (testUtilDataItem.key.toString().trim().equalsIgnoreCase("key")) {
                                key = testUtilDataItem.value.toString().trim()
                            }
                            if (testUtilDataItem.key.toString().trim().equalsIgnoreCase("value")) {
                                value = testUtilDataItem.value.toString().trim()
                            }
                            if (key != "" && value != "") {
                                orderMap[key] = value
                            }
                        }
                    }
                }
            } else if (orderItem.key.toString().trim().equalsIgnoreCase("applicationhead_data")) {
                orderItem.value.each { applicationHeadData ->
                    if (applicationHeadData) {
                        applicationHeadData.each { applicationHeadDataItem ->
                            orderMap[applicationHeadDataItem.key.toString().trim()] = applicationHeadDataItem.value.toString().trim()
                        }
                    }
                }
            } else {
                orderMap[orderItem.key.toString().trim()] = orderItem.value.toString().trim()
            }

        }
        map[rootElementValue] = orderMap
    }
    println JsonOutput.toJson(map)
//    writeFile file: 'data.json', text: JsonOutput.toJson(map)
}

def Stack<String> selectSpecificValuesFromYAMLToStack( String key, boolean doubleInsert) {
    println("select all " + key + " from yaml and return as stack...")
    def yamlData = readYaml file: pathToYaml
    Stack<String> valueStack = new Stack<String>()
    yamlData.each { hashKey, value ->
        if (doubleInsert) {
            String stackElement = value.get(key)
            valueStack.add(stackElement)
        } else {
            if (!doubleInsert && !valueStack.contains(value.get(key))) {
                String stackElement = value.get(key)
                valueStack.add(stackElement)
            }
        }
    }
    println("values in stack:")
    println(valueStack)
    return valueStack
}



