package com.codebootup.codegenerator

data class TemplateModel(
    val root: Any,
    val modelInFocus: Any,
    val itemInFocus: Any
){
    fun toMap(): Map<String, Any>{
        return mapOf(
            Pair("root", root),
            Pair("modelInFocus", modelInFocus),
            Pair("itemInFocus", itemInFocus)
        )
    }
}