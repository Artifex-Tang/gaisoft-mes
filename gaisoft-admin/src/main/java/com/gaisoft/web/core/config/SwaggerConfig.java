package com.gaisoft.web.core.config;

import com.gaisoft.common.config.RuoYiConfig;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Autowired
    private RuoYiConfig ruoyiConfig;
    @Value(value="${swagger.enabled}")
    private boolean enabled;
    @Value(value="${swagger.pathMapping}")
    private String pathMapping;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30).enable(this.enabled).apiInfo(this.apiInfo()).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).paths(PathSelectors.any()).build().securitySchemes(this.securitySchemes()).securityContexts(this.securityContexts()).pathMapping(this.pathMapping);
    }

    private List<SecurityScheme> securitySchemes() {
        ArrayList<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        apiKeyList.add((SecurityScheme)new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
        return apiKeyList;
    }

    private List<SecurityContext> securityContexts() {
        ArrayList<SecurityContext> securityContexts = new ArrayList<SecurityContext>();
        securityContexts.add(SecurityContext.builder().securityReferences(this.defaultAuth()).operationSelector(o -> o.requestMappingPattern().matches("/.*")).build());
        return securityContexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        ArrayList<SecurityReference> securityReferences = new ArrayList<SecurityReference>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("标题：装备综合保障智能问答系统_接口文档").description("描述：用于管理集团旗下公司的人员信息,具体包括XXX,XXX模块...").contact(new Contact(this.ruoyiConfig.getName(), null, null)).version("版本号:" + this.ruoyiConfig.getVersion()).build();
    }
}
