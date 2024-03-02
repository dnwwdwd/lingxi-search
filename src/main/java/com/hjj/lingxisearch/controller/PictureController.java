package com.hjj.lingxisearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjj.lingxisearch.common.BaseResponse;
import com.hjj.lingxisearch.common.ErrorCode;
import com.hjj.lingxisearch.common.ResultUtils;
import com.hjj.lingxisearch.exception.ThrowUtils;
import com.hjj.lingxisearch.model.dto.picture.PictureQueryRequest;
import com.hjj.lingxisearch.model.entity.Picture;
import com.hjj.lingxisearch.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest) {
        long current = pictureQueryRequest.getCurrent();
        long pageSize = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, pageSize);
        return ResultUtils.success(picturePage);
    }
}
