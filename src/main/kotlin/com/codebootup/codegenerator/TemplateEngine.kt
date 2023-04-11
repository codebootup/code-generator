package com.codebootup.codegenerator

import java.io.Writer

interface TemplateEngine {
    fun process(template: String, context: TemplateContext, writer: Writer)
}

data class TemplateContext(val context: Map<String, *>)