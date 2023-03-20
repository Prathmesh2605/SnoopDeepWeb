package com.ps.crawler;

import com.ps.crawler.JsonFileSpout.*;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.ps.crawler.DataStorage.*;

import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;

public class SeleniumBolt extends BaseRichBolt {
    private OutputCollector collector;
    private WebDriver driver;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;

        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        driver = new ChromeDriver();
    }

    @Override
    public void execute(Tuple input) {
        String url = input.getStringByField("url");

        driver.get(url);

        String html = driver.getPageSource();

        byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);

        String base64Image = Base64.getEncoder().encodeToString(screenshot);
        collector.emit(new Values(url, html, screenshot));
        try {
            DataStorage.storeData(url,url,html,base64Image);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            DataStorage.writeDataToFile(url,url,html,base64Image);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("url", "html", "screenshot"));
    }

    @Override
    public void cleanup() {

        driver.quit();
    }
}
