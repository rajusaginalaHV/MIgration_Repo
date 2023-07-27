import groovy.json.JsonSlurper

def call(String apiUrl) {
    def cacheKey = "github_api_${apiUrl.hashCode()}"
    def cachedResponse = Jenkins.instance.getDescriptor('org.jenkinsci.plugins.cache.CachePlugin').get(cacheKey)

    if (cachedResponse != null) {
        println("Using cached API response for $apiUrl")
        return new JsonSlurper().parseText(cachedResponse)
    }

    println("Fetching API response for $apiUrl")
    def response = new URL(apiUrl).text

    // Cache the API response for 1 hour (you can adjust the expiration time as needed)
    Jenkins.instance.getDescriptor('org.jenkinsci.plugins.cache.CachePlugin').put(cacheKey, response, 3600)

    return new JsonSlurper().parseText(response)
}
