package org.sgc.rak.services;

import org.sgc.rak.model.BlogPost;
import org.sgc.rak.repositories.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service for manipulating blog posts.
 */
@Service
public class BlogPostService {

    private final BlogPostRepository repository;

    @Autowired
    public BlogPostService(BlogPostRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns blog posts.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of blog posts.
     */
    public Page<BlogPost> getBlogPosts(Pageable pageInfo) {
        return repository.findAllByOrderByCreateDateDesc(pageInfo);
    }
}
