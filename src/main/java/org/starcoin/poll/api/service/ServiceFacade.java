package org.starcoin.poll.api.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.poll.api.bean.PollItem;
import org.starcoin.poll.api.dao.PollItemRepository;

import java.util.Objects;

@Service
public class ServiceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFacade.class);

    @Autowired
    private ContractService contractService;

    @Autowired
    private PollItemService pollItemService;

    @Autowired
    private PollItemRepository pollItemRepository;

    public void updateByOnChainInfo(PollItem pollItem) {
        //if (pollItem.getEndTime().compareTo(System.currentTimeMillis()) > 0) { // 投票时间尚未结束，根据链上状态更新数据库中的 status
        try {
            int status = contractService.getPollStatus(pollItem.getIdOnChain(), pollItem.getCreator(), pollItem.getTypeArgs1());
            Integer oldStatus = pollItem.getStatus();
            //pollItem.setStatus(status);
            if (!Objects.equals(oldStatus, status) && status > (oldStatus != null ? oldStatus : -1)) {
                //pollItemService.asyncUpdateStatus(id, status);
                pollItem.setStatus(status);
                pollItem.setUpdatedAt(System.currentTimeMillis());
                pollItemRepository.save(pollItem);
            }
        } catch (RuntimeException e) {
            LOGGER.error("Update poll status by on-chain info error.", e);
        }
        //}
        try {
            JSONObject pollObj = contractService.getPollVotes(pollItem.getCreator(), pollItem.getTypeArgs1());
            if (pollObj.containsKey("for_votes")) {
                pollItem.setForVotes(pollObj.getLongValue("for_votes"));
            }
            if (pollObj.containsKey("against_votes")) {
                pollItem.setAgainstVotes(pollObj.getLongValue("against_votes"));
            }
            if (pollObj.containsKey("quorum_votes")) {
                pollItem.setQuorumVotes(pollObj.getLongValue("quorum_votes"));
            }
            if (pollObj.containsKey("start_time")) {
                pollItem.setOnChainStartTime(pollObj.getLongValue("start_time"));
            }
            if (pollObj.containsKey("end_time")) {
                pollItem.setEndTime(pollObj.getLongValue("end_time"));
            }
            pollItemRepository.save(pollItem);
        } catch (RuntimeException e) {
            LOGGER.error("Update poll votes by on-chain info error.", e);
        }

    }

}
