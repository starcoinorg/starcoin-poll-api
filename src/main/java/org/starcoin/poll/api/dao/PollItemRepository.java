package org.starcoin.poll.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.poll.api.bean.PollItem;

public interface PollItemRepository extends JpaRepository<PollItem, Long> {
}
