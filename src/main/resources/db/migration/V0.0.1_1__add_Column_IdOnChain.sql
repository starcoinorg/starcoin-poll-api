 alter table poll_item add column id_on_chain varchar(100);
 update poll_item set id_on_chain = id where id_on_chain is null and id <> 88888888;

 alter table poll_item modify id_on_chain varchar(100) not null;
 alter table poll_item add constraint uniq_network_id_on_chain unique key (network, id_on_chain);

