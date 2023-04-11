package com.codebootup.codegenerator

import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser

interface FileNamingStrategy {
    fun name(model: Any) : String
}

class ItemInFocusFileNamingStrategy(private val path: String, private val suffix: String) : FileNamingStrategy{
    private val parser: ExpressionParser = SpelExpressionParser()

    override fun name(model: Any): String {
        return "${(parser.parseExpression(path).getValue(model) as String)}.$suffix"
    }
}