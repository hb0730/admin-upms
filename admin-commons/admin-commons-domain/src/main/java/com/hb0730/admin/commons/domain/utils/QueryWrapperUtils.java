package com.hb0730.admin.commons.domain.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hb0730.admin.commons.domain.model.entity.BaseDomain;
import com.hb0730.admin.commons.domain.model.query.BaseParams;
import com.hb0730.admin.upms.commons.enums.SortTypeEnum;
import com.hb0730.commons.lang.collection.CollectionUtils;
import com.hb0730.commons.spring.BeanUtils;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * mybatis-plus 条件构造器
 *
 * @author bing_huang
 */
public class QueryWrapperUtils {
    /**
     * 分页
     *
     * @param params   过滤参数
     * @param <ENTITY> 继承{@link BaseDomain}
     * @param <PARAMS> 继承{@link BaseParams}
     * @return 过滤器
     * @see Page
     * @since v1.1
     */
    @NonNull
    public static <ENTITY extends BaseDomain, PARAMS extends BaseParams> Page<ENTITY> getPage(@NonNull PARAMS params) {
        return new Page<>(params.getPageNum(), params.getPageSize());
    }

    /**
     * page 类型转换
     *
     * @param page      page
     * @param clazz     转换目标
     * @param <ENTITY2> 源类型
     * @param <ENTITY>  目标类型
     * @return 转换后的page
     */
    public static <ENTITY2, ENTITY> Page<ENTITY> pageToBean(Page<ENTITY2> page, Class<ENTITY> clazz) {
        List<ENTITY2> records = page.getRecords();
        Page<ENTITY> p1 = new Page<>();
        List<ENTITY> entities = BeanUtils.transformFromInBatch(records, clazz);
        p1.setRecords(entities);
        p1.setTotal(page.getTotal());
        p1.setSize(page.getSize());
        p1.setCurrent(page.getCurrent());
        p1.setOrders(page.getOrders());
        return p1;
    }

    /**
     * 动态拼接过滤参数
     *
     * @param params   过滤参数
     * @param <PARAMS> 参数类型
     * @param <ENTITY> 实体类型
     * @return {@link QueryWrapper}
     */
    @NonNull
    public static <PARAMS extends BaseParams, ENTITY extends BaseDomain> QueryWrapper<ENTITY> getQuery(@NonNull PARAMS params) {
        QueryWrapper<ENTITY> queryWrapper = new QueryWrapper<>();
        List<String> groupColumn = params.getGroupColumn();
        if (!CollectionUtils.isEmpty(groupColumn)) {
            String[] array = groupColumn.toArray(new String[0]);
            queryWrapper.groupBy(array);
        }
        if (!CollectionUtils.isEmpty(params.getSortColumn())) {
            String[] array = params.getSortColumn().toArray(new String[0]);
            queryWrapper.orderBy(true, SortTypeEnum.ASC.getValue().equals(params.getSortType()), array);
        }
        return queryWrapper;
    }
}
