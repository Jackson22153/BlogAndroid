package com.example.blognhom2.model

import java.time.LocalDate
import java.util.Date

data class Post (
    val id : String,
    val userID : Int,
    val time : LocalDate,
    val postImg : String,
    val title : String,
    val categories: String,
    val postContent: String,
    val postContentID: Int
)