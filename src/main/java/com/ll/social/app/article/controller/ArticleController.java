package com.ll.social.app.article.controller;

import com.ll.social.app.article.entity.Article;
import com.ll.social.app.article.entity.dto.ArticleForm;
import com.ll.social.app.article.service.ArticleService;
import com.ll.social.app.base.dto.RsData;
import com.ll.social.app.fileupload.entity.GenFile;
import com.ll.social.app.fileupload.service.GenFileService;
import com.ll.social.app.security.dto.MemberContext;
import com.ll.social.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final GenFileService genFileService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String writeForm() {
        // 타임리프 디폴트 prefix = templates/
        return "article/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@AuthenticationPrincipal MemberContext memberContext, @Valid ArticleForm form, MultipartRequest multipartRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "article/write";
        }

        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        log.debug("fileMap : {}", fileMap);

        Article article = articleService.write(memberContext.getId(), form.getSubject(), form.getContent());

        RsData<Map<String, GenFile>> saveFileRsData = genFileService.saveFile(article, fileMap);
        log.debug("saveFileRsDate : {}", saveFileRsData);

        String msg = "%d번 게시물이 작성되었습니다.".formatted(article.getId());
        msg = Util.url.encode(msg);
        return "redirect:/article/%d?msg=%s".formatted(article.getId(), msg);
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Article article = articleService.getForPrintArticleById(id);
        model.addAttribute("article", article);

        return "article/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String modifyForm(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id, Model model) {
        Article article = articleService.getForPrintArticleById(id);

        if (memberContext.getId() != article.getMember().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); // 403
        }

        model.addAttribute("article", article);

        return "article/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@PathVariable Long id, @Valid ArticleForm articleForm) {

        Article article = articleService.getForPrintArticleById(id);

        return "redirect:/article/%d".formatted(id);
    }
}
