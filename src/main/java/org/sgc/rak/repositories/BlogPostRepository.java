package org.sgc.rak.repositories;

import org.sgc.rak.model.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * JPA repository for blog posts.
 */
public interface BlogPostRepository extends PagingAndSortingRepository<BlogPost, Long> {

    Page<BlogPost> findAllByOrderByCreateDateDesc(Pageable pageInfo);
}
