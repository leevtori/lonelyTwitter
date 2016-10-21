package ca.ualberta.cs.lonelytwitter;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by vlee2 on 10/20/16.
 */
public class ElasticsearchTweetController {

    private static JestDroidClient client;

    // TODO we need a function which adds tweets to elastic search
    public static class AddTweetsTask extends AsyncTask<NormalTweet, Void, Void> {

        @Override
        protected Void doInBackground(NormalTweet... tweets) {
            for (NormalTweet tweet : tweets) {

                Index index = new Index.Builder(tweets[0]).index("testing").type("tweet").build();

                try {
                    // where is the client
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()){
                        tweet.setId(result.getId());
                    }
                    else {
                        Log.i("Error","Faile to insert the tweet into elastic search");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The applocation faield to build and send tweets");
                }
            }
            return null;
        }
    }
    // TODO we need a functio whcih gest tweeet from elastic search

    public static class GetTweetsTask extends AsyncTask<String, Void, ArrayList<NormalTweet>>{

        @Override
        protected ArrayList<NormalTweet> doInBackground(String...search_parameters){
            verifySettings();
            ArrayList<NormalTweet> tweets = new ArrayList<NormalTweet>();

            // Assumption: Only the first search_parameters[0] is used.
            String search_string = "{\"from\": 0, \"size\":10000}";

            Search search = new Search.Builder(search_string)
                    .addIndex("testing")
                    .addType("tweet")
                    .build();
            try{
                SearchResult result = client.execute(search);
                if (result.isSucceeded()){
                    List<NormalTweet> foundTweets = result.getSourceAsObjectList(NormalTweet.class);
                    tweets.addAll(foundTweets);
                }
                else{
                    Log.i("Error", "The search executed but it didn't work");
                }

            }
            catch (Exception e){
                Log.i("Eror", "Executing the get tweets method failed");
            }

            return tweets;
        }
    }

    // client configuration
    public static void verifySettings() {
        if (client == null){
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080/");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
