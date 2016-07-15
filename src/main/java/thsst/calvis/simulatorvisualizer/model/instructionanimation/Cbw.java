package thsst.calvis.simulatorvisualizer.model.instructionanimation;

import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Marielle Ong on 8 Jul 2016.
 */
public class Cbw extends CalvisAnimation {

    @Override
    public void animate(ScrollPane tab) {
        this.root.getChildren().clear();
        tab.setContent(root);

        RegisterList registers = currentInstruction.getRegisters();
        Memory memory = currentInstruction.getMemory();

        // ANIMATION ASSETS
//        Token[] tokens = currentInstruction.getParameterTokens();
//        for ( int i = 0; i < tokens.length; i++ ) {
//            System.out.println(tokens[i] + " : " + tokens[i].getClass());
//        }

        // CODE HERE
        int width = 140;
        int height = 70;
        Token tokenAL = new Token(Token.REG, "AL");
        Token tokenAH = new Token(Token.REG, "AH");
        Rectangle desRectangle = this.createRectangle(tokenAH, width, height);
        Rectangle srcRectangle = this.createRectangle(tokenAL, width, height);

        Text text = new Text("The sign of register AL is extended to register AH.\n" +
                "Affected flags: none");

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(100);
            desRectangle.setY(50);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.widthProperty().getValue() + 100);
            srcRectangle.setY(50);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            root.getChildren().addAll(text, desRectangle, srcRectangle);

            int desSize = 0;
            if ( tokenAH.getType() == Token.REG )
                desSize = registers.getBitSize(tokenAH);
            else if ( tokenAH.getType() == Token.MEM && tokenAL.getType() == Token.REG )
                desSize = registers.getBitSize(tokenAL);
            else
                desSize = memory.getBitSize(tokenAH);

            Text desLabelText = new Text("AH");
            Text desValueText = new Text(registers.get(tokenAH.getValue() + ""));
            Text srcLabelText = new Text("AL");
            Text srcValueText = new Text(registers.get(tokenAL.getValue() + ""));

            desLabelText.setX(90);
            desLabelText.setY(50);

            desValueText.setX(90);
            desValueText.setY(50);

            srcLabelText.setX(90);
            srcLabelText.setY(50);

            srcValueText.setX(90);
            srcValueText.setY(50);

            root.getChildren().addAll(desLabelText, desValueText, srcLabelText, srcValueText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcTransition = new TranslateTransition();

            // Destination label static
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(10 + (desRectangle.getLayoutBounds().getWidth() - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // Destination value moving
            desTransition.setInterpolator(Interpolator.LINEAR);
            desTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
                    .add((srcRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.fromYProperty().bind(srcRectangle.translateYProperty()
                    .add(srcRectangle.getLayoutBounds().getHeight() / 1.5));
            desTransition.toXProperty().bind(desRectangle.translateXProperty()
                    .add(10 + (desRectangle.getLayoutBounds().getWidth() - desValueText.getLayoutBounds().getWidth()) / 2));
            desTransition.toYProperty().bind(desTransition.fromYProperty());

            // Source label static
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // Source value static
            srcTransition.setNode(srcValueText);
            srcTransition.fromXProperty().bind(srcRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 110)
                    .add((srcRectangle.getLayoutBounds().getWidth() - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcTransition.fromYProperty().bind(desTransition.fromYProperty());
            srcTransition.toXProperty().bind(srcTransition.fromXProperty());
            srcTransition.toYProperty().bind(srcTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            srcLabelTransition.play();
            desTransition.play();
            srcTransition.play();
        }
    }
}