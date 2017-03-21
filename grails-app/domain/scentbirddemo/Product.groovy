package scentbirddemo

class Product {

    static constraints = {
        externalId blank: false, unique: true
        name blank: false
        brand blank: false
        price min: 0.01
        size inList: [50, 100]
        amount min: 0
    }
    String externalId
    String name
    String brand
    BigDecimal price
    Integer size
    Integer amount
}
