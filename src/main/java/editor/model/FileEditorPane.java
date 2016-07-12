package editor.model;

import editor.controller.WorkspaceController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import org.fxmisc.undo.UndoManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Jennica on 02/07/2016.
 */
public class FileEditorPane {

    private WorkspaceController workspaceController;
    private Tab tab = new Tab();
    private TextEditor textEditor = new TextEditor();
    private boolean isLoaded = false;

    // 'path' property
    private final ObjectProperty<Path> path = new SimpleObjectProperty<>();
    // 'modified' property
    private final ReadOnlyBooleanWrapper modified = new ReadOnlyBooleanWrapper();
    // 'canUndo' property
    private final BooleanProperty canUndo = new SimpleBooleanProperty();
    // 'canRedo' property
    private final BooleanProperty canRedo = new SimpleBooleanProperty();

    public FileEditorPane(WorkspaceController workspaceController, Path path) {
        this.workspaceController = workspaceController;

        this.tab.setUserData(this);

        this.path.set(path);
        this.path.addListener((observable, oldPath, newPath) -> updateTab());
        this.modified.addListener((observable, oldPath, newPath) -> updateTab());
        this.updateTab();

        this.tab.setOnSelectionChanged(e -> {
            if ( tab.isSelected() )
                Platform.runLater(() -> activate());
        });

        this.tab.setContent(textEditor.getCodeArea());
//        this.activate();
    }

    public Tab getTab() {
        return tab;
    }

    public TextEditor getTextEditor() {
        return textEditor;
    }

    // Path Property
    public Path getPath() {
        return path.get();
    }

    public void setPath(Path path) {
        this.path.set(path);
    }

    public ObjectProperty<Path> pathProperty() {
        return path;
    }

    // Modified Property
    public boolean isModified() {
        return modified.get();
    }

    public ReadOnlyBooleanProperty modifiedProperty() {
        return modified.getReadOnlyProperty();
    }

    // 'canUndo' property
    public BooleanProperty canUndoProperty() {
        return canUndo;
    }

    // 'canRedo' property
    public BooleanProperty canRedoProperty() {
        return canRedo;
    }

    private void updateTab() {
        Path path = this.path.get();
        this.tab.setText((path != null) ? path.getFileName().toString() : "Untitled");
        this.tab.setTooltip((path != null) ? new Tooltip(path.toString()) : null);
        this.tab.setGraphic(isModified() ? new Text("*") : null);
    }

    private void activate() {
        System.out.println("tabPane = " + this.tab.getTabPane());
        System.out.println("tabContent = " + this.tab.getContent());

        if( this.tab.getTabPane() == null || !tab.isSelected() )
            return; // Tab is already closed or no longer active

        if ( this.tab.getContent() != null ) {
            this.textEditor.requestFocus();
//            return;
        } else {
            this.textEditor = new TextEditor();
            this.tab.setContent(this.textEditor.getCodeArea());
        }

        this.textEditor.pathProperty().bind(path);

        // Load file and Create UI when the tab becomes visible the first time
        if( !this.isLoaded )
            this.isLoaded = this.load();

        // Clear undo history after first load
        this.textEditor.getUndoManager().forgetHistory();

        // Bind the text editor undo manager to the properties
        UndoManager undoManager = textEditor.getUndoManager();
        this.modified.bind(Bindings.not(undoManager.atMarkedPositionProperty()));
        this.canUndo.bind(undoManager.undoAvailableProperty());
        this.canRedo.bind(undoManager.redoAvailableProperty());

        this.textEditor.requestFocus();
    }

    private boolean load() {
        System.out.println("load text");
        Path path = this.path.get();
        if ( path == null )
            return false;

        try {
            byte[] bytes = Files.readAllBytes(path);
            String text = new String(bytes);
            this.textEditor.setCodeAreaText(text);
            this.textEditor.getUndoManager().mark();
            return  true;
        } catch ( IOException ex ) {
            Alert alert = workspaceController.createAlert(Alert.AlertType.ERROR, "Load",
                    "Failed to load ''{0}''.\n\nReason: {1}", path, ex.getMessage());
            alert.showAndWait();
        }
        return false;
    }

    public boolean save() {
        String text = textEditor.getCodeAreaText();
        byte[] bytes = text.getBytes();

        try {
            Files.write(path.get(), bytes);
            this.textEditor.getUndoManager().mark();
            return true;
        } catch ( IOException ex ) {
            Alert alert = workspaceController.createAlert(Alert.AlertType.ERROR, "Save",
                    "Failed to save ''{0}''.\n\nReason: {1}", path.get(), ex.getMessage());
            alert.showAndWait();
            return false;
        }
    }
}
