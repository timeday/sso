/**
 *
 */
package com.spring.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  客户端
 */

@SpringBootApplication
@RestController
@EnableOAuth2Sso
public class SsoClient2Application {



	@GetMapping("/user")
	public Authentication user(Authentication user) {
		return user;
	}

	public static void main(String[] args) {
		SpringApplication.run(SsoClient2Application.class, args);
	}
/*	@GetMapping("/userinfo")
	public Object getCurrentUser(Authentication user, HttpServletRequest request) throws Exception {

		String token = StringUtils.substringAfter(request.getHeader("Authorization"), "bearer ");

		Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("UTF-8"))
					.parseClaimsJws(token).getBody();

		String company = (String) claims.get("company");

		System.out.println(company);

		return user;
	}*/
}
