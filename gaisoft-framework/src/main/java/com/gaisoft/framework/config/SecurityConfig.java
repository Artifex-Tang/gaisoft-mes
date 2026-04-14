package com.gaisoft.framework.config;

import com.gaisoft.framework.config.properties.PermitAllUrlProperties;
import com.gaisoft.framework.security.filter.JwtAuthenticationTokenFilter;
import com.gaisoft.framework.security.handle.AuthenticationEntryPointImpl;
import com.gaisoft.framework.security.handle.LogoutSuccessHandlerImpl;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.CorsFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;
    @Autowired
    private CorsFilter corsFilter;
    @Autowired
    private PermitAllUrlProperties permitAllUrl;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder((PasswordEncoder)this.bCryptPasswordEncoder());
        return new ProviderManager(new AuthenticationProvider[]{daoAuthenticationProvider});
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return (SecurityFilterChain)httpSecurity.csrf(csrf -> csrf.disable()).headers(headersCustomizer -> headersCustomizer.cacheControl(cache -> cache.disable()).frameOptions(options -> options.sameOrigin())).exceptionHandling(exception -> exception.authenticationEntryPoint((AuthenticationEntryPoint)this.unauthorizedHandler)).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(requests -> {
            this.permitAllUrl.getUrls().forEach(url -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.antMatchers(new String[]{url})).permitAll());
            ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.antMatchers(new String[]{"/login", "/register", "/captchaImage", "/deepseek/**", "/file/**", "/kb/download", "/filePage"})).permitAll().antMatchers(HttpMethod.GET, new String[]{"/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**"})).permitAll().antMatchers(new String[]{"/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**"})).permitAll().anyRequest()).authenticated();
        }).logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler((LogoutSuccessHandler)this.logoutSuccessHandler)).addFilterBefore((Filter)this.authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class).addFilterBefore((Filter)this.corsFilter, JwtAuthenticationTokenFilter.class).addFilterBefore((Filter)this.corsFilter, LogoutFilter.class).build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
