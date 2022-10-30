package com.vu.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.vu.constant.AttributeConstant;
import com.vu.constant.ParameterConstant;
import com.vu.constant.UrlConstant;
import com.vu.service.impl.UserDetailsServiceImpl;
import com.vu.repository.UserRepository;

@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@ComponentScan(basePackageClasses = { UserDetailsServiceImpl.class, UserRepository.class })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsServiceImpl userDetailsService;

	public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable();

		// Pages not require login
		httpSecurity.authorizeRequests().antMatchers(UrlConstant.CSS_FILES_PATH_URL, UrlConstant.JS_FILES_PATH_URL,
				UrlConstant.LOGIN_URL, UrlConstant.LOGOUT_URL).permitAll();

		// Pages require login with roles: ROLE_USER, ROLE_ADMIN.
		// If not login yet, redirect to /login
		httpSecurity.authorizeRequests()
				.antMatchers(UrlConstant.DEFAULT_URL, UrlConstant.HOME_URL, UrlConstant.USER_LIST_URL)
				.access(AttributeConstant.REQUIRED_LOGIN_ANY_ROLES_ATTRIBUTE);

		// Pages require login with role: ROLE_ADMIN.
		// If not login at admin role yet, redirect to /login
		httpSecurity.authorizeRequests().antMatchers(UrlConstant.USERS_PATH_URL, UrlConstant.ROLES_PATH_URL)
				.access(AttributeConstant.REQUIRED_LOGIN_ADMIN_ROLE_ATTRIBUTE);

		// When user login with ROLE_USER, but try to
		// access pages require ROLE_ADMIN, redirect to /error-403
		httpSecurity.authorizeRequests().and().exceptionHandling().accessDeniedPage(UrlConstant.ACCESS_DENIED_URL);

		// Configure login page (check login by spring security)
		httpSecurity.authorizeRequests().and().formLogin().loginProcessingUrl(UrlConstant.SECURITY_CHECK_URL)
				.loginPage(UrlConstant.LOGIN_URL).defaultSuccessUrl(UrlConstant.HOME_URL)
				.failureUrl(UrlConstant.LOGIN_FAILURE_URL).usernameParameter(ParameterConstant.USERNAME_PARAMETER)
				.passwordParameter(ParameterConstant.PASSWORD_PARAMETER).and().logout()
				.logoutUrl(UrlConstant.LOGOUT_URL).logoutSuccessUrl(UrlConstant.LOGOUT_SUCCESS_URL);

		// Configure remember me (save token in database)
		httpSecurity.authorizeRequests().and().rememberMe().tokenRepository(this.persistentTokenRepository())
				.tokenValiditySeconds(ParameterConstant.TOKEN_VALIDITY_SECONDS);
	}

	// Token stored in memory (of web server)
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		InMemoryTokenRepositoryImpl inMemoryTokenRepository = new InMemoryTokenRepositoryImpl();
		return inMemoryTokenRepository;
	}
}
