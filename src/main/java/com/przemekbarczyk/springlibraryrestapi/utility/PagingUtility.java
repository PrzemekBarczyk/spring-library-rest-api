package com.przemekbarczyk.springlibraryrestapi.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagingUtility {

    public Pageable getPaging(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {

        Sort sort = switch (sortDirection) {
            case "descending" -> Sort.by(sortBy).descending();
            case "ascending" -> Sort.by(sortBy).ascending();
            default -> Sort.by(sortBy);
        };

        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
