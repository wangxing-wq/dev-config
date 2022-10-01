package com.wx.common;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @version 1.0
 * @author 王兴
 * @date 2022/10/2 1:43
 * SwaggerConfig,这里swagger使用的ui不是swagger用原生的,使用的是knife4j
 */
public class SwaggerConfig {

	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage(""))
				.paths(PathSelectors.any())
				.build();
	}



	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("swaggerComponent.getTitle()")
				.build();
	}

}
