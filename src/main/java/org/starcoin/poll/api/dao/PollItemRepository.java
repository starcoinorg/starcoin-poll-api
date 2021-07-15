package org.starcoin.poll.api.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.poll.api.bean.PollItem;

public interface PollItemRepository extends JpaRepository<PollItem, Long> {

    PollItem findByIdAndDeletedAtIsNull(Long id);

    Page<PollItem> findByNetworkAndDeletedAtIsNullOrderByIdDesc(String network, Pageable page);

    PollItem findByTitleOrTitleEnAndDeletedAtIsNull(String title, String titleEn);
}
