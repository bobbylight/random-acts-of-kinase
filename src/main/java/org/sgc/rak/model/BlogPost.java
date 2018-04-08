package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * JPA entity representing a blog post.
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogPost {

    private static final int MAX_BODY_TOSTRING_LENGTH = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_post_id")
    private Long id;

    @Column(length = ModelConstants.BLOG_POST_TITLE_LENGTH_MAX, nullable = false)
    @Length(min = ModelConstants.BLOG_POST_TITLE_LENGTH_MIN,
        max = ModelConstants.BLOG_POST_TITLE_LENGTH_MAX,
        message = "The 'title' field must have at least 5 characters")
    @NotEmpty(message = "The 'title' field is required")
    private String title;

    @Column(length = ModelConstants.BLOG_POST_BODY_LENGTH_MAX)
    @Length(max = ModelConstants.BLOG_POST_BODY_LENGTH_MAX,
        message = "The 'body' field must be less than 1048576 characters")
    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreateDate() {
        return createDate != null ? new Date(createDate.getTime()) : null;
    }

    public void setCreateDate(Date date) {
        this.createDate = date != null ? new Date(date.getTime()) : null;
    }

    @Override
    public String toString() {

        String shortenedBody = StringUtils.abbreviate(body, MAX_BODY_TOSTRING_LENGTH);

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("title", title)
            .append("body", shortenedBody)
            .append("createDate", createDate)
            .build();
    }
}
