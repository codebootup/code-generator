package com.codebootup.codegenerator

class TemplateRenderContext(
    val template: String,
    val modelPathInFocus: String = ".",
    val fileNamingStrategy: FileNamingStrategy,
    val baseDirectory: String,
    val fileDirectory: String
)