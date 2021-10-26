package org.starcoin.poll.api.taskservice;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.starcoin.poll.api.bean.PollItem;
import org.starcoin.poll.api.dao.PollItemRepository;
import org.starcoin.poll.api.service.ContractService;
import org.starcoin.poll.api.service.MailService;
import org.starcoin.poll.api.service.WebhookService;
import org.starcoin.poll.api.system.MailConfiguration;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Conditional(MailConfiguration.MailSenderCondition.class)
@Component
public class CheckPollStateTaskService {
    private static final String MAINNET = "main";
    private static final int ALERT_HOURS_BEFORE_DEADLINE = 12; // 提前 12 个小时告警

    private static final Logger LOG = LoggerFactory.getLogger(CheckPollStateTaskService.class);

    @Value("${alert.mail.to}")
    private String alertMailTo;

    @Value("${alert.mail.subject-prefix}")
    private String mailSubjectPrefix;

    @Autowired
    private PollItemRepository pollItemRepository;

    @Autowired
    private ContractService contractService;

    @Autowired
    private MailService mailService;

    @Autowired
    private WebhookService webhookService;

    @Scheduled(fixedDelay = 1000 * 60 * 60) // 每小时检查一次？
    @SuppressWarnings("unchecked")
    public void task() {
        Page<PollItem> pollItems = pollItemRepository.findByNetworkAndDeletedAtIsNullOrderByIdDesc(MAINNET, PageRequest.of(0, 5));
        pollItems.forEach(pollItem -> {
            JSONObject responseObj = contractService.getProposalResourceResponseObject(pollItem.getCreator(), pollItem.getTypeArgs1());
            if (!ContractService.indicatesSuccess(responseObj)) {
                return;
            }
            Map<String, Object> resultMap = (Map<String, Object>) responseObj.get("result");
            if (LOG.isDebugEnabled()) {
                LOG.debug("get Proposal Resource: " + resultMap);
            }
            BigInteger yesVotes = new BigInteger((String) getValueInResultMap(resultMap, "for_votes", "U128"));
            BigInteger quorumVotes = new BigInteger((String) getValueInResultMap(resultMap, "quorum_votes", "U128"));
            Long endTimeMills = Long.parseLong((String) getValueInResultMap(resultMap, "end_time", "U64"));
            if (isPollAboutToEnd(endTimeMills) && yesVotes.compareTo(quorumVotes) < 0) {
                LOG.info("Poll is about to end! Id(onChain): " + pollItem.getIdOnChain());
                String subject = mailSubjectPrefix + "Poll is about to end! #" + pollItem.getIdOnChain();
                String content = "Yes votes: " + yesVotes + ". It did NOT reach quorum votes: " + quorumVotes;
                mailService.sendMail(subject, content, Arrays.asList(alertMailTo.split(",")));
                webhookService.post(subject, content);
            }
        });
    }

    private boolean isPollAboutToEnd(Long pollEndTimeMills) {
        return System.currentTimeMillis() < pollEndTimeMills &&
                System.currentTimeMillis() + ALERT_HOURS_BEFORE_DEADLINE * 60L * 60 * 1000 > pollEndTimeMills;
    }

    @SuppressWarnings("unchecked")
    private Object getValueInResultMap(Map<String, Object> resultMap, String valueName, String valueType) {
        List<Object> valueItem = (List<Object>) ((List<Object>) resultMap.get("value")).stream()
                .filter(i -> i instanceof List && valueName.equals(((List<Object>) i).get(0))).findFirst().orElse(null);
        if (valueItem == null) {
            throw new RuntimeException("CANNOT get value '" + valueName + "' from map: " + resultMap);
        }
        Map<String, Object> viMap = (Map<String, Object>) valueItem.get(1);
        return viMap.get(valueType);
    }
}
