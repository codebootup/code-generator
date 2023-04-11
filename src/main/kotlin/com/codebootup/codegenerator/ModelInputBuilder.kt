package com.codebootup.codegenerator

interface ModelInputBuilder<out T> {
    fun build(): T
}