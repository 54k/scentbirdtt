package scentbirddemo

import grails.core.GrailsApplication

class SecurityService {

    GrailsApplication grailsApplication

    def isValidToken(String authToken) {
        return authToken == grailsApplication.config.getProperty("app.authToken")
    }
}
