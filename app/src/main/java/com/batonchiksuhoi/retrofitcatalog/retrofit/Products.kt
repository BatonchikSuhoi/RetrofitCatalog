package com.batonchiksuhoi.retrofitcatalog.retrofit

data class Products(
    val products: List<Product>,
    val limit: Int,
    val skip: Int,
    val total: Int,
)
