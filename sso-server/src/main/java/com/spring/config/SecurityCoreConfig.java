/**
 *
 */
package com.spring.config;

import com.spring.properties.SecurityProperties;
import com.spring.handler.SpringLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @EnableConfigurationProperties 注册配置文件
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 退出时的处理策略配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new SpringLogoutSuccessHandler(securityProperties.getSignOutUrl());
    }
}
