This has been generated by the StormCrawler Maven Archetype as a starting point for building your own crawler.
Have a look at the code and resources and modify them to your heart's content. 

# Prerequisites

You need to install Apache Storm. The instructions on [setting up a Storm cluster](https://storm.apache.org/releases/2.4.0/Setting-up-a-Storm-cluster.html) should help. Alternatively, 
the [stormcrawler-docker](https://github.com/DigitalPebble/stormcrawler-docker) project contains resources for running Apache Storm on Docker. 

You also need to have an instance of URLFrontier running. See [the URLFrontier README](https://github.com/crawler-commons/url-frontier/tree/master/service); the easiest way is to use Docker, like so:

```
docker pull crawlercommons/url-frontier
docker run --rm --name frontier -p 7071:7071  crawlercommons/url-frontier
```

# Compilation

Generate an uberjar with

``` sh
mvn clean package
```

# URL injection

The next step is to inject URLs into URLFrontier, using the [client](https://github.com/crawler-commons/url-frontier/tree/master/client). Fortunately, it is added as a dependency to this project so all
you need to do is

``` sh
java -cp  target/stormcrawler-1.0.jar crawlercommons.urlfrontier.client.Client PutURLs -f seeds.txt
```

where _seeds.txt_ is a file containing URLs to inject, with one URL per line.

# Running the crawl

You can now submit the topology using the storm command:

``` sh
storm local target/stormcrawler-1.0.jar --local-ttl 60 com.ps.crawler.CrawlTopology -- -conf crawler-conf.yaml
```

This will run the topology in local mode for 60 seconds. Simply use the 'storm jar' to start the topology in distributed mode, where it will run indefinitely.

You can also use Flux to do the same:

``` sh
storm local target/stormcrawler-1.0.jar  org.apache.storm.flux.Flux crawler.flux --local-ttl 3600
```

Note that in local mode, Flux uses a default TTL for the topology of 20 secs. The command above runs the topology for 1 hour.

It is best to run the topology with `storm jar` to benefit from the Storm UI and logging. In that case, the topology runs continuously, as intended.


Inorder to run the application
1. Start the zookeeper server using zkServer.cmd command in the terminal.
2. Start the storm nimbus server using storm nimbus command followed by storm supervisor.
3. In order to create the UI for storm topologies run storm ui command.
4. After starting the storm UI in another terminal run the following command to execute your topology and submit it to the storm cluster
  storm jar path/to/target/storm-example-1.0-jar-with-dependencies.jar com.ps.crawler.CrawlTopology 
5. You can view your topolgies in the storm cluster UI on the localhost:8080.

Reference/Documentations used are
1.https://stormcrawler.net/getting-started/
2.https://javadoc.io/doc/com.digitalpebble.stormcrawler/storm-crawler-core/latest/index.html
3.https://storm.apache.org/releases/2.4.0/index.html
4.http://admicloud.github.io/www/storm.html#:~:text=WordCount%20example,in%20the%20examples%20source%20code.&text=In%20this%20example%20there%20are%20three%20processing%20units%20arranged%20in%20the%20graph.
 
 
 From this project I got to learn about apache storm and storm crawler. Got to learn how apache storm is used for real time processing of distributed system and how stormcrawler can be use to scrape/crawl through the websites.
