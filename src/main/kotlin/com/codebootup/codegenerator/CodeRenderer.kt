package com.codebootup.codegenerator

import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser

class CodeRenderer(
    private val modelBuilder: ModelBuilder<*>,
    private val templateEngine: TemplateEngine,
    private val writerBuilder: WriterBuilder
) {
    private val parser: ExpressionParser = SpelExpressionParser()
    private val templates: MutableList<TemplatePath> = mutableListOf()

    fun addTemplate(templatePath: TemplatePath) : CodeRenderer{
        templates.add(templatePath)
        return this
    }

    fun render(){
        val output = modelBuilder.build()
        templates.forEach { templatePath ->
            val model = if(templatePath.itemInFocus != "."){
                parser.parseExpression(templatePath.itemInFocus).getValue(output)!!
            }
            else{
                output!!
            }
            val modelMap: MutableMap<String, Any> = mutableMapOf()
            modelMap["root"] = output!!
            if(model is Iterable<*>){
                model.filterNotNull().forEach { process(modelMap, it, templatePath) }
            }
            else{
                process(modelMap, model, templatePath)
            }
        }
    }

    private fun process(modelMap: MutableMap<String, Any>, model: Any, templatePath: TemplatePath) {
        modelMap["itemInFocus"] = model
        modelMap["fileDirectory"] = templatePath.fileDirectory
        val filename = templatePath.fileNamingStrategy.name(model)
        val writer = writerBuilder.build(
            directory = templatePath.fileDirectory,
            filename = filename
        )
        templateEngine.process(
            template = templatePath.template,
            context = TemplateContext(modelMap),
            writer = writer
        )
    }
}