package com.pyj.paris.util;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Component
public class PbPageUtils {

    private int page;     // 현재 페이지 번호(요청 파라미터로 받는다.)
    private int total;    // 전체 항목의 개수(DB에서 구한 뒤 받는다.)
    private int display;  // 한 페이지에 표시할 항목의 개수(요청 파라미터로 받는다.)
    private int begin;    // 한 페이지에 표시되는 항목의 시작 번호(계산한다.)
    private int end;      // 한 페이지에 표시되는 항목의 종료 번호(계산한다.)

    private int totalPage;        // 전체 페이지의 개수(계산한다.)
    private int pagePerBlock=5;  // 한 블록에 표시되는 페이지의 개수(임의로 정한다.)
    private int beginPage;        // 한 블록에 표시되는 페이지의 시작 번호(계산한다.)
    private int endPage;          // 한 블록에 표시되는 페이지의 종료 번호(계산한다.)

    public void setPaging(int page, int total, int display) {

        /* 한 페이지를 나타낼 때 필요한 정보 */

        // 받은 정보 저장
        this.page = page;
        this.total = total;
        this.display = display;

        // 계산한 정보 저장
        begin = (page - 1) * display + 1;
        end = begin + display - 1;
        end = end > total ? total : end;


        /* 전체 페이지를 나타낼 때 필요한 정보 */

        // 전체 페이지 계산
        totalPage = (int)Math.ceil((double)total / display);

        // 각 블록의 시작 페이지와 종료 페이지 계산
        beginPage = ((page - 1) / pagePerBlock) * pagePerBlock + 1;
        endPage = beginPage + pagePerBlock - 1;
        endPage = endPage > totalPage ? totalPage : endPage;

    }

    public String getMvcPaging(String url) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div class=\"pagination\">");

        // 이전 페이지 버튼
        if (page > 1) {
            sb.append("<a href=\"").append(url).append("?page=").append(page - 1)
                    .append("\" class=\"previous\">Prev</a>");
        }

        // 첫 번째 페이지
        if (beginPage > 1) {
            sb.append("<a href=\"").append(url).append("?page=1\" class=\"page\">1</a>");
            if (beginPage > 2) {
                sb.append("<span class=\"extra\">&hellip;</span>");
            }
        }

        // 페이지 번호 (현재 페이지 기준으로 앞뒤 2개씩)
        for (int p = beginPage; p <= endPage; p++) {
            if (p == page) {
                sb.append("<a href=\"#\" class=\"page active\">").append(p).append("</a>");
            } else {
                sb.append("<a href=\"").append(url).append("?page=").append(p)
                        .append("\" class=\"page\">").append(p).append("</a>");
            }
        }

        // 마지막 페이지
        if (endPage < totalPage) {
            if (endPage < totalPage - 1) {
                sb.append("<span class=\"extra\">&hellip;</span>");
            }
            sb.append("<a href=\"").append(url).append("?page=").append(totalPage)
                    .append("\" class=\"page\">").append(totalPage).append("</a>");
        }

        // 다음 페이지 버튼
        if (page < totalPage) {
            sb.append("<a href=\"").append(url).append("?page=").append(page + 1)
                    .append("\" class=\"next\">Next</a>");
        }

        sb.append("</div>");
        return sb.toString();
    }

    public String getAjaxPaging() {

        StringBuilder sb = new StringBuilder();

        sb.append("<div class=\"paging\">");

        // 이전 블록
        if(beginPage == 1) {
            sb.append("<a>이전</a>");
        } else {
            sb.append("<a href=\"javascript:fnAjaxPaging(" + (beginPage-1) + ")\">이전</a>");
        }

        // 페이지 번호
        for(int p = beginPage; p <= endPage; p++) {
            if(p == page) {
                sb.append("<a class=\"now_page\">" + p + "</a>");
            } else {
                sb.append("<a href=\"javascript:fnAjaxPaging(" + p + ")\">" + p + "</a>");
            }
        }

        // 다음 블록
        if(endPage == totalPage) {
            sb.append("<a>다음</a>");
        } else {
            sb.append("<a href=\"javascript:fnAjaxPaging(" + (endPage+1) + ")\">다음</a>");
        }

        sb.append("</div>");

        return sb.toString();

    }

}