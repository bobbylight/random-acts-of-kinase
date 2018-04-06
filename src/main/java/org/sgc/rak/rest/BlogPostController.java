package org.sgc.rak.rest;

import org.sgc.rak.model.BlogPost;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for blog posts.
 */
@RestController
@RequestMapping(path = "/api/blogPosts")
public class BlogPostController {

    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    /**
     * Returns blog posts.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of blog posts.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<BlogPost> getBlogPosts(@SortDefault("createDate") Pageable pageInfo) {
        Page<BlogPost> page = blogPostService.getBlogPosts(pageInfo);
        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }
}
