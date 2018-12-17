package com.utry.openticket.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

/**
 * 用于设置文件上传大小的配置文件
 * @author MH
 *
 */

public class FileUploadConfig {
	@Bean
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory=new MultipartConfigFactory();
		factory.setMaxFileSize("10240KB");
		factory.setMaxRequestSize("102400KB");
		return factory.createMultipartConfig();
	}
}
