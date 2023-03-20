package com.ps.crawler;

import com.digitalpebble.stormcrawler.spout.FileSpout;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Map;
import com.ps.crawler.*;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class CrawlTopology {
    public static void main(String[] args) {
        // Create a new topology builder
        TopologyBuilder builder = new TopologyBuilder();

        // Add the spout to the topology
        builder.setSpout("url-reader-spout", new JsonFileSpout(), 1);

        // Add the bolt to the topology
        builder.setBolt("selenium-bolt", new SeleniumBolt(), 1)
                .shuffleGrouping("url-reader-spout");

        // Define the output fields of the bolt
        Fields outputFields = new Fields("seed_url", "current_url", "html", "screenshot");

        // Set the configuration options for the topology
        Config conf = new Config();
        conf.setDebug(true);

        // Create a new local cluster and submit the topology
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("crawler-topology", conf, builder.createTopology());

        // Wait for the topology to finish
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shut down the local cluster
        cluster.killTopology("crawler-topology");
        cluster.shutdown();
    }
}
