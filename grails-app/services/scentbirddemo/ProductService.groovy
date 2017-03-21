package scentbirddemo

import grails.transaction.Transactional
import grails.validation.ValidationException

@Transactional
class ProductService {

    @Transactional(readOnly = true)
    def getProducts(String query) {
        if (query) {
            return Product.findAll("from Product p where p.name like :query or p.brand like :query",
                    [query: query])
        }
        return Product.list()
    }

    @Transactional(readOnly = true)
    def getEndingProducts(int amountThreshold) {
        Product.findAllByAmountLessThan(amountThreshold)
    }

    def getProduct(long id) {
        return Product.findById(id)
    }

    def saveProduct(Product product) {
        if (product.hasErrors()) {
            throw new ValidationException("", product.errors)
        }

        product.save()
    }

    def deleteProduct(Product product) {
        product.delete()
    }
}
