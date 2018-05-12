package org.sgc.rak.services;

import org.sgc.rak.exceptions.NotFoundException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.BlogPost;
import org.sgc.rak.repositories.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * Service for manipulating blog posts.
 */
@Service
public class BlogPostService {

    private final BlogPostRepository repository;

    private final Messages messages;

    @Autowired
    public BlogPostService(BlogPostRepository repository, Messages messages) {
        this.repository = repository;
        this.messages = messages;
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

    /**
     * Updates a blog post.
     *
     * @param post The blog post to update.  The blog post with the {@code id} of the input post is updated with the
     *        new title and body.
     * @return The updated version of the blog post.
     * @throws NotFoundException If no blog post with the specified ID exists.
     */
    public BlogPost updateBlogPost(BlogPost post) {

        Optional<BlogPost> optional = repository.findById(post.getId());
        if (!optional.isPresent()) {
            throw new NotFoundException(messages.get("error.noSuchBlogPost", post.getId()));
        }

        BlogPost currentPost = optional.get();
        currentPost.setTitle(post.getTitle());
        currentPost.setBody(post.getBody());
        currentPost.setViewCount(currentPost.getViewCount() + 1);

        return repository.save(currentPost);
    }
}
