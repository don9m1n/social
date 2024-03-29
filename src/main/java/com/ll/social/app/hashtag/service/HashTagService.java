package com.ll.social.app.hashtag.service;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.hashtag.entity.HashTag;
import com.ll.social.app.hashtag.repository.HashTagRepository;
import com.ll.social.app.keyword.entity.Keyword;
import com.ll.social.app.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final KeywordService keywordService;
    private final HashTagRepository hashTagRepository;

    public void applyHashTags(Article article, String hashTagContent) {

        List<HashTag> oldHashTags = getHashTags(article);

        List<String> keywordContentList = Arrays.stream(hashTagContent.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<HashTag> needToDelete = new ArrayList<>();
        for (HashTag oldHashTag : oldHashTags) {
            boolean contains = keywordContentList
                    .stream()
                    .anyMatch(s -> s.equals(oldHashTag.getKeyword().getContent()));

            if (!contains) {
                needToDelete.add(oldHashTag);
            }
        }

        needToDelete.forEach(hashTag -> hashTagRepository.delete(hashTag));
        keywordContentList.forEach(keywordContent -> saveHashTag(article, keywordContent));
    }

    private HashTag saveHashTag(Article article, String keywordContent) {
        Keyword keyword = keywordService.save(keywordContent);

        Optional<HashTag> optHashTag = hashTagRepository.findByArticleIdAndKeywordId(article.getId(), keyword.getId());

        if (optHashTag.isPresent()) {
            return optHashTag.get();
        }

        HashTag hashTag = HashTag.builder()
                .article(article)
                .keyword(keyword)
                .build();

        return hashTagRepository.save(hashTag);
    }

    public List<HashTag> getHashTags(Article article) {
        return hashTagRepository.findAllByArticleId(article.getId());
    }

    public List<HashTag> getHashTagsByArticleIdIn(long[] ids) {
        return hashTagRepository.findAllByArticleIdIn(ids);
    }
}
