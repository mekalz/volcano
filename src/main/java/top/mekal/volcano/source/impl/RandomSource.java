package top.mekal.volcano.source.impl;

import top.mekal.volcano.common.Field;
import top.mekal.volcano.source.Source;
import com.typesafe.config.Config;

import java.util.HashMap;
import java.util.Properties;

/**
 * Created by mekal on 08/01/2017.
 */
public class RandomSource extends Source {

    private String template;
    private HashMap<String, Object> mapping = new HashMap<>();

    public RandomSource(String name, Config conf) {
        super(name, conf);

        this.template = conf.getString("template");

        String mappingString = conf.getString("mapping");
        String mappingFields[] = mappingString.split(";");
        for (String mappingField: mappingFields) {
            String mappingSegments[] = mappingField.split(":");

            if (mappingSegments.length != 3) {
                System.err.println("Template mapping format error, should be [fieldName:fieldType:valueRange], won't make any replacement for template.");
            } else {
                this.mapping.put(mappingSegments[0], new Field(mappingSegments[1], mappingSegments[2]));
            }
        }
    }

    public RandomSource(Properties prop) {
        super(prop);
    }

    public String fetch() {
        String result = this.template;
        for (String fieldKey : this.mapping.keySet()) {
            Field field = (Field) (this.mapping.get(fieldKey));
            result = result.replace("{" + fieldKey + "}", field.random());
        }

        Long t = System.currentTimeMillis();
        result = result.replace("{__TIMESTAMP__}", t.toString());
        Double tf = t.doubleValue() / 1000D;
        result = result.replace("{__TIMESTAMP_FLOAT__}", String.format("%.3f", tf));
        return result;
    }
}
