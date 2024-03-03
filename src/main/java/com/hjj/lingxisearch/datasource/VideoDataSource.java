package com.hjj.lingxisearch.datasource;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

@Service
public class VideoDataSource implements DataSource {
    @Override
    public Page doSearch(String searchText, long pageNum, long pageSize) {
        return null;
    }
}
