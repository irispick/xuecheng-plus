package com.xuecheng.base.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author Iris
 * @version 1.0
 * @description 分页查询通用参数
 * @date 2023/2/7 18:04
 */
@Data
@ToString
public class PageParams {

    // 当前页码默认值
    public static final long DEFAULT_PAGE_CURRENT = 1L;

    // 每页记录默认值
    public static final long DEFAULT_PAGE_SIZE = 10L;

    // 当前页码
    private long pageNo = DEFAULT_PAGE_CURRENT;

    // 每页记录数默认值
    private long pageSize = DEFAULT_PAGE_SIZE;

    public PageParams() {
    }

    public PageParams(long pageNo, long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
