package com.hb0730.upms.authorization.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 认证中心
 *
 * @author bing_huang
 */
@SpringBootApplication
@MapperScan("com.hb0730.upms.authorization.server.**.mapper")
@EnableRedisHttpSession
public class AuthorizationServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }
}
