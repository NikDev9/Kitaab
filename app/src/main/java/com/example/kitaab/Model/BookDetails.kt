package com.example.kitaab.Model

data class BookDetails (val title: String,
                        val author: String,
                        val coverImg: String,
                        val rating: Float,
                        val reviews: List<String>,
                        val synopsis: String)