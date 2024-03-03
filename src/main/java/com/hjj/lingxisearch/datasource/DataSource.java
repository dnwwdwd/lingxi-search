package com.hjj.lingxisearch.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 数据源接口（新街入的接口必须实现它）
 * @param <T>
 */
public interface DataSource<T> {
    Page<T>  doSearch(String searchText, long pageNum, long pageSize);
}
