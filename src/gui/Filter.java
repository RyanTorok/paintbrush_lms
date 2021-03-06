package gui;

import classes.Post;
import classes.PostStatus;
import classes.TimeStatus;
import searchengine.Identifier;

import java.util.Date;
import java.util.UUID;

public class Filter {

    private TimeStatus timeStatus;
    private Date comparisonTime;
    private PostStatus postStatus;
    private Post.Type postType;
    String name;
    private UUID userId;
    private UUID classItemId;

    Filter(String name, UUID userId, UUID classItemId) {
        this.name = name;
        this.userId = userId;
        this.classItemId = classItemId;
    }

    Filter(String name, PostStatus status) {
        this.name = name;
        this.postStatus = status;
    }

    Filter(String name, TimeStatus status, long comparisonTime) {
        this.name = name;
        this.timeStatus = status;
        this.comparisonTime = new Date(comparisonTime);
    }

    Filter(String name, TimeStatus status) {
        this(name, status, -1);
    }

    public Filter(String name, Post.Type type) {
       this.name = name;
       this.postType = type;
    }

    boolean matches(Post post) {
        if (postStatus != null && !post.getStatusLabels().contains(postStatus)) return false;
        if (postType != null && post.getType() != postType) return false;
        if (timeStatus != null && !timeMatches(post)) return false;
        return classItemId == null || classItemId.equals(new UUID(0, -1)) || post.getClassItemId().equals(classItemId);
    }

    private boolean timeMatches(Post post) {
        Identifier id = post.getIdentifier();
        long ms = 86400000;
        switch (timeStatus) {
            case TODAY: {
                long now = System.currentTimeMillis();
                long today = now - now % ms;
                long diff1 = id.getTime1() - today;
                long diff2 = id.getTime2() - today;
                return (diff1 > 0 && diff1 < ms) || (diff2 > 0 && diff2 < ms);
            }
            case THIS_WEEK: {
                Date idDate = new Date(id.getTime1() - id.getTime1() % ms);
                long now = System.currentTimeMillis();
                Date lastWeek = new Date(Math.max(0, now - (now % ms + ms * 7)));
                return idDate.after(lastWeek) || new Date(id.getTime2() - id.getTime2() % ms).after(lastWeek);
            }
            case ON:
                long diff1 = id.getTime1() - comparisonTime.getTime();
                long diff2 = id.getTime2() - comparisonTime.getTime();
                return (diff1 > 0 && diff1 < ms) || (diff2 > 0 && diff2 < ms);
            case AFTER:
                return new Date(id.getTime1()).after(comparisonTime) || new Date(id.getTime2()).after(comparisonTime);
            case BEFORE:
                return (new Date(id.getTime1()).before(comparisonTime) && (id.getTime1() != 0)) || (new Date(id.getTime2()).before(comparisonTime) && (id.getTime2() != 0));
            default: return false;
        }
    }

    public Date getComparisonTime() {
        return comparisonTime;
    }

    public void setComparisonTime(Date comparisonTime) {
        this.comparisonTime = comparisonTime;
    }
}
