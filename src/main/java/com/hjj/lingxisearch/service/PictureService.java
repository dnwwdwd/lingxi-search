package com.hjj.lingxisearch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjj.lingxisearch.model.entity.Picture;

/**
 * 图片服务
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface PictureService {
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
