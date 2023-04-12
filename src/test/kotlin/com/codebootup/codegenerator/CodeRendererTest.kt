package com.codebootup.codegenerator

import com.codebootup.compare.AssertDirectories
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Test
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import java.io.File
import kotlin.io.path.Path

class CodeRendererTest {

    @Test
    fun `throws IllegalArgumentException when model returns null object`(){
        val modelBuilder = object: ModelBuilder<Any?>{
            override fun build(): Any? {
                return null
            }
        }

        assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
            CodeRenderer(
                modelBuilder = modelBuilder,
                templateEngine = ThymeleafTemplateEngine(TemplateEngine())
            ).render()
        }.withMessage("Model builder must return a non null value")

    }
    @Test
    fun `can render parent child files`(){
        val modelBuilder = object: ModelBuilder<Parent?>{
            override fun build(): Parent? {
                return Parent("Elaine", listOf(
                    Children("Lee"),
                    Children("Austin"),
                    Children("Gemma"),
                ))
            }
        }
        val testOutputDirectory = "parent"
        val baseDirectory = "${DirStatics.TEST_ACTUAL_OUTPUT_DIR}${File.separator}$testOutputDirectory"
        val fileNamingStrategy = ItemInFocusFileNamingStrategy(path = "name", suffix = "txt")
        val outputDir = "com${File.separator}codebootup"
        val templateEngine = TemplateEngine()
        templateEngine.addTemplateResolver(templateResolver())

        CodeRenderer(
            modelBuilder = modelBuilder,
            templateEngine = ThymeleafTemplateEngine(templateEngine)
        )
        .addTemplate(TemplateRenderContext(
            template = "perParent",
            fileNamingStrategy = fileNamingStrategy,
            fileDirectory = outputDir,
            baseDirectory = baseDirectory
        ))
        .addTemplate(TemplateRenderContext(
            template = "perChild",
            fileNamingStrategy = fileNamingStrategy,
            fileDirectory = outputDir,
            modelPathInFocus = "Children",
            baseDirectory = baseDirectory
        ))
        .addTemplate(TemplateRenderContext(
            template = "simple",
            fileNamingStrategy = SimpleFileNamingStrategy("Simple.txt"),
            fileDirectory = outputDir,
            baseDirectory = baseDirectory
        ))
        .render()

        val expected = Path("${DirStatics.TEST_EXPECTED_OUTPUT_DIR}${File.separator}$testOutputDirectory")
        val actual = Path(baseDirectory)

        AssertDirectories.assertThat(actual).isEqualTo(expected)
    }

    private fun templateResolver() : ITemplateResolver{
        val resolver = ClassLoaderTemplateResolver()
        resolver.prefix = "templates/"
        resolver.setTemplateMode("TEXT")
        resolver.suffix = ".template"
        return resolver
    }
}