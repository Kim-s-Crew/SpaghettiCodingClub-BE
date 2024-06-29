package wercsmik.spaghetticodingclub.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(
                        "https://spaghetticoding.shop",
                        "https://kim.spaghetticoding.shop",
                        "http://43.202.186.51:8080",
                        "http://localhost:3000/",
                        "http://43.202.186.51:3000",
                        "http://localhost:3000",
                        "https://spaghetti-coding-club.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("Origin", "Content-Type", "Accept")
                .allowCredentials(true)
                .exposedHeaders("Authorization")
                .maxAge(3600);
    }
}