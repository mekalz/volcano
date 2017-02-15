package top.mekal.volcano.common;

import java.util.Random;

/**
 * Created by mekal on 27/12/2016.
 */
public class Field {
    public String type;
    private String values[];
    private Number from;
    private Number to;

    private Random rand = new Random();

    public Field(String typeDef, String rangeDef) {
        this.type = typeDef;
        switch (typeDef) {
            case "int":
                String rawRangesInt[] = rangeDef.split(",");
                this.from = Integer.parseInt(rawRangesInt[0]);
                this.to = Integer.parseInt(rawRangesInt[1]);
                break;
            case "float":
                String rawRangesFloat[] = rangeDef.split(",");
                this.from = Float.parseFloat(rawRangesFloat[0]);
                this.to = Float.parseFloat(rawRangesFloat[1]);
                break;
            default:
                this.values = rangeDef.split(",");
        }
    }

    public String random() {
        String res = "";
        switch (this.type) {
            case "int":
                Integer intValue = rand.nextInt(this.to.intValue() - this.from.intValue() + 1) + this.from.intValue();
                res = intValue.toString();
                break;
            case "float":
                Float floatValue = rand.nextFloat() * (this.to.floatValue() - this.from.floatValue()) + this.from.floatValue();
                res = floatValue.toString();
                break;
            case "string":
                Integer valueLength = this.values.length;
                res = this.values[rand.nextInt(valueLength)];
                break;
            default:
                res = "unknow";
        }
        return res;
    }
}
