package com.codebootup.codegenerator

import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser

interface FileNamingStrategy {
    fun name(model: TemplateModel) : String
}

class ItemInFocusFileNamingStrategy(private val path: String, private val suffix: String) : FileNamingStrategy{
    private val parser: ExpressionParser = SpelExpressionParser()

    override fun name(model: TemplateModel): String {
        return "${(parser.parseExpression(path).getValue(model.itemInFocus)?.toString())}.$suffix"
    }
}

class ModelInFocusFileNamingStrategy(private val path: String, private val suffix: String) : FileNamingStrategy{
    private val parser: ExpressionParser = SpelExpressionParser()

    override fun name(model: TemplateModel): String {
        return "${(parser.parseExpression(path).getValue(model.modelInFocus)?.toString())}.$suffix"
    }
}

class RootFileNamingStrategy(private val path: String, private val suffix: String) : FileNamingStrategy{
    private val parser: ExpressionParser = SpelExpressionParser()

    override fun name(model: TemplateModel): String {
        return "${(parser.parseExpression(path).getValue(model.root)?.toString())}.$suffix"
    }
}

class SimpleFileNamingStrategy(private val filename : String) : FileNamingStrategy{
    override fun name(model: TemplateModel): String = filename

}