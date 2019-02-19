/**
 *
 */
package com.spring.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.properties.SecurityProperties;
import com.spring.response.ResponseType;
import com.spring.response.SimpleResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功的处理器
 *
 * @author zhailiang
 */
@Component("springAuthenticationSuccessHandler")
public class SpringAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SecurityProperties securityProperties;

	private RequestCache requestCache = new HttpSessionRequestCache();


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

        logger.info("登录成功");

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && StringUtils.hasText(request
                .getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }
        //清楚认证数据
        clearAuthenticationAttributes(request);
        // Use the DefaultSavedRequest URL
        String targetUrl = savedRequest.getRedirectUrl();
        logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        //跳转
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

}
