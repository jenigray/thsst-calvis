package MainEditor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Jennica on 02/01/2016.
 */
public class FindAndReplaceDialogController {

    @FXML
    private TextField textFieldFind;
    @FXML
    private TextField textFieldReplace;

    private WorkspaceController workspaceController;
    private Stage dialogStage;

    public void setWorkspaceController(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public String getFindField() {
        return textFieldFind.getText();
    }

    public String getReplaceField() {
        return textFieldReplace.getText();
    }

    @FXML
    private void handleCancel(ActionEvent event) throws Exception {
        dialogStage.close();
    }

    @FXML
    private void handleFindAndReplace(ActionEvent event) throws Exception {
        System.out.println("FIND: " + getFindField());
        System.out.println("REPLACE: " + getReplaceField());

        workspaceController.onActionFindAndReplace(getFindField(), getReplaceField());
    }


}
