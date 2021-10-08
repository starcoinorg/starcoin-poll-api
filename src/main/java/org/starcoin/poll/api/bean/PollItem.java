package org.starcoin.poll.api.bean;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "poll_item", uniqueConstraints = {
        @UniqueConstraint(name = "uniq_network_id_on_chain", columnNames = {"network", "id_on_chain"})
})
@DynamicInsert
public class PollItem {

    /**
     * 代理 Id
     */
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue
    private Long id;

    /**
     * 让 network 以及 idOnChain 组成领域键。
     */
    @Column(length = 20)
    private String network;

    /**
     * 让 network 以及 idOnChain 组成领域键。
     */
    @Column(name = "id_on_chain", length = 100)
    private String idOnChain;

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
    private Long createdAt;

    @Column
    private Long updatedAt;

    @Column
    private Long deletedAt;

    @Transient
    private Long quorumVotes;

    @Transient
    private Long onChainStartTime;

    @Transient
    private Long onChainEndTime;

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

    public String getIdOnChain() {
        return idOnChain;
    }

    public void setIdOnChain(String idOnChain) {
        this.idOnChain = idOnChain;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getQuorumVotes() {
        return quorumVotes;
    }

    public void setQuorumVotes(Long quorumVotes) {
        this.quorumVotes = quorumVotes;
    }

    public Long getOnChainStartTime() {
        return onChainStartTime;
    }

    public void setOnChainStartTime(Long onChainStartTime) {
        this.onChainStartTime = onChainStartTime;
    }

    public Long getOnChainEndTime() {
        return onChainEndTime;
    }

    public void setOnChainEndTime(Long onChainEndTime) {
        this.onChainEndTime = onChainEndTime;
    }


    @Override
    public String toString() {
        return "PollItem{" +
                "id=" + id +
                ", network='" + network + '\'' +
                ", idOnChain='" + idOnChain + '\'' +
                ", title='" + title + '\'' +
                ", titleEn='" + titleEn + '\'' +
                ", description='" + description + '\'' +
                ", descriptionEn='" + descriptionEn + '\'' +
                ", creator='" + creator + '\'' +
                ", againstVotes=" + againstVotes +
                ", forVotes=" + forVotes +
                ", link='" + link + '\'' +
                ", typeArgs1='" + typeArgs1 + '\'' +
                ", endTime=" + endTime +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", quorumVotes=" + quorumVotes +
                ", onChainStartTime=" + onChainStartTime +
                ", onChainEndTime=" + onChainEndTime +
                '}';
    }
}
