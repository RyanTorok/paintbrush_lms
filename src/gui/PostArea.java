package gui;

import classes.ClassPd;
import classes.Post;
import classes.PostStatus;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.*;


public class PostArea extends VBox {

    private final boolean editable;
    private final PostWindow wrapper;
    private Post post;
    private final ControlIcon like;
    private final Text numLikes;
    private final Text instructorEndorsed;

    public PostArea(PostWindow wrapper, Post post, TextFlow text, boolean editable) {
        super(text);
        this.wrapper = wrapper;
        this.post = post;
        Styles.setBackgroundColor(this, Color.WHITE);
        Styles.setProperty(this, "-fx-background-radius", Double.toString(Size.lessWidthHeight(10)));
        setPadding(Size.insets(20, 20, 10, 20));
        setMinHeight(Size.height(150));

        instructorEndorsed = new Text("");
        instructorEndorsed.setFont(Font.font(Font.getDefault().getFamily(), FontPosture.ITALIC, Size.fontSize(14)));
        instructorEndorsed.setFill(Color.DARKGRAY);
        updateInstructorEndorsed(post);
        HBox controls = new HBox(new Layouts.Filler(), instructorEndorsed, new Layouts.Filler());
        controls.setSpacing(Size.width(15));
        controls.setAlignment(Pos.BOTTOM_LEFT);
        Text numViews = new Text(UtilAndConstants.parseLargeNumber(post.getViews()));
        ControlIcon views = new ControlIcon("\ud83d\udc41", numViews.getText() + " Views");
        numViews.setFont(Font.font(Size.fontSize(12)));
        numViews.setFill(Color.DARKGRAY);
        like = new ControlIcon("\ud83d\udc4d", "Like");
        numLikes = new Text(UtilAndConstants.parseLargeNumber(post.getLikes()));
        numLikes.setFont(Font.font(Size.fontSize(12)));
        numLikes.setFill(Color.DARKGRAY);
        ControlIcon answer = new ControlIcon('a', "Answer");
        answer.setFont(CustomFonts.comfortaa(30));
        controls.getChildren().add(new TextFlow(numViews, views));
        if (post.getType().equals(Post.Type.Question))
            controls.getChildren().add(answer);
        ControlIcon comment = new ControlIcon((char) 0x27a5, "Comment");
        ControlIcon copyLink = new ControlIcon("\ud83d\udd17", "Copy Link to this Post");
        copyLink.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString("@" + post.getIdentifier().getId());
            clipboard.setContent(content);
        });
        ControlIcon history = new ControlIcon("\ud83d\udd51", "History");
        controls.getChildren().addAll(new TextFlow(numLikes, like), comment);
        this.editable = editable;
        if (this.editable) {
            ControlIcon edit = new ControlIcon((char) 0x270e, "Edit");
            edit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> edit());
            ControlIcon delete = new ControlIcon((char) 0x00d7, "Delete");
            delete.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> delete());
            controls.getChildren().addAll(edit, copyLink, history, delete);
        } else {
            if (post.isCurrentUserLikedThis()) {
                like.setFill(Color.GREEN);
                Events.highlightOnMouseOver(like);
                like.setFill(Color.GREEN.brighter());
            }
            //only do this if the post is not editable (stop someone from liking their own post, e.g.)
            like.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (post.isCurrentUserLikedThis()) unlike();
                else like();
            });
            ControlIcon report = new ControlIcon((char) 0x2691, "Flag as Inappropriate");
            report.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> report());
            controls.getChildren().addAll(copyLink, history, report);
        }
        getChildren().addAll(new Layouts.Filler(), controls);
        setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(Size.lessWidthHeight(10)), new BorderWidths(Size.lessWidthHeight(2)))));
    }

    private void updateInstructorEndorsed(Post post) {
        if (post.getStatusLabels().contains(PostStatus.ENDORSED)) {
            Teacher teacher = ClassPd.fromId(post.getClassId()) != null ? ClassPd.fromId(post.getClassId()).getTeacher() : new Teacher(null, null, null, "Null", null, "", null, null, null);
            if (post.getType().equals(Post.Type.Question))
                instructorEndorsed.setText(teacher.getFirst() + " " + teacher.getLast() + " thinks this is a good question");
            else if (post.getType().equals(Post.Type.Note))
                instructorEndorsed.setText(teacher.getFirst() + " " + teacher.getLast() + " thinks this is a good note");
            else if (post.getType().equals(Post.Type.Student_Answer))
                instructorEndorsed.setText(teacher.getFirst() + " " + teacher.getLast() + " endorsed this answer");
            else
                instructorEndorsed.setText(teacher.getFirst() + " " + teacher.getLast() + " liked this");
        }
    }

    private void edit() {

    }

    private void delete() {

    }

    private void like() {
        like.setFill(Color.GREEN);
        Events.highlightOnMouseOver(like);
        like.setFill(Color.GREEN.brighter());
        post.like();
        numLikes.setText(UtilAndConstants.parseLargeNumber(post.getLikes()));
    }

    private void unlike() {
        like.setFill(Color.LIGHTGRAY);
        Events.highlightOnMouseOver(like);
        like.setFill(Color.LIGHTGRAY.darker());
        post.unlike();
        numLikes.setText(UtilAndConstants.parseLargeNumber(post.getLikes()));
        updateInstructorEndorsed(post);
    }


    private void report() {

    }

    class ControlIcon extends Text {
        public ControlIcon(char character, String hoverText) {
            this(Character.toString(character), hoverText);
        }

        public ControlIcon(String s, String hoverText) {
            super(s);
            setFont(Font.font(Size.fontSize(30)));
            setFill(Color.LIGHTGRAY);
            Events.highlightOnMouseOver(this);
            Tooltip tooltip = new Tooltip(hoverText);
            tooltip.setFont(Font.font(12));
            Tooltip.install(this, tooltip);
        }
    }
}