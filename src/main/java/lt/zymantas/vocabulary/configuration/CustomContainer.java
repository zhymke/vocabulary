package lt.zymantas.vocabulary.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CustomContainer implements
        WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Value("${server.port}")
    private String port;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.setPort(Integer.valueOf(System.getenv("PORT") == null ? port : System.getenv("PORT")));
    }
}