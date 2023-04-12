package com.codebootup.codegenerator;

import com.codebootup.compare.AssertDirectories;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CodeRendererJavaTest {
    @Test
    public void canUseIdiomaticJava(){
        String testOutputDirectory = "java";
        String outputDir = "com"+File.separator+"codebootup";
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(templateResolver());
        ArrayList<Child> children = new ArrayList<>();
        children.add(new Child("Bean"));
        new CodeRenderer(
            (ModelBuilder<Family>) () -> new Family("Java", children),
            new ThymeleafTemplateJavaEngine(templateEngine),
            new TemplateLocation(
                JavaDirStatics.TEST_ACTUAL_DIR + File.separator + testOutputDirectory,
                outputDir
            )
        ).addTemplate(
            new TemplateRenderContext(
                "root",
                new SimpleFileNamingStrategy("java.txt")
            )
        ).render();

        final Path expected = Paths.get(JavaDirStatics.TEST_EXPECTED_DIR + File.separator + testOutputDirectory);
        final Path actual = Paths.get(JavaDirStatics.TEST_ACTUAL_DIR + File.separator + testOutputDirectory);

        AssertDirectories.Companion.assertThat(actual).isEqualTo(expected);
    }

    private ITemplateResolver templateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setTemplateMode("TEXT");
        resolver.setSuffix(".template");
        return resolver;
    }
}
