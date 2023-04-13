# code-generator
Core JVM (Written in Kotlin can be used in Java projects) library to support code generation.  There are some common 
functions that most code generators need and this library intends to meet those needs.

The codebootup code generator allows developers to bootstrap their own code generators.  A typical use case 
would be to generate code from some schemas (OpenApi, JSON schema, WSDL).  Typically, domain models and api 
interfaces defining operations, and oftentimes, implementations and tests are code-generated.

Most code generation tools in the opensource community is poor, and you never actually get exactly what you want.
This library will help you quickly get up and running with your own code generator while staying out of your way.

The library contains two main functions. It allows you to bind a part of your model to a template and gives you the 
option for a number of out-of-the-box file-naming strategies for the rendered template output.

You as the developer will be in charge of defining what input models you want to use and what files should be generated 
according to the templates.

To produce these simple files for example:
```
.
├── Child1.txt
├── Child2.txt
└── Child3.txt
```
```shell
$ cat Child1.txt 
"Parent - Child1"

$ cat Child2.txt
"Parent - Child2" 

$ cat Child3.txt
"Parent - Child3"
```
Use Thymeleaf "per-child" template example
```
[(${root.name})] - [(${itemInFocus.name})]
```
This file naming strategy uses [Spring Expression Language (SPEL)](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html) to find the name of the child and use it as the filename of the file 
```
ItemInFocusFileNamingStrategy(path = "name", suffix = "txt"),
```

Example Template Engine Wrapper that you will need to implement for example you might prefer freemarker as template engine. So implement this interface ```TemplateEngine``` and the code generator will use your implementation to render files:
```kotlin
class ThymeleafTemplateEngine(private val templateEngine: org.thymeleaf.TemplateEngine) : TemplateEngine {
    override fun process(context: TemplateContext) {
        val tlContext = Context()
        tlContext.setVariables(context.model.toMap())
        templateEngine.process(context.template, tlContext, context.writerBuilder.build())
    }
}
```
Add as many templates as you like. However, you must minimally supply the template key and file naming strategy. If you do not specify the model in focus, then the root of the model will be used. 

There are also options for defining the base directory and file directory paths for where the files are rendered which are not shown in the aforementioned example. 

This project uses [SPEL](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html) twice - 

1. In extracting the bit of the model you are interested in
2. In some of the file naming strategies.

```kotlin
.addTemplate(
    TemplateRenderContext(
        template = "per-child", //Name of the template how your tempalte engine looks up the template to use
        fileNamingStrategy = ItemInFocusFileNamingStrategy(path = "name", suffix = "txt"), //What to name the model
        modelPathInFocus = "Children", 
        
        //Both modelPathInFocus and ItemInFocusFileNamingStrategy use SPEL to extract the bit of the model you are 
        //interested in for the template  
        //https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html
    ),
)
```
Full example below:
```kotlin
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
File naming strategies include:
```kotlin
SimpleFileNamingStrategy //You can just hard code the file name
ItemInFocusFileNamingStrategy //Use anything in the item in focus to define the file name 
ModelInFocusFileNamingStrategy //Use anything in model in focus to define the filename 
RootFileNamingStrategy //Use anything in  the module root to define the file name 
```

Please see ```src/test/java``` for more examples in java. 
