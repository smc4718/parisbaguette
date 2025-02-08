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
    public String addNotice(HttpServletRequest request, RedirectAttributes redirectAttributes) {    //addResult가 꼭 필요한 건 아니지만, 사용자의 편의를 위해 필요할 수도 있다고 보면 돼.
        redirectAttributes.addFlashAttribute("addResult", noticeService.addNotice(request)); //addResult 없이 바로 return 에서 redirect 해도 된다. 하지만 그러면 공지 추가가 성공했는지, 실패했는지 알 방법이 없다.
        return "redirect:/notice/list.do";                                                          //addResult 있으면, DB에서 INSERT가 실패했는지 확인 가능하다.
          //addResult 없이도 공지를 추가하고 목록으로 이동하는 건 가능함. 하지만 사용자가 공지 등록이 제대로 되었는지 확인할 방법이 없음.
         //그래서 addResult를 활용하면 공지 추가 성공/실패를 사용자에게 보여줄 수 있음. (그걸 위해서 addFlashAttribute가 필요함 : 이것을 통해 리다이렉트 후에도 데이터 유지 가능, 그 데이터를 list.do 같은 곳에서 @ModelAttribute("addResult") Integer addResult로 매개변수에 사용하여 Flash Attribute 값을 가져와 공지 등록 성공/실패 여부를 보여줄 수 있다.)
    }    //그리고 폼제출시 에러가 나도, addFlashAttribute 덕분에 데이터가 한 번 유지되어서, 사용자가 입력했던 값이 없어지지 않고 남아있게 된다. (사용자 편의성 증가)

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
