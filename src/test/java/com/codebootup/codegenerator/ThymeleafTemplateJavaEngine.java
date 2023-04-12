package com.codebootup.codegenerator;

import org.jetbrains.annotations.NotNull;
import org.thymeleaf.context.Context;

public class ThymeleafTemplateJavaEngine implements TemplateEngine{

    private final org.thymeleaf.TemplateEngine engine;

    public ThymeleafTemplateJavaEngine(org.thymeleaf.TemplateEngine engine){
        this.engine = engine;
    }
    @Override
    public void process(@NotNull TemplateContext context) {
        Context tlContext = new Context();
        tlContext.setVariables(context.getModel().toMap());
        engine.process(context.getTemplate(), tlContext, context.getWriterBuilder().build());
    }
}
