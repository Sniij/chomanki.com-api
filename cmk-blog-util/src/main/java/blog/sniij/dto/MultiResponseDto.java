package blog.sniij.dto;

import org.springframework.data.domain.Page;

import java.util.List;


public class MultiResponseDto<T> {
    private List<T> data;
    private PageInfo pageInfo;

    public MultiResponseDto(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber() + 1,
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public List<T> getData() {
        return data;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }
}
