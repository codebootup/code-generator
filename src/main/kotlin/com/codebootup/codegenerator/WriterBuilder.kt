package com.codebootup.codegenerator

import java.io.File
import java.io.FileWriter
import java.io.Writer

interface WriterBuilder {
    fun build(): Writer
}

class DefaultWriterBuilder(
    private val templateRenderContext: TemplateRenderContext,
    private val filename: String,
) : WriterBuilder {
    override fun build(): Writer {
        val directoryForFile = File("${templateRenderContext.location.baseDirectory}${File.separator}${templateRenderContext.location.fileDirectory}")
        if (!directoryForFile.exists()) directoryForFile.mkdirs()
        return FileWriter("${directoryForFile.absolutePath}${File.separator}$filename")
    }
}
