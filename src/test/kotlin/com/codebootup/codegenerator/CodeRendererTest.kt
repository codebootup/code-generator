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
    fun `throws IllegalArgumentException when model returns null object`() {
        val modelBuilder = object : ModelBuilder<Any?> {
            override fun build(): Any? {
                return null
            }
        }

        assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
            CodeRenderer(
                modelBuilder = modelBuilder,
                templateEngine = ThymeleafTemplateEngine(TemplateEngine()),
            ).render()
        }.withMessage("Model builder must return a non null value")
    }

    @Test
    fun `can render parent child files`() {
        val modelBuilder = object : ModelBuilder<Root?> {
            override fun build(): Root {
                return Root(
                    name = "Elaine",
                    children = listOf(
                        Children("Lee"),
                        Children("Austin"),
                        Children("Gemma"),
                    ),
                )
            }
        }
        val testOutputDirectory = "parent"
        val baseDirectory = "${DirStatics.TEST_ACTUAL_OUTPUT_DIR}${File.separator}$testOutputDirectory"
        val outputDir = "com${File.separator}codebootup"
        val templateEngine = TemplateEngine()
        templateEngine.addTemplateResolver(templateResolver())

        CodeRenderer(
            modelBuilder = modelBuilder,
            templateEngine = ThymeleafTemplateEngine(templateEngine),
        )
            .addTemplate(
                TemplateRenderContext(
                    template = "root",
                    fileNamingStrategy = SimpleFileNamingStrategy(filename = "root-simple.txt"),
                    fileDirectory = outputDir,
                    baseDirectory = baseDirectory,
                ),
            )
            .addTemplate(
                TemplateRenderContext(
                    template = "root",
                    fileNamingStrategy = RootFileNamingStrategy(path = "name", suffix = "txt"),
                    fileDirectory = outputDir,
                    baseDirectory = baseDirectory,
                ),
            )
            .addTemplate(
                TemplateRenderContext(
                    template = "children",
                    fileNamingStrategy = ModelInFocusFileNamingStrategy(path = "size", suffix = "txt"),
                    modelPathInFocus = "Children",
                    fileDirectory = outputDir,
                    baseDirectory = baseDirectory,
                ),
            )
            .addTemplate(
                TemplateRenderContext(
                    template = "per-child",
                    fileNamingStrategy = ItemInFocusFileNamingStrategy(path = "name", suffix = "txt"),
                    fileDirectory = outputDir,
                    modelPathInFocus = "Children",
                    baseDirectory = baseDirectory,
                ),
            )
            .render()

        val expected = Path("${DirStatics.TEST_EXPECTED_OUTPUT_DIR}${File.separator}$testOutputDirectory")
        val actual = Path(baseDirectory)

        AssertDirectories.assertThat(actual).isEqualTo(expected)
    }

    private fun templateResolver(): ITemplateResolver {
        val resolver = ClassLoaderTemplateResolver()
        resolver.prefix = "templates/"
        resolver.setTemplateMode("TEXT")
        resolver.suffix = ".template"
        return resolver
    }
}
