package co.istad.mbanking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// make the abstraction for clicent to request to server path . like make the path can client go point to the store server but client don't know
@Configuration
public class ResourceHandlerConfig implements WebMvcConfigurer {
    @Value("${media.server-path}")
    private String serverPath;
    @Value("${media.client-path}")
    private String clientPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(clientPath)
                .addResourceLocations("file:" + serverPath);
    }
}
