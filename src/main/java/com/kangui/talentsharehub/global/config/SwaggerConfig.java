package com.kangui.talentsharehub.global.config;

import com.kangui.talentsharehub.global.login.resolver.annotation.AuthPrincipal;
import com.kangui.talentsharehub.global.login.resolver.dto.Principal;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    static {
        SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(AuthPrincipal.class);
    }

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Talent-Share-Hub")
                .description("Talent-Share-Hub API");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                        .security(Arrays.asList(securityRequirement)) // 이름은 자유롭게 지정 가능
                        .info(info);
    }
}
