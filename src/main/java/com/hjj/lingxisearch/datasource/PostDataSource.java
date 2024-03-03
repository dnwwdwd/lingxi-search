package com.hjj.lingxisearch.datasource;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.lingxisearch.common.ErrorCode;
import com.hjj.lingxisearch.constant.CommonConstant;
import com.hjj.lingxisearch.exception.BusinessException;
import com.hjj.lingxisearch.exception.ThrowUtils;
import com.hjj.lingxisearch.mapper.PostFavourMapper;
import com.hjj.lingxisearch.mapper.PostMapper;
import com.hjj.lingxisearch.mapper.PostThumbMapper;
import com.hjj.lingxisearch.model.dto.post.PostEsDTO;
import com.hjj.lingxisearch.model.dto.post.PostQueryRequest;
import com.hjj.lingxisearch.model.entity.Post;
import com.hjj.lingxisearch.model.entity.PostFavour;
import com.hjj.lingxisearch.model.entity.PostThumb;
import com.hjj.lingxisearch.model.entity.User;
import com.hjj.lingxisearch.model.vo.PostVO;
import com.hjj.lingxisearch.model.vo.UserVO;
import com.hjj.lingxisearch.service.PostService;
import com.hjj.lingxisearch.service.UserService;
import com.hjj.lingxisearch.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子服务实现
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Service
@Slf4j
public class PostDataSource implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        postQueryRequest.setCurrent(pageNum);
        postQueryRequest.setPageSize(pageSize);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Page<PostVO> postVOPage = postService.listPostVOPage(postQueryRequest, request);
        return postVOPage;
    }
}




