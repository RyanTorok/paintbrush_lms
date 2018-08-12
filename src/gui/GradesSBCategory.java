package gui;

import classes.ClassPd;
import classes.GradeCategory;
import classes.StudentGrades;
import javafx.animation.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Root;
import main.User;
import main.UtilAndConstants;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class GradesSBCategory extends VBox {

    private final ClassPd pd;
    private Text categoryTitle;
    private Text dropArrow;
    private ArrayList<HBox> entries;
    private StudentGrades allGrades;
    private boolean expanded;
    private GradesBody counterpart;

    public GradesSBCategory(ClassPd pd, GradeCategory cat, StudentGrades allGrades, GradesBody counterpart) {
        this.pd = pd;
        this.counterpart = counterpart;
        categoryTitle = new Text(cat.getName());
        // downward facing triangle
        dropArrow = new Text(Character.toString((char) 0x2bc6)) {
            {
                Color orig = UtilAndConstants.textFill(pd.getColor());
                setFill(orig);
                addEventHandler(MouseEvent.MOUSE_ENTERED, event -> setFill(orig.brighter()));
                addEventHandler(MouseEvent.MOUSE_EXITED, event -> setFill(orig));
            }
        };
        this.allGrades = allGrades;
        HBox header = new HBox(categoryTitle, new UtilAndConstants.Filler(), dropArrow);
        this.getChildren().add(header);
        entries = new ArrayList<>();
        entries.addAll(allGrades.getGrades().entrySet().stream().filter(entry -> entry.getValue().getCategory().equals(cat)).map(entry -> new HBox() {{
            Text name = new Text(entry.getKey().getName()) {{setFill(UtilAndConstants.textFill(pd.getColor()));}};
            Text grade = new Text(entry.getValue().getDisplayText(User.active()));
            getChildren().addAll(name, new UtilAndConstants.Filler(), grade);

            UtilAndConstants.underlineOnMouseOver(name);
            UtilAndConstants.underlineOnMouseOver(grade);
            name.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                switch (entry.getKey().getType()) {
                    case Housed:
                    case Test:
                    case Form:
                    case Module:
                        Root.getPortal().launchClass(pd, entry.getKey());
                        break;
                    case External:
                    default:
                        counterpart.showAssignment(entry.getKey());
                }
            });
        }}).collect(Collectors.toList()));

        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            subFieldsFade(!expanded);
            RotateTransition flip = new RotateTransition(Duration.millis(300));
            flip.setToAngle(expanded ? 0 : 180);
            flip.setNode(dropArrow);
            flip.play();
            expanded = !expanded;
        });
    }

    private void subFieldsFade(boolean appear) {
        new SequentialTransition(this, (Animation[]) entries.stream().map(entry -> new Timeline(new KeyFrame(Duration.millis(20), new KeyValue(entry.visibleProperty(), appear)))).toArray()).play();
    }

    public ClassPd getPd() {
        return pd;
    }

    public StudentGrades getAllGrades() {
        return allGrades;
    }
}
