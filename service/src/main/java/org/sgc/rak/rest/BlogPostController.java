package org.sgc.rak.rest;

import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.BlogPost;
import org.sgc.rak.reps.PagedDataRep;
import org.sgc.rak.services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST API for blog posts.
 */
@RestController
@RequestMapping(path = "/api/blogPosts")
public class BlogPostController {

    private final BlogPostService blogPostService;

    private final Messages messages;

    @Autowired
    public BlogPostController(BlogPostService blogPostService, Messages messages) {
        this.blogPostService = blogPostService;
        this.messages = messages;
    }

    /**
     * Creates a new blog post.
     *
     * @param post The blog post to create.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    void createBlogPost(@Valid @RequestBody BlogPost post) {
        blogPostService.createBlogPost(post);
    }

    /**
     * Deletes a blog post by ID.
     *
     * @param blogPostId The ID of the blog post to delete.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{blogPostId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBlogPost(@PathVariable long blogPostId) {
        blogPostService.deleteBlogPost(blogPostId);
    }

    /**
     * Returns blog posts.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of blog posts.
     */
    @RequestMapping(method = RequestMethod.GET)
    PagedDataRep<BlogPost> getBlogPosts(@SortDefault(value = "createDate",
            direction = Sort.Direction.DESC) Pageable pageInfo) {
        Page<BlogPost> page = blogPostService.getBlogPosts(pageInfo);
        long start = page.getNumber() * pageInfo.getPageSize();
        long total = page.getTotalElements();
        return new PagedDataRep<>(page.getContent(), start, total);
    }

    /**
     * Updates an existing blog post.
     *
     * @param post The blog post to update.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/{blogPostId}")
    @ResponseStatus(HttpStatus.OK)
    BlogPost updateBlogPost(@PathVariable String blogPostId, @Valid @RequestBody BlogPost post) {

        // Blog post ID must be a number
        Long id;
        try {
            id = Long.parseLong(blogPostId);
        } catch (NumberFormatException nfe) {
            throw new BadRequestException(messages.get("error.invalidBlogPostId", blogPostId));
        }

        // Blog post ID in body must match that in URL
        if (!id.equals(post.getId())) {
            throw new BadRequestException(messages.get("error.blogPostIdMismatch"));
        }

        return blogPostService.updateBlogPost(post);
    }
}
