package com.codebootup.codegenerator

class CodeRenderer(
    private val modelBuilder: ModelBuilder<*>,
    private val templateEngine: TemplateEngine,
    val defaultTemplateLocation: FileLocation = DefaultLocation(),
) {
    private val templates: MutableList<TemplateRenderContext> = mutableListOf()

    fun addTemplate(template: TemplateRenderContext): CodeRenderer {
        if (template.location is DefaultLocation) {
            val templateWithDefaultLocation = TemplateRenderContext(
                template = template.template,
                modelPathInFocus = template.modelPathInFocus,
                fileNamingStrategy = template.fileNamingStrategy,
                location = defaultTemplateLocation,
            )
            templates.add(templateWithDefaultLocation)
        } else {
            templates.add(template)
        }
        return this
    }

    fun render() {
        val root = modelBuilder.build() ?: throw IllegalArgumentException("Model builder must return a non null value")

        templates.forEach { templateRenderContext ->
            val modelInFocus = if (templateRenderContext.modelPathInFocus != ".") {
                val itemInFocus = SpelExpressionParserObject.parseExpression(templateRenderContext.modelPathInFocus).getValue(root) ?: throw CodeGeneratorParseException()
                itemInFocus
            } else {
                root
            }

            if (modelInFocus is Iterable<*>) {
                modelInFocus.filterNotNull().forEach {
                    val templateContext = TemplateContext(
                        templateRenderContext = templateRenderContext,
                        model = TemplateModel(
                            root = root,
                            modelInFocus = modelInFocus,
                            itemInFocus = it,
                        ),
                    )
                    process(templateContext)
                }
            } else {
                val templateContext = TemplateContext(
                    templateRenderContext = templateRenderContext,
                    model = TemplateModel(
                        root = root,
                        modelInFocus = modelInFocus,
                        itemInFocus = modelInFocus,
                    ),
                )
                process(templateContext)
            }
        }
    }

    private fun process(templateContext: TemplateContext) {
        templateEngine.process(
            context = templateContext,
        )
    }
}

class TemplateRenderContext(
    val template: String,
    val modelPathInFocus: String = ".",
    val fileNamingStrategy: FileNamingStrategy,
    val location: FileLocation = DefaultLocation(),
)

interface FileLocation {
    val baseDirectory: String
    val fileDirectory: String
}

private data class DefaultLocation(
    override val baseDirectory: String = "",
    override val fileDirectory: String = "",
) : FileLocation

data class TemplateLocation(
    override val baseDirectory: String = "",
    override val fileDirectory: String = "",
) : FileLocation
