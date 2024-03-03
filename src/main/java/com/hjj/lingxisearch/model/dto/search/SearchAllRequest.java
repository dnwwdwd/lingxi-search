package com.hjj.lingxisearch.model.dto.search;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchAllRequest implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 搜索类型
     */
    private String type;

    /**
     * 页数
     */
    private long pageNum;

    /**
     * 页面大小
     */
    private long pageSize;
    private static final long serialVersionUID = 1L;
}
