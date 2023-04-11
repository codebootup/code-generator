package com.codebootup.codegenerator

class ModelInputOutputBuilder<I, O>(
    private val inputBuilder: ModelInputBuilder<I>,
    private val outputBuilder: ModelOutputBuilder<I, O>,
) : ModelBuilder<O>{
    override fun build(): O {
        return outputBuilder.build(inputBuilder.build())
    }
}