package scentbirddemo

class BootStrap {

    def init = { servletContext ->
        def products = [
                [externalId: "channel",
                 name      : "Channel",
                 brand     : "Channel",
                 price     : 10,
                 size      : 50,
                 amount    : 6],
                [externalId: "givenchy",
                 name      : "Givenchy",
                 brand     : "Givenchy",
                 price     : 20,
                 size      : 50,
                 amount    : 5],
                [externalId: "kenzo",
                 name      : "Kenzo",
                 brand     : "Kenzo",
                 price     : 40,
                 size      : 100,
                 amount    : 4],
                [externalId: "guerlain",
                 name      : "Guerlain",
                 brand     : "Guerlain",
                 price     : 60,
                 size      : 100,
                 amount    : 3]
        ].each {
            new Product(it).save()
        }
    }
}
