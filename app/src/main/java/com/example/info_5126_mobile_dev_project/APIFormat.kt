package com.example.info_5126_mobile_dev_project

data class APIFormat (
    var copyright:String,
    var results: List<Results>
)

data class Results (
    var section:String,
    var title:String,
    var abstract:String,
    var published_date:String,
    var byline:String,
    var subsection:String,
    var image:String,
    val multimedia: List<Multimedia> = emptyList()

)

data class Multimedia (
    var url:String,
    var caption:String,
    var copyright:String
)
