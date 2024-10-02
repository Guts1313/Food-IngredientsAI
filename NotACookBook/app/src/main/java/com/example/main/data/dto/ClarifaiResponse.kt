package com.example.main.data.dto

data class ClarifaiResponse(
    val status: ClarifaiStatus?,
    val outputs: List<ClarifaiOutput>?
)

data class ClarifaiStatus(
    val code: Int?,
    val description: String?
)

data class ClarifaiOutput(
    val data: ClarifaiData?
)

data class ClarifaiData(
    val concepts: List<ClarifaiConcept>?
)

data class ClarifaiConcept(
    val id: String?,
    val name: String?,
    val value: Double?
)