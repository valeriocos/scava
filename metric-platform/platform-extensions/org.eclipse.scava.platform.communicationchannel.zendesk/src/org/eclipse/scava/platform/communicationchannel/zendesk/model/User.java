/*******************************************************************************
 * Copyright (c) 2018 University of York
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.scava.platform.communicationchannel.zendesk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author stephenc
 * @since 05/04/2013 15:32
 */
public class User implements SearchResultEntity {
    private Long id;
    private String url;
    private String name;
    private String externalId;
    private String alias;
    private Date createdAt;
    private Date updatedAt;
    private Boolean active;
    private Boolean verified;
    private Boolean shared;
    private Long localeId;
    private String timeZone;
    private Date lastLoginAt;
    private String email;
    private String phone;
    private String signature;
    private String details;
    private String notes;
    private Long organizationId;
    private Role role;
    private Long customRoleId;
    private Boolean moderator;
    private TicketRestriction ticketRestriction;
    private Boolean onlyPrivateComments;
    private List<String> tags;
    private Boolean suspended;
    private Attachment photo;
    private List<Identity> identities;
    private String remotePhotoUrl;
    private Map<String, Object> userFields;

    public User() {
    }

    public User(Boolean verified, String name, String email) {
        this.name = name;
        this.email = email;
        this.verified = verified;
    }

    public User(Boolean verified, String name, List<Identity> identities) {
        this.verified = verified;
        this.name = name;
        this.identities = identities;
    }

    public User(Boolean verified, String name, Identity... identities) {
        this.verified = verified;
        this.name = name;
        this.identities = new ArrayList<Identity>(Arrays.asList(identities));
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, List<Identity> identities) {
        this.name = name;
        this.identities = identities;
    }

    public User(String name, Identity... identities) {
        this.name = name;
        this.identities = new ArrayList<Identity>(Arrays.asList(identities));
    }

    public List<Identity> getIdentities() {
        return identities;
    }

    public void setIdentities(List<Identity> identities) {
        this.identities = identities;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @JsonProperty("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("custom_role_id")
    public Long getCustomRoleId() {
        return customRoleId;
    }

    public void setCustomRoleId(Long customRoleId) {
        this.customRoleId = customRoleId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("external_id")
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("last_login_at")
    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    @JsonProperty("locale_id")
    public Long getLocaleId() {
        return localeId;
    }

    public void setLocaleId(Long localeId) {
        this.localeId = localeId;
    }

    public Boolean getModerator() {
        return moderator;
    }

    public void setModerator(Boolean moderator) {
        this.moderator = moderator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @JsonProperty("only_private_comments")
    public Boolean getOnlyPrivateComments() {
        return onlyPrivateComments;
    }

    public void setOnlyPrivateComments(Boolean onlyPrivateComments) {
        this.onlyPrivateComments = onlyPrivateComments;
    }

    @JsonProperty("organization_id")
    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Attachment getPhoto() {
        return photo;
    }

    public void setPhoto(Attachment photo) {
        this.photo = photo;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @JsonProperty("ticket_restriction")
    public TicketRestriction getTicketRestriction() {
        return ticketRestriction;
    }

    public void setTicketRestriction(TicketRestriction ticketRestriction) {
        this.ticketRestriction = ticketRestriction;
    }

    @JsonProperty("time_zone")
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @JsonProperty("updated_at")
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("user_fields")
    public Map<String, Object> getUserFields() {
        return userFields;
    }

    public void setUserFields(Map<String, Object> userFields) {
        this.userFields = userFields;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @JsonProperty("remote_photo_url")
    public String getRemotePhotoUrl() {
        return remotePhotoUrl;
    }

    public void setRemotePhotoUrl(String remotePhotoUrl) {
        this.remotePhotoUrl = remotePhotoUrl;
    }

}
