package scentbirddemo

import grails.validation.ValidationException
import grails.web.RequestParameter
import org.springframework.web.multipart.MultipartFile

import static org.springframework.http.HttpStatus.*

class ProductController {

    static responseFormats = ['json']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    private static final int AMOUNT_THRESHOLD = 5

    ProductService productService

    def showProducts(@RequestParameter("q") String query) {
        def products = productService.getProducts(query)

        withFormat {
            csv {
                renderAsCsv(products)
            }
            "*" {
                respond products
            }
        }
    }

    private renderAsCsv(products) {
        response.setHeader "Content-disposition", "attachment; filename=products.csv"
        response.contentType = 'text/csv'
        products.each { p ->
            response.outputStream << "${p.externalId}, ${p.name}, ${p.brand}, ${p.price}, ${p.size}, ${p.amount}\n"
        }
        response.outputStream.flush()
    }

    def showEndingProducts() {
        respond productService.getEndingProducts(AMOUNT_THRESHOLD)
    }

    def showProduct(long id) {
        respond productService.getProduct(id)
    }

    def uploadProducts() {
        MultipartFile file = request.getFile("file")
        if (file.empty) {
            render status: NO_CONTENT
        }
        def tempFile = File.createTempFile("csv-upload", ".csv")
        file.transferTo(tempFile)
        tempFile.splitEachLine(",", { line ->
            def product = new Product(externalId: line[0],
                    name: line[1],
                    brand: line[2],
                    price: line[3] as BigDecimal,
                    size: line[4] as Integer,
                    amount: line[5] as Integer)
            productService.saveProduct(product)
        })

        render status: OK
    }

    def saveProduct(Product product) {
        if (product == null) {
            render status: NOT_FOUND
            return
        }

        try {
            productService.saveProduct(product)
            respond product, [status: CREATED, view: "show"]
        } catch (ValidationException ignore) {
            respond product.errors, view: "create"
        }
    }

    def updateProduct(Product product) {
        if (product == null) {
            render status: NOT_FOUND
            return
        }

        try {
            productService.saveProduct(product)
            respond product, [status: OK, view: "show"]
        } catch (ValidationException ignore) {
            respond product.errors, view: "edit"
        }
    }

    def deleteProduct(Product product) {
        if (product == null) {
            render status: NOT_FOUND
            return
        }

        productService.deleteProduct(product)
        render status: NO_CONTENT
    }
}
