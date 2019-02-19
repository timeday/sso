/**
 *
 */
package com.spring.config;

import com.spring.properties.OAuth2ClientProperties;
import com.spring.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class SsoAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
	private SecurityProperties securityProperties;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private TokenStore tokenStore;

	@Autowired(required = false)
	private JwtAccessTokenConverter jwtAccessTokenConverter;

	@Autowired(required = false)
	private TokenEnhancer jwtTokenEnhancer;

	/**
	 * 配置客户端信息
	 * @param clients
	 * @throws Exception
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		InMemoryClientDetailsServiceBuilder builder = clients.inMemory();

		OAuth2ClientProperties[] clientProperties = securityProperties.getOauth2().getClients();
		for (OAuth2ClientProperties client : clientProperties) {

			builder.withClient(client.getClientId())
					.secret(client.getClientSecret())
					.authorizedGrantTypes("refresh_token", "authorization_code", "password")
					.accessTokenValiditySeconds(client.getAccessTokenValidateSeconds())
					.refreshTokenValiditySeconds(2592000)
					.scopes("all");
		}
	}

	/**
	 * 配置token
	 * @param endpoints
	 * @throws Exception
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore)
				//.authenticationManager(authenticationManager)
				.userDetailsService(userDetailsService);

		if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
			TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
			List<TokenEnhancer> enhancers = new ArrayList<>();
			enhancers.add(jwtTokenEnhancer);
			enhancers.add(jwtAccessTokenConverter);
			enhancerChain.setTokenEnhancers(enhancers);
			endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
		}
	}
	/**
	 * tokenKey的访问权限表达式配置
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		//获取密钥的前提是需要经过身份认证
		security.tokenKeyAccess("isAuthenticated()");//permitAll()
	}



}
