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

class ItemInFocusFileNamingStrategy(private val path: String, private val suffix: String) : FileNamingStrategy {
    override fun name(model: TemplateModel): String = ExpressionParserFileNamingStrategy(
        path = path,
        suffix = suffix,
        model = model.itemInFocus,
    ).parse()
}

class ModelInFocusFileNamingStrategy(private val path: String, private val suffix: String) : FileNamingStrategy {

    override fun name(model: TemplateModel): String = ExpressionParserFileNamingStrategy(
        path = path,
        suffix = suffix,
        model = model.modelInFocus,
    ).parse()
}

class RootFileNamingStrategy(private val path: String, private val suffix: String) : FileNamingStrategy {

    override fun name(model: TemplateModel): String = ExpressionParserFileNamingStrategy(
        path = path,
        suffix = suffix,
        model = model.root,
    ).parse()
}

class SimpleFileNamingStrategy(private val filename: String) : FileNamingStrategy {
    override fun name(model: TemplateModel): String = filename
}

private class ExpressionParserFileNamingStrategy(
    private val model: Any,
    private val path: String,
    private val suffix: String,
) {
    fun parse(): String {
        return "${(SpelExpressionParserObject.parseExpression(path).getValue(model)?.toString() ?: throw CodeGeneratorParseException())}.$suffix"
    }
}
