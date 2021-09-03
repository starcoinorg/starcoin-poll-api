package org.starcoin.poll.api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class ContractService {
    // Proposal state
    public static final int PROPOSAL_STATE_PENDING = 1;
    public static final int PROPOSAL_STATE_ACTIVE= 2;
    public static final int PROPOSAL_STATE_DEFEATED = 3;
    public static final int PROPOSAL_STATE_AGREED= 4;
    public static final int PROPOSAL_STATE_QUEUED = 5;
    public static final int PROPOSAL_STATE_EXECUTABLE= 6;
    public static final int PROPOSAL_STATE_EXTRACTED= 7;

    private static final Logger logger = LoggerFactory.getLogger(ContractService.class);

    private static final String MAINNET_JSON_RPC_URL = "https://main-seed.starcoin.org";

    private final RestTemplate restTemplate;

    @Autowired
    public ContractService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int getPollStatus(String idOnChain, String creator, String typeArgs1) {
        JSONObject paramObject = new JSONObject();
        paramObject.put("id", System.currentTimeMillis() + new Random().nextInt(100));
        paramObject.put("jsonrpc", "2.0");
        paramObject.put("method", "contract.call");
        JSONObject paramItem = new JSONObject();
        paramItem.put("function_id", "0x1::Dao::proposal_state");
        JSONArray typeArgs = new JSONArray();
        typeArgs.add("0x1::STC::STC");
        typeArgs.add(typeArgs1);
        paramItem.put("type_args", typeArgs);
        JSONArray args = new JSONArray();
        args.add(creator);
        args.add(idOnChain);
        paramItem.put("args", args);
        JSONArray paramList = new JSONArray();
        paramList.add(paramItem);
        paramObject.put("params", paramList);
        JSONObject result = post(paramObject);
        if (result.containsKey("error") && !(result.containsKey("result") && null != result.getJSONObject("result"))) {
            return PROPOSAL_STATE_PENDING; //???
        }
        return result.getJSONArray("result").getJSONObject(0).getIntValue("U8");
    }

    public JSONObject getPollVotes(String creator, String typeArgs1) {
        JSONObject responseObj = getProposalResourceResponseObject(creator, typeArgs1);
        JSONObject votesObject = new JSONObject();
        if (indicatesSuccess(responseObj)) {
            JSONArray valueArray = responseObj.getJSONObject("result").getJSONArray("value");
            for (int i = 0; i < valueArray.size(); i++) {
                JSONArray item = valueArray.getJSONArray(i);
                if (null == item || !(item.contains("for_votes") || item.contains("against_votes"))) {
                    continue;
                }
                if (item.contains("for_votes")) {
                    votesObject.put("for_votes", item.getJSONObject(1).getString("U128"));
                }
                if (item.contains("against_votes")) {
                    votesObject.put("against_votes", item.getJSONObject(1).getString("U128"));
                }
            }
        }
        return votesObject;
    }

    public JSONObject getProposalResourceResponseObject(String creator, String typeArgs1) {
        JSONObject paramObject = new JSONObject();
        paramObject.put("id", System.currentTimeMillis() + new Random().nextInt(100));
        paramObject.put("jsonrpc", "2.0");
        paramObject.put("method", "contract.get_resource");
        JSONArray paramList = new JSONArray();
        paramList.add(creator);
        paramList.add("0x1::Dao::Proposal<0x1::STC::STC," + typeArgs1 + ">");
        paramObject.put("params", paramList);
        return post(paramObject);
    }

    public static boolean indicatesSuccess(JSONObject responseObj) {
        return !responseObj.containsKey("error")
                && (responseObj.containsKey("result") && null != responseObj.getJSONObject("result"));
    }

    private JSONObject post(JSONObject params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        logger.info("post_request：{} , params：{}", MAINNET_JSON_RPC_URL, JSONObject.toJSONString(params));
        JSONObject responseObj = restTemplate.postForObject(MAINNET_JSON_RPC_URL, new HttpEntity<>(params, headers), JSONObject.class);
        logger.info("post_response：" + JSONObject.toJSONString(responseObj));
        return responseObj;
    }
}
