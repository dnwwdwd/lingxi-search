package com.hjj.lingxisearch.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hjj.lingxisearch.model.entity.Post;
import com.hjj.lingxisearch.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import javax.json.JsonArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
public class FetchInitPostList implements CommandLineRunner {
    @Resource
    private PostService postService;

    @Override
    public void run(String... args) throws Exception {

        // 1. 请求数据
        String json = "{\n" +
                "  \"current\": 1,\n" +
                "  \"pageSize\": 8,\n" +
                "  \"sortField\": \"createTime\",\n" +
                "  \"sortOrder\": \"descend\",\n" +
                "  \"category\": \"文章\",\n" +
                "  \"tags\": [],\n" +
                "  \"reviewStatus\": 1\n" +
                "}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
        // 2. json转为对象
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JsonArray records = (JsonArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            postList.add(post);
        }
        boolean b = postService.saveBatch(postList);
        if (b) {
            log.info("获取初始化帖子列表成功，条数 = {}", postList.size());
        } else {
            log.error("获取初始化帖子列表失败");
        }
    }
}
