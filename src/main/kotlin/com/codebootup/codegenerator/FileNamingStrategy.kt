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

interface FileNamingStrategy {
    fun name(model: TemplateModel): String
}

/**
 * Strategy for naming a file using an item in the list
 *
 * @param pathExpression The path expression in Spring Expression Language (SPEL)
 * @param suffix The file extension
 */
class ItemInFocusFileNamingStrategy(
    private val pathExpression: String,
    private val suffix: String
) : FileNamingStrategy {
    override fun name(model: TemplateModel): String = ExpressionParserFileNamingStrategy(
        pathExpression = pathExpression,
        suffix = suffix,
        model = model.itemInFocus,
    ).parse()
}

/**
 * Strategy for naming a file using the model in focus
 *
 * @param pathExpression The path expression in Spring Expression Language (SPEL)
 * @param suffix The file extension
 */
class ModelInFocusFileNamingStrategy(
    private val pathExpression: String,
    private val suffix: String
) : FileNamingStrategy {
    override fun name(model: TemplateModel): String = ExpressionParserFileNamingStrategy(
        pathExpression = pathExpression,
        suffix = suffix,
        model = model.modelInFocus,
    ).parse()
}

/**
 * Strategy for naming a file using the root model
 *
 * @param pathExpression The path expression in Spring Expression Language (SPEL)
 * @param suffix The file extension
 */
class RootFileNamingStrategy(
    private val pathExpression: String,
    private val suffix: String
) : FileNamingStrategy {
    override fun name(model: TemplateModel): String = ExpressionParserFileNamingStrategy(
        pathExpression = pathExpression,
        suffix = suffix,
        model = model.root,
    ).parse()
}

/**
 * Strategy for naming a file with a hard-coded value
 *
 * @param filename Name of the file
 */
class SimpleFileNamingStrategy(private val filename: String) : FileNamingStrategy {
    override fun name(model: TemplateModel): String = filename
}

/**
 * Strategy to parse an object using SPEL for generating the file name
 *
 * @param model The object in question used in parsing the path expression
 * @param pathExpression The path expression in Spring Expression Language (SPEL)
 * @param suffix The file extension
 */
private class ExpressionParserFileNamingStrategy(
    private val model: Any,
    private val pathExpression: String,
    private val suffix: String,
) {
    fun parse(): String {
        return "${(SpelExpressionParserObject.parseExpression(pathExpression).getValue(model)?.toString() ?: throw CodeGeneratorParseException())}.$suffix"
    }
}
