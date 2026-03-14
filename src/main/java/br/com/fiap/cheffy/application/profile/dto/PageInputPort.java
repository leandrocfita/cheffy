package br.com.fiap.cheffy.application.profile.dto;

public class PageInputPort {

    private final int page;
    private final int size;
    private final SortRequestPort sort;

    public PageInputPort(int page, int size, SortRequestPort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public SortRequestPort getSort() {
        return sort;
    }
}
