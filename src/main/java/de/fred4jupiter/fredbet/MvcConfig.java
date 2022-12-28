package de.fred4jupiter.fredbet;

import de.fred4jupiter.fredbet.web.ActivePageHandlerInterceptor;
import de.fred4jupiter.fredbet.web.ChangePasswordFirstLoginInterceptor;
import de.fred4jupiter.fredbet.web.WebSecurityUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    private final WebSecurityUtil webSecurityUtil;

    public MvcConfig(WebSecurityUtil webSecurityUtil) {
        this.webSecurityUtil = webSecurityUtil;
    }

//    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
//            "classpath:/META-INF/resources/", "classpath:/resources/",
//            "classpath:/static/", "classpath:/public/" };
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
//    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error").setViewName("error");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ActivePageHandlerInterceptor());
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(changePasswordFirstLoginInterceptor());

        // registry.addInterceptor(new ExecutionTimeInterceptor());

        // for logging request header
        // registry.addInterceptor(new HeaderLogHandlerInterceptor());
    }

    @Bean
    public ChangePasswordFirstLoginInterceptor changePasswordFirstLoginInterceptor() {
        return new ChangePasswordFirstLoginInterceptor(webSecurityUtil);
    }

//    @Bean
//    public LocaleResolver localeResolver(FredbetProperties fredbetProperties) {
//        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
//        sessionLocaleResolver.setDefaultLocale(fredbetProperties.getDefaultLocale());
//        return sessionLocaleResolver;
//    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

//    @Bean
//    public Java8TimeDialect java8TimeDialect() {
//        return new Java8TimeDialect();
//    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:org/springframework/security/messages", "classpath:/TranslationMessages", "classpath:/ValidationMessages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
