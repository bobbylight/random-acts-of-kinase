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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "blog_post_id")
    private Long id;

    @Column(nullable = false)
    @Length(min = 5, max = 128, message = "*Your title must have at least 5 characters")
    @NotEmpty(message = "*Please provide title")
    private String title;

    @Column(length = 1048576)
    @Length(max = 1048576, message = "Your post body must be less than 1048576 characters")
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
        return createDate;
    }

    public void setCreateDate(Date date) {
        this.createDate = date;
    }

    @Override
    public String toString() {

        String shortenedBody = StringUtils.abbreviate(body, 10);

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("title", title)
            .append("body", shortenedBody)
            .append("createDate", createDate)
            .build();
    }
}
