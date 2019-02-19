package com.spring.config;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class TokenJwtEnhancer implements TokenEnhancer {


	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<>();
		info.put("exect", "oss");//添加额外的数据

		((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);

		return accessToken;
	}

}
