package com.hjj.lingxisearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjj.lingxisearch.common.BaseResponse;
import com.hjj.lingxisearch.common.ErrorCode;
import com.hjj.lingxisearch.common.ResultUtils;
import com.hjj.lingxisearch.exception.BusinessException;
import com.hjj.lingxisearch.model.dto.post.PostQueryRequest;
import com.hjj.lingxisearch.model.dto.search.SearchAllRequest;
import com.hjj.lingxisearch.model.dto.user.UserQueryRequest;
import com.hjj.lingxisearch.model.entity.Picture;
import com.hjj.lingxisearch.model.vo.PostVO;
import com.hjj.lingxisearch.model.vo.SearchVO;
import com.hjj.lingxisearch.model.vo.UserVO;
import com.hjj.lingxisearch.service.PictureService;
import com.hjj.lingxisearch.service.PostService;
import com.hjj.lingxisearch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping("/search")
public class SearchController {
    @Resource
    private PictureService pictureService;

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchAllRequest searchAllRequest, HttpServletRequest request) {
        String searchText = searchAllRequest.getSearchText();


        CompletableFuture<Page<UserVO>> userCompletableFuture = CompletableFuture.supplyAsync(() -> {
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
            return userVOPage;
        });

        CompletableFuture<Page<PostVO>> postCompletableFuture = CompletableFuture.supplyAsync(() -> {
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<PostVO> postVOPage = postService.listPostVOPage(postQueryRequest, request);
            return postVOPage;
        });

        CompletableFuture<Page<Picture>> pictureCompletableFuture = CompletableFuture.supplyAsync(() -> {
            Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
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

            return ResultUtils.success(searchVO);
        } catch (Exception e) {
            log.error("查询异常");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
        }
    }
}
