package com.hb0730.admin.upms.server.system.project.scope.model.dto;

import com.hb0730.admin.upms.server.system.domain.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 权限范围
 *
 * @author bing_huang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScopeDetailsDTO extends BaseDTO {
    private static final long serialVersionUID = 3137277581719708626L;
    /**
     * scopeid
     */
    @NotBlank(message = "scopeid不为空")
    private String scopeId;
    /**
     * name
     */
    @NotBlank(message = "scope name不为空")
    private String name;
}
