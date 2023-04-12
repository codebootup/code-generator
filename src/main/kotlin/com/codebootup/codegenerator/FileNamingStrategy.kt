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
