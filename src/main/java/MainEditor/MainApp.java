package MainEditor;

import Editor.controller.SystemController;
import EnvironmentConfiguration.controller.EnvironmentConfigurator;
import MainEditor.controller.ConfigurationEnvironmentController;
import MainEditor.controller.WorkspaceController;
import SimulatorVisualizer.controller.SimulationEngine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Jennica Alcalde on 10/1/2015.
 */
public class MainApp extends Application {

    public static Stage primaryStage;
    private FXMLLoader loader;
    private Parent root;
    private ConfigurationEnvironmentController configController;

    /**
     * Method implemented by extending the class to Application.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("CALVIS Instruction Set Configuration");

        initRootLayout();
    }

    /**
     * Initialize the root layout
     * @throws IOException
     */
    public void initRootLayout() throws IOException{
        loadPrimaryStageController();
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Opens a stage to show CALVIS workspace.
     * @throws IOException
     */
    public void showWorkspace() throws IOException {
        // Load root layout from fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/workspace.fxml"));
        Parent workspaceLayout = (BorderPane) loader.load();

        WorkspaceController workspaceController = loader.getController();

        primaryStage.setScene(new Scene(workspaceLayout));
        primaryStage.setTitle("CALVIS x86-32 Workspace");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.show();

        workspaceController.setEngine(buildEnvironment());
        workspaceController.displayDefaultWindows();
    }

    /**
     * Hide the main stage.
     */
    public void hidePrimaryStage() {
        this.primaryStage.hide();
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public void loadPrimaryStageController() throws IOException {
        // Load root layout from fxml file
        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/configurationEnvironment.fxml"));
        root = (BorderPane) loader.load();

        // Give the controller access to the main app
        configController = (ConfigurationEnvironmentController)  loader.getController();
        configController.setMainApp(this);

    }

    /**
     * Main method to run the application.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    public ConfigurationEnvironmentController getStageController(){
        return configController;
    }

    private SystemController buildEnvironment(){
        EnvironmentConfigurator envCon = new
                EnvironmentConfigurator(configController.getConfigurationFilePath());
        SimulationEngine simEng = new
                SimulationEngine(envCon.getRegisters(), envCon.getMemory());
        SystemController ediCon = new
                SystemController(envCon.getParser(), simEng);

        return ediCon;
    }
}
