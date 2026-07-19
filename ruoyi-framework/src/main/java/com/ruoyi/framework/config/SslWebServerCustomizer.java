package com.ruoyi.framework.config;

import com.ruoyi.system.config.SslConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

import java.io.File;

/**
 * 动态SSL WebServer定制器
 *
 * @author ruoyi
 */
@Slf4j
@Configuration
public class SslWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Autowired
    private SslConfig sslConfig;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        sslConfig.ensureLoaded();

        int httpPort = sslConfig.getHttpPort();
        if (httpPort > 0) {
            factory.setPort(httpPort);
        }

        if (!sslConfig.isEnabled()) {
            return;
        }

        String keyStore = sslConfig.getKeyStore();
        if (ObjectUtils.isEmpty(keyStore)) {
            log.warn("[SSL] HTTPS已启用但证书路径为空，跳过HTTPS配置");
            return;
        }

        try {
            Resource resource = resolveResource(keyStore);
            if (resource == null || !resource.exists()) {
                log.error("[SSL] 证书文件不存在: {}", keyStore);
                return;
            }

            File keyStoreFile = resource.getFile();
            int httpsPort = sslConfig.getPort();

            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(httpsPort);

            Http11NioProtocol httpProtocol = (Http11NioProtocol) connector.getProtocolHandler();
            httpProtocol.setSSLEnabled(true);
            httpProtocol.setKeystoreFile(keyStoreFile.getAbsolutePath());
            httpProtocol.setKeystorePass(sslConfig.getKeyStorePassword());
            httpProtocol.setKeystoreType("JKS");

            factory.addAdditionalTomcatConnectors(connector);
        } catch (Exception e) {
            log.error("[SSL] 配置HTTPS Connector失败: {}", e.getMessage(), e);
        }
    }

    private Resource resolveResource(String path) {
        if (path.startsWith("classpath:")) {
            return new ClassPathResource(path.substring("classpath:".length()));
        } else if (path.startsWith("/") || path.contains(":")) {
            return new FileSystemResource(path);
        } else {
            Resource classpathResource = new ClassPathResource(path);
            if (classpathResource.exists()) {
                return classpathResource;
            }
            return new FileSystemResource(path);
        }
    }
}
