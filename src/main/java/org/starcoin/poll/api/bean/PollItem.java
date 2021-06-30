package org.starcoin.poll.api.bean;

import javax.persistence.*;

@Entity
@Table(name = "poll_item")
public class PollItem {

    @Id
    @GeneratedValue
    @Column(nullable = false, unique = true)
    private Long id;

    @Column
    private Integer againstVotes;

    @Column
    private String creator;

    @Column
    private String description;

    @Column
    private String descriptionEn;

    @Column
    private Long endTime;

    @Column
    private Integer forVotes;

    @Column
    private String link;

    @Column
    private String title;

    @Column
    private String titleEn;

    @Column(name = "type_args_1")
    private String typeArgs1;

    @Column
    private String status;

    @Column
    private String network;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAgainstVotes() {
        return againstVotes;
    }

    public void setAgainstVotes(Integer againstVotes) {
        this.againstVotes = againstVotes;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getForVotes() {
        return forVotes;
    }

    public void setForVotes(Integer forVotes) {
        this.forVotes = forVotes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getTypeArgs1() {
        return typeArgs1;
    }

    public void setTypeArgs1(String typeArgs1) {
        this.typeArgs1 = typeArgs1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
