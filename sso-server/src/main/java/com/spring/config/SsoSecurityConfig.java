/**
 *
 */
package com.spring.config;

import com.spring.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 认证服务器登陆配置
 */
@Configuration
public class SsoSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;


	@Autowired
	protected AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	protected AuthenticationFailureHandler authenticationFailureHandler;

	@Bean
	public PasswordEncoder passwordEncoder()	{
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private SecurityProperties securityProperties;


	@Autowired
	private ServerProperties serverProperties;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//认证配置
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/authentication/require") //未认证处理跳转处理逻辑
				.loginProcessingUrl("/authentication/form")//登陆页面表单提交
				.successHandler(authenticationSuccessHandler)//登陆成功处理器 默认处理器 SavedRequestAwareAuthenticationSuccessHandler 用于oos
				.failureHandler(authenticationFailureHandler)//失败处理器
				.and().authorizeRequests()
				.antMatchers("/authentication/require",
						"/login.html").permitAll()//以上路径不用经过身份认证
				.anyRequest().authenticated().and()//以下路径需要经过身份认证
				.logout()//退出
				.logoutUrl("/signOut")//退出提交路径
				.logoutSuccessHandler(logoutSuccessHandler)//成功处理器
				.deleteCookies("JSESSIONID").and().csrf().disable();//禁值检查csrf
	}

}
