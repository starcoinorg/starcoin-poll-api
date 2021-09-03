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
                "0xb4491fd2d00db301b8ec6ddb6d5ae747",
                "4"
            ]
        }
    ]
}'
*/


insert INTO `poll`.`poll_item`
(
id,
`creator`,
`for_votes`,
`against_votes`,
`end_time`,
`link`,
`network`,
`type_args_1`,
`title`,
`title_en`,
`description`,
`description_en`,
`status`,
`created_at`,
`updated_at`,
`deleted_at`,
`id_on_chain`)
VALUES
(
3,
'0xb4491fd2d00db301b8ec6ddb6d5ae747',
0,
0,
unix_timestamp(now()) * 1000 + 1000 * 60 * 60 * 24 * 7,
'https://github.com/starcoinorg/starcoin/discussions/2806',
'main',
'0x1::UpgradeModuleDaoProposal::UpgradeModuleV2',
'UNTitiled',
'UNTitiled',
'UNDesc',
'UNDesc',
0,
unix_timestamp(now()) * 1000,
unix_timestamp(now()) * 1000,
null,
'4'
);
