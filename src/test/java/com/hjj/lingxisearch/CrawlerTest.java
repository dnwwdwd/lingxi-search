package com.hjj.lingxisearch;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hjj.lingxisearch.model.entity.Picture;
import com.hjj.lingxisearch.model.entity.Post;
import com.hjj.lingxisearch.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class CrawlerTest {
    @Resource
    private PostService postService;

    @Test
    void testFetchPicture() throws IOException {
        String url = "https://cn.bing.com/images/search?q=%E5%B0%8F%E9%BB%91%E5%AD%90&first=1&cw=1177&ch=686&ensearch=1&form=BESBTB";
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址url
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            pictureList.add(new Picture(title, murl));
        }
        System.out.println(pictureList);
    }

    @Test
    void testFetchPassage() {
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
        Object o = data.get("records");
        JSONArray records = (JSONArray) o;
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
            log.info("获取文章成功");
        } else {
            log.error("获取文章失败");
        }
    }

    @Test
    void testGetBilibiliTitle() throws IOException {
        String url = "https://www.bilibili.com/video/BV1dx4y1r7ar/?spm_id_from=333.788.0.0&vd_source=54ce90171f33041b8da557323cf4b893";
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".video-desc-container .basic-desc-info[data-v-1d530b8d]");
        for (Element element : elements) {
            System.out.println(element.select(".desc-info-text").get(0).text());
        }

        String s = "s a v";
        s.toUpperCase();
    }
}
