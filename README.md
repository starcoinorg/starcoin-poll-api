# Starcoin Poll API

## Docker Run

Docker build:

```shell
docker build .
```

List docker images:

```shell
docker images
```

Create user and password for API:

```shell
% htpasswd -c auth admin
New password: <ENTER_PASSWORD_HERE>
Re-type new password: <ENTER_PASSWORD_AGAIN>
Adding password for user admin

% cat auth 
admin:$apr1$MUvDk/sU$vyG86aHR7xMZTR2avia.//
```

In above, enter 'admin' as password.

Docker run:

```shell
docker run -p 8686:8686 -e POLL_API_PORT=8686 -e STARCOIN_JSON_RPC_URL="https://barnard-seed.starcoin.org" -e STARCOIN_MYSQL_URL="jdbc:mysql://host.docker.internal:3306/poll" -e spring_profiles_active="dev" -e STARCOIN_MYSQL_USER="root" -e STARCOIN_MYSQL_PWD="123456" -e STARCOIN_ES_URL="search-starcoin-es2-47avtmhexhbg7qtynzebcnnu64.ap-northeast-1.es.amazonaws.com" -e STARCOIN_ES_PROTOCOL="https" -e STARCOIN_ES_PORT=443 -e DISCORD_WEBHOOK_URL="http://test.org/" -e ALERT_MAIL_TO="test@test.org" -e MAIL_SMTP_HOST="smtp.test.org" -e MAIL_SENDER_USERNAME="test" -e SPRING_SECURITY_HTPASSWD='admin:$apr1$MUvDk/sU$vyG86aHR7xMZTR2avia.//' <IMAGE_ID>
```

HTTP GET test:

```text
http://localhost:8686/v1/polls/detail/1
```

Debug CORS requests using cURL:

```shell
% curl -H "Origin: http://example.com" \                                                                                 
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: X-Requested-With" \
  -X OPTIONS --verbose \
http://localhost:8600/v1/polls/add
```

Test update a poll:

```shell
% curl --verbose -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Accept: */*" \
  -H "Authorization: Basic YWRtaW46YWRtaW4=" \
  -d "title=%E6%B5%8B%E8%AF%95%E6%8F%90%E6%A1%882&titleEn=testProposal28&descriptionEn=proposal+description+2&description=%E6%B5%8B%E8%AF%95%E6%8F%90%E6%A1%88%E6%8F%8F%E8%BF%B02%E4%B8%AD%E6%96%87&creator=0x4234234234&network=main&status=1&link=https%3A%3A%2F%2Fgithub.com%2Fdiscussions%2F343&typeArgs1=0x1%3A%3AToken%3A%3AToken&idOnChain=8&databaseID=47&forVotes=1000000&againstVotes=1000&endTime=1688988888&id=47" \
http://localhost:8600/v1/polls/modif
```

API doc:

```text
http://localhost:8600/poll-api-doc/swagger-ui/index.html
```

## License

Starcoin Poll API is licensed as Apache 2.0.