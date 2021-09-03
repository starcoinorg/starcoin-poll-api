package org.starcoin.poll.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.starcoin.poll.api.service.FeishuWebhookService;
import org.starcoin.poll.api.service.PollItemService;

@SpringBootTest
class StarcoinPollApiApplicationTests {

    @Autowired
    PollItemService pollItemService;

    @Autowired
    FeishuWebhookService feishuWebhookService;

    @Test
    void contextLoads() {
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
