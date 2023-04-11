package com.codebootup.codegenerator

data class TemplatePath(
    val template: String,
    val itemInFocus: String = ".",
    val fileNamingStrategy: FileNamingStrategy,
    val fileDirectory: String
)