package com.example.newsapp.data.model

import com.example.newsapp.domain.model.HeadlineSource

data class Source(
    val id: String?,
    val name: String
)

fun Source.toDomain(): HeadlineSource {
    return HeadlineSource(
        id = id.orEmpty(),
        name = name
    )
}
