package com.pyj.paris.service;

import com.pyj.paris.dao.GuideMapper;
import com.pyj.paris.dto.GuideDto;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GuideServiceImpl implements GuideService {

    private final GuideMapper guideMapper;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String YOUTUBE_API_KEY = dotenv.get("YOUTUBE_API_KEY");

    private static final String YOUTUBE_API_URL = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=%s&key=%s";

    @Override
    public List<GuideDto> getGuideList(int page, int size) {
        int offset = (page - 1) * size;
        return guideMapper.selectGuideList(offset, size);
    }

    @Override
    public GuideDto getGuideDetail(int guideNo) {
        return guideMapper.selectGuideDetail(guideNo);
    }

    @Override
    public boolean addGuide(GuideDto guide) {
        GuideDto enriched = enrichWithYoutubeData(guide);
        return guideMapper.insertGuide(enriched) > 0;
    }

    @Override
    public boolean deleteGuide(int guideNo) {
        return guideMapper.deleteGuide(guideNo) > 0;
    }

    private GuideDto enrichWithYoutubeData(GuideDto guide) {
        String videoId = extractVideoId(guide.getYoutubeUrl());
        String embedUrl = "https://www.youtube.com/embed/" + videoId;
        guide.setYoutubeUrl(embedUrl);

        String apiUrl = String.format(YOUTUBE_API_URL, videoId, YOUTUBE_API_KEY);
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

        JSONObject json = new JSONObject(jsonResponse);
        JSONArray items = json.getJSONArray("items");

        if (items.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 YouTube 영상입니다.");
        }

        JSONObject snippet = items.getJSONObject(0).getJSONObject("snippet");

        guide.setTitle(snippet.getString("title"));
        guide.setDescription(snippet.getString("description"));

        return guide;
    }

    private String extractVideoId(String url) {
        if (url == null || url.isBlank()) return null;

        if (url.contains("watch?v=")) {
            return url.substring(url.indexOf("v=") + 2).split("&")[0];
        } else if (url.contains("youtu.be/")) {
            return url.substring(url.lastIndexOf("/") + 1).split("\\?")[0];
        } else if (url.contains("/shorts/")) {
            return url.substring(url.lastIndexOf("/shorts/") + 8).split("\\?")[0];
        }
        return url;
    }
}