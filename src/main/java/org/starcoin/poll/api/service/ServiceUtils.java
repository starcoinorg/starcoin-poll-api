package org.starcoin.poll.api.service;

public class ServiceUtils {

    public static final String depositEvent = "0x00000000000000000000000000000001::Account::DepositEvent";
    public static final String withdrawEvent = "0x00000000000000000000000000000001::Account::WithdrawEvent";

    public static String getIndex(String indexPrefix, String indexName) {
        return indexPrefix + "." + indexName;
    }

    public static final int ELASTICSEARCH_MAX_HITS = 10000;
}
