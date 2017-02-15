package top.mekal.volcano.sender;

/**
 * Created by mekal on 28/12/2016.
 */
public interface ISender {
    public void send(String message);
    public void close();
}
