package top.mekal.volcano;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import top.mekal.volcano.sender.ISender;
import top.mekal.volcano.source.Source;

/**
 * Created by mekal on 27/12/2016.
 */
public class Generator extends UntypedActor {

    private Integer recordsPerSecond;
    private ISender sender;
    private Integer id;

    private Source<String> source;

    private Integer status = 0;
    private Long interval = 1000L;

    public Generator(Integer genId, Source source, ISender sender, Integer recordsPerSecond) {
        this.id = genId;
        this.source = source;
        this.sender = sender;
        this.recordsPerSecond = recordsPerSecond;
    }

    private void gen() {
        Long startTime = System.currentTimeMillis();

        for (Integer i = 0; i < this.recordsPerSecond; i++) {
            this.sender.send(source.fetch());
        }

        Long endTime = System.currentTimeMillis();
        Long duration = endTime - startTime;
        Long sleepTime = 0L;

        if (duration < interval) {
            sleepTime = interval - duration;
        }

        try {
            Thread.sleep(sleepTime);
        } catch (Exception e) {
            System.err.println(e);
        }

        if (this.status == 1) {
            self().tell("next", ActorRef.noSender());
        }
    }

    private void start() {
        this.status = 1;
        System.out.printf("Generator %d started.\n", this.id);
        gen();
    }

    private void stop() {
        this.status = 0;
        System.out.printf("Generator %d stopped.\n", this.id);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String) {
            switch (message.toString()) {
                case "start":
                    start();
                    break;
                case "next":
                    gen();
                    break;
                case "stop":
                    stop();
                    break;
            }
        }
    }
}
