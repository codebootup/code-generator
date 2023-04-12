package com.codebootup.codegenerator

class CodeGeneratorParseException(override val message: String = "SpelExpressionParser Unexpectedly returned null when ParseException expected instead") : RuntimeException(message)