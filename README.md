# Starcoin Poll API

## Docker Run

```shell
docker build .
```

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

```shell
docker run -p 8686:8686 -e POLL_API_PORT=8686 -e STARCOIN_JSON_RPC_URL="https://barnard-seed.starcoin.org" -e STARCOIN_MYSQL_URL="jdbc:mysql://host.docker.internal:3306/poll" -e spring_profiles_active="dev" -e STARCOIN_MYSQL_USER="root" -e STARCOIN_MYSQL_PWD="123456" -e STARCOIN_ES_URL="search-starcoin-es2-47avtmhexhbg7qtynzebcnnu64.ap-northeast-1.es.amazonaws.com" -e STARCOIN_ES_PROTOCOL="https" -e STARCOIN_ES_PORT=443 -e DISCORD_WEBHOOK_URL="http://test.org/" -e ALERT_MAIL_TO="test@test.org" -e MAIL_SMTP_HOST="smtp.test.org" -e MAIL_SENDER_USERNAME="test" -e SPRING_SECURITY_HTPASSWD="admin:$apr1$MUvDk/sU$vyG86aHR7xMZTR2avia.//" <IMAGE_ID>
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

API doc:

```text
http://localhost:8600/poll-api-doc/swagger-ui/index.html
```

## License

Starcoin Poll API is licensed as Apache 2.0.