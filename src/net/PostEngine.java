package net;

import classes.ClassPd;
import classes.Post;
import main.Root;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static net.Net.root;
import static net.Net.urlEncode;

public class PostEngine implements Serializable {

    static final long serialVersionUID = 98L;

    private long lastUpdate;
    private ClassPd belongsTo;
    private ArrayList<Post> posts;

    public void update() {
        try {
            //post data
            String data = "id=" + Root.getActiveUser().getID() + "&username="+ Root.getActiveUser().getUsername() + "&password=" + urlEncode(Root.getActiveUser().getPassword()) + "&classId=" + belongsTo.getUniqueId();
            byte[] postData = data.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            //start connection
            URL url = new URL(root(), "post/getPosts.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }
            BufferedReader readDone = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String encoding = readDone.readLine();
            ArrayList<Post> newList = new ArrayList<>();
            while (encoding != null) {
                newList.add(Post.fromEncoding(encoding));
                encoding = readDone.readLine();
            }
            posts = newList;
        } catch (IOException e) {
        }
    }

    public void index() {
    }

    private List<Post> getPosts() {
        return posts;
    }

    public ClassPd getBelongsTo() {
        return belongsTo;
    }
}