package br.com.southsystem.voto.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
	public Docket api() {
		List<Parameter> token = new ArrayList<Parameter>();
		
		ParameterBuilder _token = new ParameterBuilder();
			_token.name("Authorization")
				  .modelRef(new ModelRef("string"))
				  .parameterType("header")
				  .defaultValue("Bearer [token]")
				  .required(false)
           .build();
		
		token.add(_token.build());
		
	return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.com.southsystem"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo())
				.globalOperationParameters(token);
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"Gerenciador de Votos", 
				"Para gerenciar votos de pautas.",
				"v1",
				"Terms Of Service Url",
				new Contact("Gustavo Moraes Rego Fonseca", "nome", "gu.moraes.fonseca@gmail.com"),
				"License of API", "License of URL", Collections.emptyList());
	}
}