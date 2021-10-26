package org.starcoin.poll.api;

import com.alibaba.fastjson.JSONObject;
import com.novi.serde.DeserializationError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.starcoin.poll.api.service.PollItemService;
import org.starcoin.poll.api.service.TransactionService;
import org.starcoin.poll.api.service.WebhookService;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class StarcoinPollApiApplicationTests {

    @Autowired
    PollItemService pollItemService;

    @Autowired
    WebhookService webhookService;

    @Autowired
    TransactionService transactionService;

    @Test
    void contextLoads() {
//        webhookService.post("Poll is about to end! Id(onChain): 7", "vote!");
//        if (true) return;
        try {
            String indexPrefix = "main"; // "main.0727"
            List<JSONObject> jsonObjectList = transactionService.getEventsByProposalIdAndProposer(indexPrefix, 0L, "0xb2aa52f94db4516c5beecef363af850a");
            System.out.println(jsonObjectList);
            System.out.println("result size: " + jsonObjectList.size());
            System.out.println(jsonObjectList.stream().map(j -> j.getJSONObject("voteChangedEvent").getBigDecimal("vote")).reduce((j1, j2) -> j1.add(j2)));
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (DeserializationError deserializationError) {
            deserializationError.printStackTrace();
        }
//        String idOnChain = "1";
//        pollItemService.add(0L, "tester", "test", "test", System.currentTimeMillis(),
//                0L, "http://test.com/test/poll/1", "test", "test", "0x11::Test:Test",
//                1, "dev", idOnChain);
//        feishuWebhookService.post("Test subject", "This is a test message.");
    }
/*
curl --location --request POST 'https://main-seed.starcoin.org' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": 101,
    "jsonrpc": "2.0",
    "method": "contract.call",
    "params": [
        {
            "function_id": "0x1::Dao::proposal_state",
            "type_args": [
                "0x1::STC::STC",
                "0x1::UpgradeModuleDaoProposal::UpgradeModuleV2"
            ],
            "args": [
                "0xb2aa52f94db4516c5beecef363af850a",
                "0"
            ]
        }
    ]
}'

curl --location --request POST 'https://main-seed.starcoin.org' \
--header 'Content-Type: application/json' \
--data-raw '{
 "id":101,
 "jsonrpc":"2.0",
 "method":"contract.get_resource",
 "params":["0xb2aa52f94db4516c5beecef363af850a", "0x1::Dao::Proposal<0x1::STC::STC,0x1::UpgradeModuleDaoProposal::UpgradeModuleV2>"]
}'

 */

}
