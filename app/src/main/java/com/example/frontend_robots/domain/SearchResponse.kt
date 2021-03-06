package com.example.frontend_robots.domain

import java.io.Serializable

class SearchResponse(
    val query: String,
    val link: String,
    val image: String,
    var message: String?,
): Serializable
