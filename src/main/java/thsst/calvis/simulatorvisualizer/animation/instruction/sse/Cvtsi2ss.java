package thsst.calvis.simulatorvisualizer.animation.instruction.sse;

import javafx.animation.TranslateTransition;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import thsst.calvis.configuration.model.engine.Memory;
import thsst.calvis.configuration.model.engine.RegisterList;
import thsst.calvis.configuration.model.engine.Token;
import thsst.calvis.simulatorvisualizer.model.CalvisAnimation;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Cvtsi2ss extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);

        RegisterList registers = this.currentInstruction.getRegisters();
        Memory memory = this.currentInstruction.getMemory();

        // ANIMATION ASSETS
        Token[] tokens = this.currentInstruction.getParameterTokens();
        for ( int i = 0; i < tokens.length; i++ ) {
            System.out.println(tokens[i] + " : " + tokens[i].getClass());
        }

        // CODE HERE
        int width = 300;
        int height = 70;
        Rectangle desRectangle = this.createRectangle(tokens[0], width, height);
        Rectangle srcRectangle = this.createRectangle(tokens[1], width, height);

        if ( desRectangle != null && srcRectangle != null ) {
            desRectangle.setX(X);
            desRectangle.setY(Y);
            desRectangle.setArcWidth(10);
            desRectangle.setArcHeight(10);

            srcRectangle.setX(desRectangle.xProperty().getValue() + desRectangle.getLayoutBounds().getWidth() + X);
            srcRectangle.setY(Y);
            srcRectangle.setArcWidth(10);
            srcRectangle.setArcHeight(10);

            Circle operandCircle = new Circle(desRectangle.xProperty().getValue() +
                    desRectangle.getLayoutBounds().getWidth() + 50,
                    135, 40, Color.web("#798788", 1.0));

            root.getChildren().addAll(desRectangle, srcRectangle, operandCircle);

            int desBitSize = 64;
            int desHexSize = desBitSize / 4;
            // SOURCE IF XMM REGISTER = 128, ELSE MEMORY = 64
            int srcBitSize = 32;
//            if( tokens[1].getType().equals(Token.REG) )
//                srcBitSize = registers.getBitSize(tokens[1]);
//            else
//                srcBitSize = memory.getBitSize(tokens[1]);
//            int srcHexSize = srcBitSize / 4;

            String description = "Destination[31:0]  Convert Integer to Single Precision Floating Point(Source[31:0])";

//            "IF SOURCE BIT SIZE == 64\n"
//                    + "DESTINATION[63:0] <- Convert Integer to Double Precision Floating Point ( SOURCE[63:0] )\n"
//                    + "ELSE IF SOURCE BIT SIZE == 32\n"

            Text detailsText = new Text(X, Y * 2, description);

            Text desLabelText = this.createLabelText(X, Y, tokens[0]);
            String desValueString = this.getSubLowerHexValueString(tokens[0], registers, memory, desBitSize, desHexSize/2);
            Text desValueText = new Text(X, Y, desValueString);

            Text srcLabelText = this.createLabelText(X, Y, tokens[1]);
            String srcValueString = this.getValueString(tokens[1], registers, memory, srcBitSize);
            Text srcValueText = new Text(X, Y, srcValueString);

            Text operandText = new Text(X, Y, "CONVERT\n <<<-----");
//            operandText.setFont(Font.font(48));
            operandText.setFill(Color.WHITESMOKE);

            root.getChildren().addAll(detailsText, srcLabelText, srcValueText,
                    desLabelText, desValueText, operandText);

            // ANIMATION LOGIC
            TranslateTransition desLabelTransition = new TranslateTransition();
            TranslateTransition desValueTransition = new TranslateTransition(new Duration(1000), desValueText);
            TranslateTransition srcLabelTransition = new TranslateTransition();
            TranslateTransition srcValueTransition = new TranslateTransition();
            TranslateTransition operandTransition = new TranslateTransition();

            // DESTINATION LABEL -- STATIC
            desLabelTransition.setNode(desLabelText);
            desLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth()
                            - desLabelText.getLayoutBounds().getWidth()) / 2));
            desLabelTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 3));
            desLabelTransition.toXProperty().bind(desLabelTransition.fromXProperty());
            desLabelTransition.toYProperty().bind(desLabelTransition.fromYProperty());

            // DESTINATION VALUE -- STATIC
            desValueTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add((desRectangle.getLayoutBounds().getWidth()
                            - desValueText.getLayoutBounds().getWidth()) / 2));
            desValueTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
            desValueTransition.toXProperty().bind(desValueTransition.fromXProperty());
            desValueTransition.toYProperty().bind(desValueTransition.fromYProperty());

            // OPERAND -- STATIC
            operandTransition.setNode(operandText);
            operandTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + 40
                            + desRectangle.getLayoutBounds().getWidth())
                    .divide(2));
            operandTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 2));
            operandTransition.toXProperty().bind(operandTransition.fromXProperty());
            operandTransition.toYProperty().bind(operandTransition.fromYProperty());

            // SOURCE LABEL -- STATIC
            srcLabelTransition.setNode(srcLabelText);
            srcLabelTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((desRectangle.getLayoutBounds().getWidth()
                            - srcLabelText.getLayoutBounds().getWidth()) / 2));
            srcLabelTransition.fromYProperty().bind(desLabelTransition.fromYProperty());
            srcLabelTransition.toXProperty().bind(srcLabelTransition.fromXProperty());
            srcLabelTransition.toYProperty().bind(srcLabelTransition.fromYProperty());

            // SOURCE VALUE -- STATIC
            srcValueTransition.setNode(srcValueText);
            srcValueTransition.fromXProperty().bind(desRectangle.translateXProperty()
                    .add(desRectangle.getLayoutBounds().getWidth() + X)
                    .add((desRectangle.getLayoutBounds().getWidth()
                            - srcValueText.getLayoutBounds().getWidth()) / 2));
            srcValueTransition.fromYProperty().bind(desRectangle.translateYProperty()
                    .add(desRectangle.getLayoutBounds().getHeight() / 1.5));
            srcValueTransition.toXProperty().bind(srcValueTransition.fromXProperty());
            srcValueTransition.toYProperty().bind(srcValueTransition.fromYProperty());

            // Play 1000 milliseconds of animation
            desLabelTransition.play();
            desValueTransition.play();
            srcLabelTransition.play();
            srcValueTransition.play();
            operandTransition.play();
        }
    }
}

