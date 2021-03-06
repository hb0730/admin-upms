package com.hb0730.admin.upms.server.system.project.client.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hb0730.admin.upms.server.system.domain.model.entity.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 客户端详情
 *
 * @author bing_huang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("oauth_client_details")
public class OauthClientDetails extends BaseDomain {
    private static final long serialVersionUID = -380264374802235816L;
    /**
     * client
     */
    @TableId(value = "client_id")
    private String clientId;

    /**
     * 所属资源id
     */
    @TableField("resource_ids")
    private String resourceIds;

    /**
     * 客户端密钥
     */
    @TableField("client_secret")
    private String clientSecret;

    /**
     * 权限范围
     */
    @TableField("scope")
    private String scope;

    /**
     * 客户端支持的grant_typ
     */
    @TableField("authorized_grant_types")
    private String authorizedGrantTypes;

    /**
     * 重定向
     */
    @TableField("web_server_redirect_uri")
    private String webServerRedirectUri;

    /**
     * 客户端所拥有的Spring Security的权限值
     */
    @TableField("authorities")
    private String authorities;

    /**
     * 设定客户端的access_token的有效时间值(单位:秒)
     */
    @TableField("access_token_validity")
    private Integer accessTokenValidity;

    /**
     * 设定客户端的refresh_token的有效时间值(单位:秒),可选
     */
    @TableField("refresh_token_validity")
    private Integer refreshTokenValidity;

    /**
     * 是否自动授权
     */
    @TableField("autoapprove")
    private String autoapprove;

    /**
     * 这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,如:
     */
    @TableField("additional_information")
    private String additionalInformation;

    public static final String CLIENT_ID="client_id";
    public static final String RESOURCE_IDS="resource_ids";
    public static final String CLIENT_SECRET="client_secret";
    public static final String SCOPE="scope";
    public static final String AUTHORIZED_GRANT_TYPES="authorized_grant_types";
    public static final String WEB_SERVER_REDIRECT_URI="web_server_redirect_uri";
    public static final String AUTHORITIES="authorities";
    public static final String ACCESS_TOKEN_VALIDITY="access_token_validity";
    public static final String AUTOAPPROVE="autoapprove";
    public static final String ADDITIONAL_INFORMATION="additional_information";
}
