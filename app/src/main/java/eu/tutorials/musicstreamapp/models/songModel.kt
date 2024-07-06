package eu.tutorials.musicstreamapp.models

data class songModel(
    val id:String,
    val title:String,
    val subtitle:String,
    val Url:String,
    val coverUrl:String,
){
    constructor():this("","","","","")
}

