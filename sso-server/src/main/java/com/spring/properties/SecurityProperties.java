/**
 *
 */
package com.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "spring.security")
public class SecurityProperties {

	/**
	 * OAuth2认证服务器配置
	 */
	private OAuth2Properties oauth2 = new OAuth2Properties();

	private String responseType="JSON";


	private String singInSuccessUrl= "/index.html";

	private String signInPage= "/login.html";


	private String signOutUrl= "/loginOut.htm";


}


