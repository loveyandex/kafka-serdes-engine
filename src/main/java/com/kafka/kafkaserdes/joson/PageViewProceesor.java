package com.kafka.kafkaserdes.joson;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.TimeWindows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class PageViewProceesor {
      
 

      @Autowired
      public void buildPipeline(StreamsBuilder streamsBuilder) {

            System.out.println("in page view proccessor");

            final KStream<String, PageView> views = streamsBuilder.stream("streams-pageview-input",
                        Consumed.with(Serdes.String(), new JSONSerde<PageView>()));

            final KTable<String, UserProfile> users = streamsBuilder.table("streams-userprofile-input",
                        Consumed.with(Serdes.String(), new JSONSerde<>()));

            final Duration duration24Hours = Duration.ofHours(24);

            final KStream<WindowedPageViewByRegion, RegionCount> regionCount = views
                        .leftJoin(users, (view, profile) -> {
                              final PageViewByRegion viewByRegion = new PageViewByRegion();
                              viewByRegion.user = view.user;
                              viewByRegion.page = view.page;

                              if (profile != null) {
                                    viewByRegion.region = profile.region;
                              } else {
                                    viewByRegion.region = "UNKNOWN";
                              }
                              return viewByRegion;
                        })
                        .map((user, viewRegion) -> new KeyValue<>(viewRegion.region, viewRegion))
                        .groupByKey(Grouped.with(Serdes.String(), new JSONSerde<>()))
                        .windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofDays(7), duration24Hours)
                                    .advanceBy(Duration.ofSeconds(1)))
                        .count()
                        .toStream()
                        .map((key, value) -> {
                              final WindowedPageViewByRegion wViewByRegion = new WindowedPageViewByRegion();
                              wViewByRegion.windowStart = key.window().start();
                              wViewByRegion.region = key.key();

                              final RegionCount rCount = new RegionCount();
                              rCount.region = key.key();
                              rCount.count = value;

                              return new KeyValue<>(wViewByRegion, rCount);
                        });

            // write to the result topic
            regionCount.to("streams-pageviewstats-typed-output", Produced.with(new JSONSerde<>(), new JSONSerde<>()));

            // final KafkaStreams streams = new KafkaStreams(builder.build(), props);
            // final CountDownLatch latch = new CountDownLatch(1);

      }

}
