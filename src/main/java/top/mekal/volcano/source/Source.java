package top.mekal.volcano.source;

import com.typesafe.config.Config;

import java.util.Properties;

/**
 * Created by mekal on 08/01/2017.
 */
public abstract class Source<T> {

    public Source(String name, Config conf) {}
    public Source(Properties prop) {}

    public abstract T fetch();
}
