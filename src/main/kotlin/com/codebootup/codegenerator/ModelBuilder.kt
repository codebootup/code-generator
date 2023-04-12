package com.codebootup.codegenerator

interface ModelBuilder<out T> {
    fun build(): T
}
