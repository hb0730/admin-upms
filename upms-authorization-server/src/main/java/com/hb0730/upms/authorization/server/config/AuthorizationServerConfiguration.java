package com.hb0730.upms.authorization.server.config;

import com.hb0730.upms.authorization.server.service.impl.RedisAuthenticationCodeService;
import com.hb0730.upms.authorization.server.service.impl.RedisClientDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 认证配置
 *
 * @author bing_huang
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisClientDetailsServiceImpl redisClientDetailsService;
    private final WebResponseExceptionTranslator responseExceptionTranslator;
    private final RedisAuthenticationCodeService authenticationCodeService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(redisClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .authorizationCodeServices(authenticationCodeService)
                .tokenServices(tokenServices())
                .exceptionTranslator(responseExceptionTranslator)
                .accessTokenConverter(accessTokenConverter())
        ;
    }

    @Bean
    public ResourceOwnerPasswordTokenGranter resourceOwnerPasswordTokenGranter(AuthenticationManager authenticationManager,
                                                                               OAuth2RequestFactory oAuth2RequestFactory) {
        DefaultTokenServices defaultTokenServices = tokenServices();
        return new ResourceOwnerPasswordTokenGranter(authenticationManager, defaultTokenServices, redisClientDetailsService, oAuth2RequestFactory);
    }


    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore store = new RedisTokenStore(redisConnectionFactory);
        store.setAuthenticationKeyGenerator(oAuth2Authentication -> UUID.randomUUID().toString());
        return store;
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(redisClientDetailsService);
        return tokenServices;
    }

    @Bean
    public DefaultOAuth2RequestFactory oAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(redisClientDetailsService);
    }

    /**
     * 解决获取不到相关用户信息
     *
     * @return {@link DefaultAccessTokenConverter}
     */
    @Bean
    public DefaultAccessTokenConverter accessTokenConverter() {
        DefaultAccessTokenConverter converter = new DefaultAccessTokenConverter();
        converter.setUserTokenConverter(new UserAuthenticationConverter());
        return converter;
    }

    @Component
    static class UserAuthenticationConverter extends DefaultUserAuthenticationConverter {
        @Override
        public Map<String, ?> convertUserAuthentication(Authentication authentication) {
            Map<String, Object> response = new LinkedHashMap<String, Object>();
            response.put(USERNAME, authentication.getName());
            if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
            }
            response.put("user-info", authentication);
            return response;
        }

        @Override
        public Authentication extractAuthentication(Map<String, ?> map) {
            return super.extractAuthentication(map);
        }
    }

}
