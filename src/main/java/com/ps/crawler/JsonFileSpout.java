package com.ps.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import com.ps.crawler.DataStorage.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonFileSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private List<String> urls;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;

        // Read the JSON file and store URLs in a list of strings
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.urls = mapper.readValue(getClass().getResourceAsStream("D:\\Mvn\\New folder\\stormcrawler\\stormcrawler\\seed_urls.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (IOException e) {
            throw new RuntimeException("Error reading seed URLs from JSON file", e);
        }
    }

    @Override
    public void nextTuple() {
        // Emit each URL as a tuple
        for (String url : urls) {
            collector.emit(new Values(url));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("url"));
    }

}
