package simulatorvisualizer.model.instructionanimation;

import configuration.model.engine.Calculator;
import configuration.model.engine.Memory;
import configuration.model.engine.RegisterList;
import configuration.model.engine.Token;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import simulatorvisualizer.model.CalvisAnimation;
import simulatorvisualizer.model.TimeLineFunction;

import java.math.BigInteger;

/**
 * Created by Goodwin Chua on 5 Jul 2016.
 */
public class Pinsrw extends CalvisAnimation {

    @Override
    public void animate(ScrollPane scrollPane) {
        this.root.getChildren().clear();
        scrollPane.setContent(root);
        RegisterList registers = currentInstruction.getRegisters();
        ObservableList<Node> parent = this.root.getChildren();
        Memory memory = currentInstruction.getMemory();
        TimeLineFunction timeFunc = new TimeLineFunction(timeline, root, registers, memory, finder);
        Calculator c = new Calculator(registers, memory);
        Token[] tokens = currentInstruction.getParameterTokens();
        Token des = tokens[0];
        Token src = tokens[1];
        Token ctr = tokens[2];
        int size = timeFunc.getBitSize(des);
        int sizeSrc = timeFunc.getBitSize(src);
        String desStr = timeFunc.getValue(des);
        String srcStr = timeFunc.getPreviousValue(des, size);
        String foundWord = "";
        if(sizeSrc == 16 ){
            foundWord = timeFunc.getPreviousValue(des, 16);
        }
        else if(sizeSrc == 32){
            foundWord = timeFunc.getPreviousValue(des, 16).substring(4, 8);
        }
        String resultStr = timeFunc.getValue(des, size);
        int operationSize = 8;
        int count = Integer.parseInt(new BigInteger(timeFunc.getValue(ctr, size), 16).toString(10)) % 8;
        Text sign = new Text("Transfer Value To ^");

        Rectangle desRec = this.createRectangle(des, 230, 80);
        Rectangle srcRec = this.createRectangle(src, 320, 150);
        Rectangle fake = new Rectangle(0,0);
        Text desLabel = this.createLabelText(des);
        Text srcLabel = this.createLabelText(src);
        Text srcValue = new Text(srcStr);
        Text desValue = new Text(desStr);

        parent.addAll(fake, desRec, srcRec, desLabel, srcLabel, desValue, sign);


        //

        double posX = 200;
        double posY = 20;

        desRec.setX(posX + 50);
        desRec.setY(posY);

        desLabel.setX(posX + (desRec.getLayoutBounds().getWidth() / 2 - desLabel.getLayoutBounds().getWidth() / 2) + 50);
        desLabel.setY(posY + 15);

        desValue.setX(posX + (desRec.getLayoutBounds().getWidth() / 2 - desValue.getLayoutBounds().getWidth() / 2) + 50);
        desValue.setY(posY + 40);

        srcRec.setX(posX);
        srcRec.setY(posY + desRec.getHeight() + 50);

        sign.setX(srcRec.getWidth() / 2 - sign.getLayoutBounds().getWidth()/2 + posX);
        sign.setY(posY + desRec.getHeight() + 25);

        srcLabel.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - srcLabel.getLayoutBounds().getWidth() / 2));
        srcLabel.setY(posY + 30 + desRec.getHeight() + 40);

        srcValue.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - srcValue.getLayoutBounds().getWidth() / 2));
        srcValue.setY(posY + 40 + desRec.getHeight() + 45);

        String srcBits = c.convertHexToBits(srcStr, timeFunc.getBitSize(src));
        System.out.println("srcBITS " + srcBits);
        Text counterText = new Text(this.createLabelText(src).getText() + "Word Index : " + Integer.toString(count));
        Text extractedWord = new Text(" " + foundWord + "");
        Text foundedWord = new Text("Found Word: " + foundWord);
        extractedWord.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        Text firstHalf = new Text("");
        if(count > 0){
            firstHalf.setText(srcStr.substring((0), 4 * count));
        }
        Text secondHalf = new Text("");
        System.out.println((count) + " wow " + (size/16));
        if(count + 1 < timeFunc.getBitSize(des) / 16){
            secondHalf.setText(srcStr.substring((4 * count) + 4));
        }


        TextFlow combinedText = new TextFlow(new Text("Result:"), firstHalf, extractedWord, secondHalf);
        combinedText.setLayoutX(posX + (srcRec.getLayoutBounds().getWidth()/ 9 - combinedText.getLayoutBounds().getWidth()/ 2));
        combinedText.setLayoutY(posY + 100 + desRec.getHeight() + 45);

        parent.addAll(counterText, foundedWord, combinedText);

        counterText.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - counterText.getLayoutBounds().getWidth() * 0.75));
        counterText.setY(posY + 60 + desRec.getHeight() + 45);

        foundedWord.setX(posX + (srcRec.getLayoutBounds().getWidth() / 2 - foundedWord.getLayoutBounds().getWidth() * 0.75));
        foundedWord.setY(posY + 80 + desRec.getHeight() + 45);



    }
}

