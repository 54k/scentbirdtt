package scentbirddemo

import static org.springframework.http.HttpStatus.UNAUTHORIZED

class SecurityInterceptor {

    SecurityService securityService

    SecurityInterceptor() {
        match(controller: "product", action: "uploadProducts")
        match(controller: "product", action: "saveProduct")
        match(controller: "product", action: "updateProduct")
        match(controller: "product", action: "deleteProduct")
    }

    boolean before() {
        String authToken = request.getHeader("authToken")
        if (!securityService.isValidToken(authToken)) {
            render status: UNAUTHORIZED, message: "Authentication required"
            return false
        }
        return true
    }
}
