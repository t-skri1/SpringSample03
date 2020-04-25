package com.example.web.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@EnableWebMvc // Spring MVCを有効化する
@Configuration
@ComponentScan(basePackages = {"com.example.web.controller"})
public class MvcConfig implements WebMvcConfigurer {
	@Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver =
                new SpringResourceTemplateResolver();
        // ビューを保存するフォルダ名を指定する
        templateResolver.setPrefix("/WEB-INF/templates/");
        // ビューの拡張子を指定する
        templateResolver.setSuffix(".html");
        // テンプレートモードをHTMLに指定する
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // テンプレートを読み込む際の文字コードを指定する
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());

        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(SpringResourceTemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // SpringResourceTemplateResolverをセットする
        templateEngine.setTemplateResolver(templateResolver());
        // SpELのコンパイラを有効化してパフォーマンスを向上させる
        templateEngine.setEnableSpringELCompiler(true);
        // Date and Time APiを利用するためのDialectを追加
        templateEngine.addDialect(new Java8TimeDialect());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        // ビューを書き出す際の文字コードを指定する
        viewResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
        registry.addResourceHandler("/webjars/**")
        		.addResourceLocations("classpath:/META-INF/resources/webjars/")
        		.resourceChain(false)
        		.addResolver(new VersionResourceResolver()
        				.addContentVersionStrategy("/**"));
    }

    //メッセージソースの設定
    //WEBページでプロパティファイルを使用できる
    //日本語メッセージ：messages_ja.properties
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");

        // trueをセットすれば、メッセージのキーがない場合にキーを表示
        // false の場合、NoSuchMessageExceptionを投げる
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        // # -1 : リロードしない、0 : 常にリロードする
        messageSource.setCacheSeconds(0);
        return messageSource;
    }
}