package com.codebootup.codegenerator

import org.springframework.expression.ExpressionParser
import org.springframework.expression.ParseException
import org.springframework.expression.spel.standard.SpelExpressionParser

class CodeRenderer(
    private val modelBuilder: ModelBuilder<*>,
    private val templateEngine: TemplateEngine
) {
    private val templates: MutableList<TemplateRenderContext> = mutableListOf()

    fun addTemplate(templatePath: TemplateRenderContext) : CodeRenderer{
        templates.add(templatePath)
        return this
    }

    fun render(){
        val root = modelBuilder.build() ?: throw IllegalArgumentException("Model builder must return a non null value")

        templates.forEach { templateRenderContext ->
            val modelInFocus = if(templateRenderContext.modelPathInFocus != "."){
                val itemInFocus = SpelExpressionParserObject.parseExpression(templateRenderContext.modelPathInFocus).getValue(root) ?: throw CodeGeneratorParseException()
                itemInFocus
            }
            else{
                root
            }

            if(modelInFocus is Iterable<*>){
                modelInFocus.filterNotNull().forEach {
                    val templateContext = TemplateContext(
                        templateRenderContext = templateRenderContext,
                        model = TemplateModel(
                            root = root,
                            modelInFocus = modelInFocus,
                            itemInFocus = it
                        )
                    )
                    process(templateContext)
                }
            }
            else{
                val templateContext = TemplateContext(
                    templateRenderContext = templateRenderContext,
                    model = TemplateModel(
                        root = root,
                        modelInFocus = modelInFocus,
                        itemInFocus = modelInFocus
                    )
                )
                process(templateContext)
            }
        }
    }

    private fun process(templateContext: TemplateContext) {
        templateEngine.process(
            context = templateContext
        )
    }
}