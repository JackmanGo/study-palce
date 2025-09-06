package place;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import im.client.sdk.netty.ClientByWSNetty;
import im.client.sdk.utils.JWTUtil;
import io.netty.channel.Channel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication(scanBasePackages = {"place"})
public class  SysnImHistory {
    public static void main(String[] args){
        SpringApplication.run(SysnImHistory.class, args);
    }
}
