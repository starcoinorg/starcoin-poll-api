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

    @Transactional
    public boolean add(Long againstVotes, String creator, String description, String descriptionEn, Long endTime, Long forVotes, String link, String title, String titleEn, String typeArgs1, Integer status, String network) {
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
        item.setCreatedAt(System.currentTimeMillis());
        item.setUpdatedAt(System.currentTimeMillis());
        pollItemRepository.save(item);
        return true;
    }

    public PollItem get(Long id) {
        return pollItemRepository.findByIdAndDeletedAtIsNull(id);
    }

    public PollItem getByTitleOrTitleEn(String title, String titleEn) {
        return pollItemRepository.findByTitleOrTitleEnAndDeletedAtIsNull(title, titleEn);
    }

    public PageResult<PollItem> getListByNetwork(String network, int page, int size) {
        Page<PollItem> list = pollItemRepository.findByNetworkAndDeletedAtIsNull(network, PageRequest.of(page, size));
        PageResult<PollItem> result = new PageResult<>();
        result.setCurrentPage(page);
        result.setTotalPage(list.getTotalPages());
        result.setTotalElements((int) list.getTotalElements());
        result.setList(list.getContent());
        return result;
    }

    @Transactional
    public boolean modif(Long id, Long againstVotes, String creator, String description, String descriptionEn, Long endTime, Long forVotes, String link, String title, String titleEn, String typeArgs1, Integer status, String network) {
        PollItem item = get(id);
        if (null == item) {
            return false;
        }
        boolean isUpdate = false;
        if (null != againstVotes) {
            item.setAgainstVotes(againstVotes);
            isUpdate = true;
        }
        if (null != creator && creator.length() > 0) {
            item.setCreator(creator);
            isUpdate = true;
        }
        if (null != description && description.length() > 0) {
            item.setDescription(description);
            isUpdate = true;
        }
        if (null != descriptionEn && descriptionEn.length() > 0) {
            item.setDescription(descriptionEn);
            isUpdate = true;
        }
        if (null != endTime) {
            item.setEndTime(endTime);
            isUpdate = true;
        }
        if (null != forVotes) {
            item.setForVotes(forVotes);
            isUpdate = true;
        }
        if (null != link && link.length() > 0) {
            item.setLink(link);
            isUpdate = true;
        }
        if (null != title && title.length() > 0) {
            item.setTitle(title);
            isUpdate = true;
        }
        if (null != titleEn && titleEn.length() > 0) {
            item.setTitle(titleEn);
            isUpdate = true;
        }
        if (null != typeArgs1 && typeArgs1.length() > 0) {
            item.setTypeArgs1(typeArgs1);
            isUpdate = true;
        }
        if (null != status) {
            item.setStatus(status);
            isUpdate = true;
        }
        if (null != network && network.length() > 0) {
            item.setNetwork(network);
            isUpdate = true;
        }
        if (isUpdate) {
            item.setUpdatedAt(System.currentTimeMillis());
            pollItemRepository.saveAndFlush(item);
        }
        return isUpdate;
    }

    @Transactional
    public void del(Long id) {
        PollItem item = get(id);
        if (null == item) {
            return;
        }
        item.setDeletedAt(System.currentTimeMillis());
        pollItemRepository.saveAndFlush(item);
    }
}
