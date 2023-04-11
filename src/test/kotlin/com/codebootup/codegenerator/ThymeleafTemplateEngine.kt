package com.codebootup.codegenerator

import org.thymeleaf.context.Context
import java.io.Writer

class ThymeleafTemplateEngine(private val templateEngine: org.thymeleaf.TemplateEngine) : TemplateEngine {
    override fun process(template: String, context: TemplateContext, writer: Writer) {
        val tlContext = Context()
        tlContext.setVariables(context.context)
        templateEngine.process(template, tlContext, writer)
    }
}