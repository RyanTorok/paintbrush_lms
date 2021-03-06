package gui;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import main.Events;
import main.Size;

import java.util.Random;

public abstract class PrecipParticle {
    final boolean day;
    Shape shape;
    int size;
    private AnchorPane holder;

    PrecipParticle(AnchorPane holder, double speed, boolean day) {
        this(new Random().nextInt(6) + 1, speed, day);
        this.holder = holder;
    }

    public PrecipParticle(int size, double speed, boolean day) {
        if (size < 3)
            this.size = 1;
        else if (size < 5)
            this.size = 2;
        else this.size = 3;
        this.day = day;
        shape = get();

        //random start point
        double rand = Math.random() * (Size.width(1920) + Size.height(1080));
        if (rand < Size.height(1080))
            AnchorPane.setTopAnchor(shape, rand);
        else AnchorPane.setLeftAnchor(shape, rand - Size.height(1080));
        TranslateTransition move = new TranslateTransition();
        move.setDuration(Duration.seconds((1.0 / speed) * ((4 - this.size) * 3 * (Math.random() / 4 + .875))));
        move.setNode(shape);
        move.setByX(Size.width(1000 + (Math.random() * 500) - 250));
        move.setByY(Size.height(2000));
        //Don't let this stop a screen resize, otherwise the resize would never happen
        Events.animation(move, false);
        move.setOnFinished(event -> holder.getChildren().remove(shape));

    }

    protected abstract Shape get();
}