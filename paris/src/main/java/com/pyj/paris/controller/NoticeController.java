package com.pyj.paris.controller;

import com.pyj.paris.dao.NoticeMapper;
import com.pyj.paris.dto.NoticeDto;
import com.pyj.paris.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class NoticeController {

    private final NoticeService noticeService;
    private final NoticeMapper noticeMapper;

    @GetMapping("/list.do")
    public String list(HttpServletRequest request, Model model) {
        noticeService.loadNoticeList(request, model);
        return "notice/list";
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
        return "redirect:/notice/list.do";
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
        return "redirect:/notice/list.do";
    }
}
