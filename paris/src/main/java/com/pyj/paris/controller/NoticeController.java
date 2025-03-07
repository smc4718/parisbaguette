package com.pyj.paris.controller;

import com.pyj.paris.dto.NoticeDto;
import com.pyj.paris.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public String list() {
        return "notice/list";
    }

    @ResponseBody
    @GetMapping(value = "/getList.do", produces = "application/json")
    public Map<String, Object> getList(HttpServletRequest request) {
        return noticeService.getNoticeList(request);
    }

    @GetMapping("/detail.do")
    public String detail(int noticeNo, Model model) {
        noticeService.getNotice(noticeNo, model);
        return "notice/detail";
    }

    @GetMapping("/write.form")
    public String write() {
        return "notice/write";
    }

    @PostMapping(value = "/addNotice.do")
    public String addNotice(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("addResult", noticeService.addNotice(request));
        return "redirect:/notice/list";
    }

    @PostMapping("/edit.form")
    public String edit(@ModelAttribute("notice") NoticeDto notice) {
        return "notice/edit";
    }

    @PostMapping("/modifyNotice.do")
    public String modifyNotice(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        int modifyResult = noticeService.modifyNotice(request);
        redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
        return "redirect:/notice/detail.do?noticeNo=" + request.getParameter("noticeNo");
    }

    @PostMapping("/remove.do")
    public String remove(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        int removeResult = noticeService.removeNotice(request);
        redirectAttributes.addFlashAttribute("removeResult", removeResult);
        return "redirect:/notice/list";
    }

    @ResponseBody
    @PostMapping("/increaseHit.do")
    public int increaseHit(@RequestParam("noticeNo") int noticeNo) {
        int increaseHit = noticeService.increaseHit(noticeNo);
        return increaseHit;
    }

    @ResponseBody
    @PostMapping(value = "/imageUpload.do", produces = "application/json")
    public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest) {
        return noticeService.imageUpload(multipartRequest);
    }
}
