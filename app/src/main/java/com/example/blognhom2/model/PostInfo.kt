package com.example.blognhom2.model

import java.time.LocalDate

data class PostInfo (
    val id : Int,
    val user : String,
    val time : String,
    val postImg : String,
    val title : String,
    val categories: String,
    val postContentID: String,
    val state: String
)