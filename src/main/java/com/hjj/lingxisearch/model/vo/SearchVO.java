package com.hjj.lingxisearch.model.vo;

import com.hjj.lingxisearch.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合搜索VO
 */
@Data
public class SearchVO implements Serializable {

    List<UserVO> userVOList;

    List<PostVO> postVOList;

    List<Picture> pictureVOList;

    List<?> dataSourceList;
}
