package com.codebootup.codegenerator

import java.io.File
import java.io.FileWriter
import java.io.Writer

class WriterBuilder(private val basePath: String) {
    fun build(directory: String, filename: String) : Writer{
        val directoryForFile = File("$basePath${File.separator}$directory")
        if(!directoryForFile.exists()) directoryForFile.mkdirs()
        return FileWriter("${directoryForFile.absolutePath}${File.separator}$filename")
    }
}