package com.codebootup.codegenerator

interface TemplateEngine {
    fun process(context: TemplateContext)
}

class TemplateContext(
    templateRenderContext: TemplateRenderContext,
    val model: TemplateModel
){
    val template = templateRenderContext.template
    val writerBuilder: WriterBuilder = DefaultWriterBuilder(
        templateRenderContext = templateRenderContext,
        filename = templateRenderContext.fileNamingStrategy.name(model.itemInFocus)
    )
}