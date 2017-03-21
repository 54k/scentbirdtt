(function (window) {
    var AUTH_TOKEN = "iamgod"; // that's it - hardcoded
    var apiRoot = "http://localhost:8080/product";
    var authToken = "";

    var app = angular.module("scentbirdtt-client", ["ngRoute", "controllers", "services"]);
    app.config(["$routeProvider", function ($routeProvider) {
        $routeProvider
            .when("/product-list", {
                templateUrl: "template/product-list.html",
                controller: "ProductListCtrl"
            })
            .when("/product-edit/:id", {
                templateUrl: "template/product-detail.html",
                controller: "ProductDetailCtrl"
            })
            .when("/product-create", {
                templateUrl: "template/product-creation.html",
                controller: "ProductCreationCtrl"
            })
            .otherwise({
                redirectTo: "/product-list"
            });
    }]);

    app.directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
                });
            }
        };
    }]);

    var services = angular.module("services", ["ngResource"]);
    services.service("ProductsService", function ($resource) {
        return $resource(apiRoot, null, {
            query: {
                method: "GET",
                isArray: true,
                params: {
                    q: "@query"
                },
            },
            queryEnding: {
                url: apiRoot + "/ending",
                method: "GET",
                isArray: true
            },
            upload: {
                url: apiRoot + "/upload",
                method: "POST",
                transformRequest: angular.identity,
                headers: {
                    "Content-Type": undefined
                }
            },
            get: {
                url: apiRoot + "/:id"
            },
            remove: {
                url: apiRoot + "/:id",
                method: "DELETE",
                params: {
                    id: "@id"
                }
            },
            save: {
                method: "POST",
                params: {
                    id: "@id"
                }
            }
        });
    });

    var controllers = angular.module("controllers", []);
    controllers.controller("ProductListCtrl", ["$scope", "$http", "ProductsService", "$location", function ($scope,
                                                                                                            $http,
                                                                                                            productsService,
                                                                                                            $location) {
        $scope.login = function () {
            authToken = AUTH_TOKEN;
            $http.defaults.headers.common["authToken"] = authToken;
            $scope.readOnly = true;
        };

        $scope.logout = function () {
            authToken = "";
            $http.defaults.headers.common["authToken"] = window.undefined;
            $scope.readOnly = false;
        };

        $scope.getProductList = function () {
            $scope.products = productsService.query({
                q: $scope.query
            });
        };

        $scope.getEndingProductList = function () {
            $scope.products = productsService.queryEnding();
        };

        $scope.createNewProduct = function () {
            $location.path("/product-create");
        };

        $scope.editProduct = function (id) {
            $location.path("/product-edit/" + id);
        };

        $scope.deleteProduct = function (id) {
            productsService.remove({
                id: id
            }, $scope.getProductList);
        };

        $scope.uploadProducts = function () {
            if (!$scope.file) {
                return;
            }

            var formData = new FormData();
            formData.append("file", $scope.file);
            productsService.upload(formData, $scope.getProductList);
        };

        $scope.getProductList();
        $scope.readOnly = !!authToken;
        $http.defaults.headers.common["authToken"] = authToken || window.undefined;
    }]);

    controllers.controller("ProductCreationCtrl", ["$scope", "ProductsService", "$location", function ($scope,
                                                                                                       productsService,
                                                                                                       $location) {
        $scope.createNewProduct = function () {
            productsService.save($scope.product, $scope.cancel, function (response) {
                $scope.error = response.data.message;
            });
        };

        $scope.cancel = function () {
            $location.path("/product-list");
        };
    }]);

    controllers.controller("ProductDetailCtrl", ["$scope", "ProductsService", "$location", "$routeParams", function ($scope, productsService, $location, $routeParams) {
        $scope.updateProduct = function () {
            productsService.save($scope.product, $scope.cancel, function (response) {
                $scope.error = response.data.message;
            });
        };

        $scope.cancel = function () {
            $location.path("/product-list");
        };

        $scope.product = productsService.get({
            id: $routeParams.id
        }, function () {
        }, $scope.cancel);

        $scope.readOnly = !!authToken;
    }]);
})(this);