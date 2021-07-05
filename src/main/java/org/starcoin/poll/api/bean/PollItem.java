package org.starcoin.poll.api.bean;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "poll_item")
@DynamicInsert
public class PollItem {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue
    private Long id;

    @Column(length = 20)
    private String network;

    @Column
    private String title;

    @Column
    private String titleEn;

    @Column(length = 5000)
    private String description;

    @Column(length = 5000)
    private String descriptionEn;

    @Column(length = 70)
    private String creator;

    @Column
    private Long againstVotes;

    @Column
    private Long forVotes;

    @Column
    private String link;

    @Column(name = "type_args_1")
    private String typeArgs1;

    @Column
    private Long endTime;

    @Column
    private Integer status;

    @Column
    private Timestamp createdAt;

    @Column
    private Timestamp updatedAt;

    @Column
    private Timestamp deletedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getAgainstVotes() {
        return againstVotes;
    }

    public void setAgainstVotes(Long againstVotes) {
        this.againstVotes = againstVotes;
    }

    public Long getForVotes() {
        return forVotes;
    }

    public void setForVotes(Long forVotes) {
        this.forVotes = forVotes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTypeArgs1() {
        return typeArgs1;
    }

    public void setTypeArgs1(String typeArgs1) {
        this.typeArgs1 = typeArgs1;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }
}
