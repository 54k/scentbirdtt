package scentbirddemo

class UrlMappings {

    static mappings = {
        get "/$controller(.$format)?"(action: "showProducts")
        get "/$controller/ending"(action: "showEndingProducts")
        get "/$controller/$id"(action: "showProduct")

        post "/$controller/upload"(action: "uploadProducts")
        post "/$controller"(action: "saveProduct")
        put "/$controller/$id"(action: "updateProduct")
        delete "/$controller/$id"(action: "deleteProduct")

        post "/login"(controller: "login", action: "login")
        get "/logout"(controller: "login", action: "logout")

        "/"(controller: "application", action: "index")
        "500"(view: "/error")
        "404"(view: "/notFound")
    }
}
