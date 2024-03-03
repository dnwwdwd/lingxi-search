package com.hjj.lingxisearch.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjj.lingxisearch.common.ErrorCode;
import com.hjj.lingxisearch.datasource.*;
import com.hjj.lingxisearch.exception.BusinessException;
import com.hjj.lingxisearch.exception.ThrowUtils;
import com.hjj.lingxisearch.model.dto.post.PostQueryRequest;
import com.hjj.lingxisearch.model.dto.search.SearchAllRequest;
import com.hjj.lingxisearch.model.dto.user.UserQueryRequest;
import com.hjj.lingxisearch.model.entity.Picture;
import com.hjj.lingxisearch.model.enums.SearchTypeEnum;
import com.hjj.lingxisearch.model.vo.PostVO;
import com.hjj.lingxisearch.model.vo.SearchVO;
import com.hjj.lingxisearch.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class SearchFacade {

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    public SearchVO searchAll(SearchAllRequest searchAllRequest, HttpServletRequest request) {
        String searchText = searchAllRequest.getSearchText();
        String type = searchAllRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        long pageNum = searchAllRequest.getPageNum();
        long pageSize = searchAllRequest.getPageSize();
        if (searchTypeEnum == null) {
            CompletableFuture<Page<UserVO>> userCompletableFuture = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userDataSource.doSearch(searchText, pageNum, pageSize);
                return userVOPage;
            });

            CompletableFuture<Page<PostVO>> postCompletableFuture = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postDataSource.doSearch(searchText, pageNum, pageSize);
                return postVOPage;
            });

            CompletableFuture<Page<Picture>> pictureCompletableFuture = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureDataSource.doSearch(searchText, pageNum, pageSize);
                return picturePage;
            });

            CompletableFuture.allOf(userCompletableFuture, postCompletableFuture, pictureCompletableFuture).join();
            try{
                Page<UserVO> userVOPage = userCompletableFuture.get();
                Page<PostVO> postVOPage = postCompletableFuture.get();
                Page<Picture> picturePage = pictureCompletableFuture.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setUserVOList(userVOPage.getRecords());
                searchVO.setPostVOList(postVOPage.getRecords());
                searchVO.setPictureVOList(picturePage.getRecords());

                return searchVO;
            } catch (Exception e) {
                log.error("查询异常");
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else {
            SearchVO searchVO = new SearchVO();
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, pageNum, pageSize);
            searchVO.setDataSourceList(page.getRecords());
            return searchVO;
        }
        //     switch(searchTypeEnum) {
        //         case POST:
        //             PostQueryRequest postQueryRequest = new PostQueryRequest();
        //             postQueryRequest.setSearchText(searchText);
        //             Page<PostVO> postVOPage = postService.listPostVOPage(postQueryRequest, request);
        //             searchVO.setPostVOList(postVOPage.getRecords());
        //             break;
        //         case USER:
        //             UserQueryRequest userQueryRequest = new UserQueryRequest();
        //             userQueryRequest.setUserName(searchText);
        //             Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        //             searchVO.setUserVOList(userVOPage.getRecords());
        //             break;
        //         case PICTURE:
        //             Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
        //             searchVO.setPictureVOList(picturePage.getRecords());
        //             break;
        //         default:
        //     }
        //     return searchVO;
        // }
    }
}
