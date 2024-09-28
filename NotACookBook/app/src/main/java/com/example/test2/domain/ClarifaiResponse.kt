package com.example.test2.domain

data class ClarifaiResponse(
    val outputs: List<ClarifaiOutput>
)

data class ClarifaiOutput(
    val data: List<ClarifaiConcept>
)

data class ClarifaiConcept(
    val name: String
)