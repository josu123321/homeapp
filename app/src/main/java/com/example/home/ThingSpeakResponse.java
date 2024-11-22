import java.util.List;

public class ThingSpeakResponse {
    public Channel channel;
    public List<Feed> feeds;

    public static class Channel {
        public int id;
        public String name;
    }

    public static class Feed {
        public String created_at; // Timestamp of the reading
        public String field1;     // Temperature
        public String field2;     // Humidity
    }
}
