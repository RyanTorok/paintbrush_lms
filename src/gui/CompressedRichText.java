package gui;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CompressedRichText {
    private String unformattedText;
    private HashMap<String, List<Pair<Integer, Integer>>> styles;

    public CompressedRichText(TextFlow flow) {
        unformattedText = "";
        styles = new HashMap<>();
        int characterIndex = 0;
        for (Node n : flow.getChildren()) {
            if (n instanceof Text) {
                Text t = (Text) n;
                String[] myStyles = t.getStyle().split(";");
                int textLength = t.getText().length();
                for (String component : myStyles) {
                    List<Pair<Integer, Integer>> mappings = styles.computeIfAbsent(component, k -> new ArrayList<>());
                    mappings.add(new Pair<>(characterIndex, characterIndex + textLength));
                }
                characterIndex += textLength;
                unformattedText += t.getText();
            }
        }
    }

    public TextFlow extract() {
        TextFlow flow = new TextFlow();
        for (int i = 0; i < unformattedText.length(); i++) {
            flow.getChildren().add(new Text(Character.toString(unformattedText.charAt(i))));
        }
        for (String styleComponent : styles.keySet()) {
            List<Pair<Integer, Integer>> indexList = styles.get(styleComponent);
            for (Pair<Integer, Integer> range :
                    indexList) {
                for (int i = range.getKey(); i < range.getValue(); i++) {
                    String[] split = styleComponent.split(":");
                    Styles.setProperty(flow.getChildren().get(i), split[0], split[1]);
                }
            }
        }
        return flow;
    }

    public String getUnformattedText() {
        return unformattedText;
    }

    @Override
    public String toString() {
        return unformattedText;
    }
}