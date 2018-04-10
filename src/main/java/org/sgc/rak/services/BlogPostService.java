package org.sgc.rak.services;

import org.sgc.rak.model.BlogPost;
import org.sgc.rak.repositories.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

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
     * Creates a new blog post.
     *
     * @param post The blog post to create.
     */
    public void createBlogPost(BlogPost post) {
        post.setCreateDate(new Date());
        repository.save(post);
    }

    /**
     * Deletes a blog post.
     *
     * @param id The ID of the blog post to delete.
     */
    public void deleteBlogPost(Long id) {
        repository.deleteById(id);
    }

    /**
     * Returns blog posts.
     *
     * @param pageInfo How to sort the data and what page of the data to return.
     * @return The list of blog posts.
     */
    public Page<BlogPost> getBlogPosts(Pageable pageInfo) {
        return repository.findAll(pageInfo);
    }
}
