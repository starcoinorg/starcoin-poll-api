package org.starcoin.poll.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starcoin.poll.api.bean.PollItem;
import org.starcoin.poll.api.dao.PollItemRepository;
import org.starcoin.poll.api.vo.PageResult;

@Service
public class PollItemService {

    private static final Logger logger = LoggerFactory.getLogger(PollItemService.class);

    private final PollItemRepository pollItemRepository;

    @Autowired
    public PollItemService(PollItemRepository pollItemRepository) {
        this.pollItemRepository = pollItemRepository;
    }

    public boolean add(Integer againstVotes, String creator, String description, String descriptionEn, Long endTime, Integer forVotes, String link, String title, String titleEn, String typeArgs1, String status, String network) {
        PollItem item = getByTitleOrTitleEn(title, titleEn);
        if (null != item) {
            return false;
        }
        item = new PollItem();
        item.setAgainstVotes(againstVotes);
        item.setCreator(creator);
        item.setDescription(description);
        item.setDescriptionEn(descriptionEn);
        item.setEndTime(endTime);
        item.setForVotes(forVotes);
        item.setLink(link);
        item.setTitle(title);
        item.setTitleEn(titleEn);
        item.setTypeArgs1(typeArgs1);
        item.setStatus(status);
        item.setNetwork(network);
        pollItemRepository.save(item);
        return true;
    }

    public PollItem get(Long id) {
        return pollItemRepository.findById(id).orElse(null);
    }

    public PollItem getByTitleOrTitleEn(String title, String titleEn) {
        return pollItemRepository.findByTitleOrTitleEn(title, titleEn);
    }

    public PageResult<PollItem> getListByNetwork(String network, int page, int size) {
        Page<PollItem> list = pollItemRepository.findByNetwork(network, PageRequest.of(page, size));
        PageResult<PollItem> result = new PageResult<>();
        result.setCurrentPage(page);
        result.setTotalPage(list.getTotalPages());
        result.setTotalElements((int) list.getTotalElements());
        result.setList(list.getContent());
        return result;
    }

    @Transactional
    public boolean modif(Long id, Integer againstVotes, String creator, String description, String descriptionEn, Long endTime, Integer forVotes, String link, String title, String titleEn, String typeArgs1, String status, String network) {
        PollItem item = get(id);
        if (null == item) {
            return false;
        }
        if (null != againstVotes) {
            item.setAgainstVotes(againstVotes);
        }
        if (null != creator && creator.length() > 0) {
            item.setCreator(creator);
        }
        if (null != description && description.length() > 0) {
            item.setDescription(description);
        }
        if (null != descriptionEn && descriptionEn.length() > 0) {
            item.setDescription(descriptionEn);
        }
        if (null != endTime) {
            item.setEndTime(endTime);
        }
        if (null != forVotes) {
            item.setForVotes(forVotes);
        }
        if (null != link && link.length() > 0) {
            item.setLink(link);
        }
        if (null != title && title.length() > 0) {
            item.setTitle(title);
        }
        if (null != titleEn && titleEn.length() > 0) {
            item.setTitle(titleEn);
        }
        if (null != typeArgs1 && typeArgs1.length() > 0) {
            item.setTypeArgs1(typeArgs1);
        }
        if (null != status && status.length() > 0) {
            item.setStatus(status);
        }
        if (null != network && network.length() > 0) {
            item.setNetwork(network);
        }
        pollItemRepository.save(item);
        return true;
    }

    @Transactional
    public void del(Long id) {
        PollItem item = get(id);
        if (null == item) {
            return;
        }
        pollItemRepository.deleteById(id);
    }
}
