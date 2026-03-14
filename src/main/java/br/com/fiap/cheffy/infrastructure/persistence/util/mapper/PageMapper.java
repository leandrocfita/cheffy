package br.com.fiap.cheffy.infrastructure.persistence.util.mapper;

import br.com.fiap.cheffy.application.profile.dto.PageInputPort;
import br.com.fiap.cheffy.application.profile.dto.PageOutputPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {

    public Pageable toPageable(PageInputPort page) {
        return PageRequest.of(
                page.getPage(),
                page.getSize(),
                Sort.by(
                        Sort.Direction.fromString(page.getSort().getDirection().name()),
                        page.getSort().getField()
                )
        );
    }

    public <T> PageOutputPort<T> toPageOutputPort(Page<T> page) {
        return new PageOutputPort<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
