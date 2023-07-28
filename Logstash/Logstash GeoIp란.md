## `Logstash GeoIp란?`

Logstash에는 `GeoIp`란 maxmind 데이베이스를 이용해서 IP의 지리정보 매핑을 지원한다.

Access.log의 ip의 지리 정보를 판단할 때 사용한다.

`Nginx` -> `Filebeat` -> `Logstash` -> `ElasticSearch`  형태로 많이 사용한다.

```nginx configuration
input {
  beats {
      port => "5044" 
  }
}

filter {
   json {
       source => "message" // Filebeat 메세지를 받아온다.
   }

   geoip {
       source => "[json][ip]"  // 메세지의 ip 라는 필드를 꺼낸다.  
       target => "geoip"       // geoip 플러그인을 사용한다.
   }
}

output {
  # elastsic search로 전송
  elasticsearch {
    hosts => ["http://gyunny.com:10200"]
    user => "test"
    password => "test"
    ilm_enabled => false
    index => "accesslog.%{+yyyy.MM.dd}"
    timeout => 30
    template_name => logstash
    doc_as_upsert => true
    codec => "json"
  }

  stdout {
    codec => rubydebug {}
  }
}
```

위처럼 `Logstash.conf` 에서 파일비트를 Consume 하여 geoIp 플러그인을 사용하여 `ElasticSearch`로 데이터를 전송한다.

그러면 `Kibana` 에서 해당 `ip`의 국가 지리적 정보를 얻을 수 있다.

<br>

### `Referenece`

- [https://www.elastic.co/guide/en/logstash/current/plugins-filters-geoip.html](https://www.elastic.co/guide/en/logstash/current/plugins-filters-geoip.html)