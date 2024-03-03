package com.hjj.lingxisearch.controller;

import com.hjj.lingxisearch.common.BaseResponse;
import com.hjj.lingxisearch.common.ResultUtils;
import com.hjj.lingxisearch.manager.SearchFacade;
import com.hjj.lingxisearch.model.dto.search.SearchAllRequest;
import com.hjj.lingxisearch.model.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/search")
public class SearchController {

    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchAllRequest searchAllRequest, HttpServletRequest request) {
        SearchVO searchVO = searchFacade.searchAll(searchAllRequest, request);
        return ResultUtils.success(searchVO);
    }
}
