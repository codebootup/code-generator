package com.codebootup.codegenerator

interface ModelOutputBuilder<in I, out O> {
    fun build(i: I): O
}