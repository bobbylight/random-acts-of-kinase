package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

/**
 * JPA entity representing an audit record.
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)

    private Long id;

    @Column(name = "user_nm", nullable = false, updatable = false, length = ModelConstants.AUDIT_USER_NAME_MAX)
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = ModelConstants.AUDIT_ACTION_MAX)
    private AuditAction action;

    @Column(updatable = false, length = ModelConstants.AUDIT_IP_ADDRESS_LENGTH_MAX)
    private String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dttm", nullable = false, updatable = false)
    private Date createDate;

    @Column(updatable = false)
    private Boolean success;

    @PrePersist
    public void onCreate() {
        this.createDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getCreateDate() {
        return createDate != null ? new Date(createDate.getTime()) : null;
    }

    public void setCreateDate(Date date) {
        this.createDate = date != null ? new Date(date.getTime()) : null;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("userName", userName)
            .append("action", action)
            .append("ipAddress", ipAddress)
            .append("createDate", createDate)
            .append("success", success)
            .build();
    }
}
