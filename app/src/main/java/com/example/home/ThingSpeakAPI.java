import retrofit2.Call;
import retrofit2.http.GET;

public interface ThingSpeakAPI {
    // Fetch the latest feeds
    @GET("channels/2388701/feeds.json?results=1")
    Call<ThingSpeakResponse> getLatestFeeds();
}
