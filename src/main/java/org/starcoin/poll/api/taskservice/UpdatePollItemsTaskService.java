package org.starcoin.poll.api.taskservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.starcoin.poll.api.bean.PollItem;
import org.starcoin.poll.api.dao.PollItemRepository;
import org.starcoin.poll.api.service.ServiceFacade;

@Component
public class UpdatePollItemsTaskService {
    private static final String MAINNET = "main";

    @Value("${starcoin.network}")
    private String starcoinNetwork;

    @Autowired
    private PollItemRepository pollItemRepository;

    @Autowired
    private ServiceFacade serviceFacade;

    @Scheduled(fixedDelay = 1000 * 60 * 2) // 两分钟更新一次？
    @SuppressWarnings("unchecked")
    public void task() {
        Page<PollItem> pollItems = pollItemRepository.findByNetworkAndDeletedAtIsNullOrderByIdDesc(getStarcoinNetwork(), PageRequest.of(0, 20));
        pollItems.forEach(pollItem -> {
            serviceFacade.updateByOnChainInfo(pollItem);
        });
    }

    public String getStarcoinNetwork() {
        return starcoinNetwork != null && !starcoinNetwork.isEmpty() ? starcoinNetwork : MAINNET;
    }
}
