package top.mekal.volcano;

import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import akka.actor.Props;
import top.mekal.volcano.sender.ISender;
import top.mekal.volcano.sender.impl.ConsoleSender;
import top.mekal.volcano.sender.impl.KafkaSender;
import top.mekal.volcano.source.Source;
import top.mekal.volcano.source.impl.NginxSource;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mekal on 27/12/2016.
 */
public class Runner {
    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.printf("=============================\nUsage: volcano <config/file/path>\n=============================\n");
            System.exit(1);
        }
        System.out.println("Runner started.");

        String confPath = args[0];
        System.out.println(confPath);

        File configFile = new File(confPath);
        Config genConf = ConfigFactory.parseFile(configFile);

        String tpl = genConf.getString("template");
        String mapping = genConf.getString("mapping");
        Long duration = genConf.getLong("duration") * 1000;
        Long rate = genConf.getLong("rate");

        String sinkType = genConf.getString("sink");
        final ISender sender;

        switch (sinkType) {
            case "kafka":
                String kafkaTopic = genConf.getString("kafkaTopic");
                String kafkaBrokers = genConf.getString("kafkaBrokers");
                String kafkaClientId = genConf.getString("kafkaClientId");

                System.out.printf("Template: %s\n", tpl);
                System.out.printf("Mapping: %s\n", mapping);
                System.out.printf("Duration: %s\n", duration);
                System.out.printf("Rate: %s\n", rate);
                System.out.printf("Kafka topic: %s\n", kafkaTopic);
                System.out.printf("Kafka client.id: %s\n", kafkaClientId);
                System.out.printf("Kafka brokers: %s\n", kafkaBrokers);
                sender = new KafkaSender(kafkaTopic, kafkaBrokers, kafkaClientId);
                break;
            default:
                System.out.println("Sink type is unknown or not defined. \nSend data to console by default.");
                sender = new ConsoleSender();

        }


        final Source source = new NginxSource("nginx.hawkeye", genConf);
        final ActorSystem actorSystem = ActorSystem.create("volcano");

        final Integer genNums = (int)(Math.ceil(rate.doubleValue() / 1000));
        Integer genRate = (int)(Math.ceil(rate.doubleValue() / genNums));

        final List<ActorRef> generatorList = new ArrayList<>();
        for (Integer i = 0; i < genNums; i++) {
            ActorRef g = actorSystem.actorOf(Props.create(Generator.class, i, source, sender, genRate));
            generatorList.add(g);
            g.tell("start", ActorRef.noSender());
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Integer i = 0; i < genNums; i++) {
                    generatorList.get(i).tell("stop", ActorRef.noSender());
                }

                try {
                    Thread.sleep(5000);
                } catch(Exception e) {
                   System.err.println(e);
                }
                sender.close();
                System.exit(0);
            }
        }, duration);
    }
}
