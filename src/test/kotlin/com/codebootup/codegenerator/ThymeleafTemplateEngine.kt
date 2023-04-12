package com.codebootup.codegenerator

import org.thymeleaf.context.Context

class ThymeleafTemplateEngine(private val templateEngine: org.thymeleaf.TemplateEngine) : TemplateEngine {
    override fun process(context: TemplateContext) {
        val tlContext = Context()
        tlContext.setVariables(context.model.toMap())
        templateEngine.process(context.template, tlContext, context.writerBuilder.build())
    }
}