# code-generator
Core library to support code generation.  There are some common functions that most code generators and this library's 
intention is to meet those needs.

The codebootup code generator allows developers to quickly developer their own code generators.  A typical use case 
would be to generate code from some schema for example OpenApi, JSON schema, WSDL.  Typically, domain models and api 
interfaces defining operations and sometimes implementations are code generated.

Typically most code generation in the opensource community is poor, and you never actually get exactly what you want.
This library will help you quickly get up and running with your own code generator and stay out of your way at the same 
time.

The library performs two main functions it allows you to bind a part of your model to a template and gives you the option 
of a number of out of the box file naming strategies for the rendered template output.

So you as a developer are in charge of defining what input model you want to use and then based on that model what files 
should be generated using which templates for which bits of the model.

To produce these simple files for example
```
Child1.txt 
"Parent - Child1"
Child2.txt
"Parent - Child2" 
Child3.txt
"Parent - Child3"
```
USe Thymeleaf "per-child" template example
```
[(${root.name})] - [(${itemInFocus.name})]
```
This file naming strategy used SPEL to find the name of the child and use it as the filename of the file 
```
ItemInFocusFileNamingStrategy(path = "name", suffix = "txt"),
```
Example Template Engine Wrapper that you will need to implement for example you might prefer freemarker as template
engine.  So implement this interface ```TemplateEngine``` and the code generator will use your implementation to render files
```
class ThymeleafTemplateEngine(private val templateEngine: org.thymeleaf.TemplateEngine) : TemplateEngine {
    override fun process(context: TemplateContext) {
        val tlContext = Context()
        tlContext.setVariables(context.model.toMap())
        templateEngine.process(context.template, tlContext, context.writerBuilder.build())
    }
}
```
Full example below
```
val modelBuilder = object : ModelBuilder<ModelToGoIntoTemplate> {
    override fun build(): ModelToGoIntoTemplate {
        return ModelToGoIntoTemplate(
            name = "Parent",
            children = listOf(
                Children("Child1"),
                Children("Child2"),
                Children("Child3"),
            ),
        )
    }
}
...

CodeRenderer(
    modelBuilder = modelBuilder,
    templateEngine = ThymeleafTemplateEngine(templateEngine), 
)
.addTemplate(
    TemplateRenderContext(
        template = "per-child",
        fileNamingStrategy = ItemInFocusFileNamingStrategy(path = "name", suffix = "txt"),
        modelPathInFocus = "Children", //SPEL
    ),
)
.render()
```
Please see ```src/test/java``` for examples in java 
