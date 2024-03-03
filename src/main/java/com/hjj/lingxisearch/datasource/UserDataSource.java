package com.hjj.lingxisearch.datasource;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjj.lingxisearch.common.ErrorCode;
import com.hjj.lingxisearch.constant.CommonConstant;
import com.hjj.lingxisearch.constant.UserConstant;
import com.hjj.lingxisearch.exception.BusinessException;
import com.hjj.lingxisearch.exception.ThrowUtils;
import com.hjj.lingxisearch.mapper.UserMapper;
import com.hjj.lingxisearch.model.dto.user.UserQueryRequest;
import com.hjj.lingxisearch.model.entity.User;
import com.hjj.lingxisearch.model.enums.UserRoleEnum;
import com.hjj.lingxisearch.model.vo.LoginUserVO;
import com.hjj.lingxisearch.model.vo.UserVO;
import com.hjj.lingxisearch.service.UserService;
import com.hjj.lingxisearch.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO> {

    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(pageNum);
        userQueryRequest.setPageSize(pageSize);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        return userVOPage;
    }
}
