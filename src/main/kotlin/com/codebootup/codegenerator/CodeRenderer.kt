/*
 *  Copyright 2023-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.codebootup.codegenerator

class CodeRenderer @JvmOverloads constructor(
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

class TemplateRenderContext @JvmOverloads constructor(
    val template: String,
    val fileNamingStrategy: FileNamingStrategy,
    val modelPathInFocus: String = ".",
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
