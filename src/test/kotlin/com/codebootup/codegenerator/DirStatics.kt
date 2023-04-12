package com.codebootup.codegenerator

import java.io.File

class DirStatics {
    companion object {
        val TEST_RESOURCES_DIR = "src${File.separator}test${File.separator}resources"
        val TEST_ACTUAL_OUTPUT_DIR = "$TEST_RESOURCES_DIR${File.separator}act"
        val TEST_EXPECTED_OUTPUT_DIR = "$TEST_RESOURCES_DIR${File.separator}exp"
    }
}
