package jcd.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import static jcd.PropertyType.EXPORT_CODE_ERROR_MESSAGE;
import static jcd.PropertyType.EXPORT_CODE_ERROR_TITLE;
import static jcd.PropertyType.EXPORT_CODE_TITLE;
import static jcd.PropertyType.NAME_UPDATE_ERROR_TITLE;
import static jcd.controller.ControlMode.*;
import jcd.data.ContainerType;
import jcd.data.DataManager;
import jcd.data.LinkType;
import jcd.data.MethodPrototype;
import jcd.data.VariablePrototype;
import jcd.file.FileManager;
import jcd.gui.ContainerPane;
import jcd.gui.MethodEditSingleton;
import jcd.gui.PackageInquirySingleton;
import jcd.gui.ParentSelectionSingleton;
import jcd.gui.VariableEditSingleton;
import jcd.gui.Workspace;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import static saf.settings.AppPropertyType.SAVE_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_ERROR_TITLE;
import static saf.settings.AppStartupConstants.PATH_WORK;
import saf.ui.AppMessageDialogSingleton;

/**
 *
 * @author Shane
 */
public class DiagramEditController {

    AppTemplate app;

    DataManager dataManager;

    public DiagramEditController(AppTemplate initApp) {
        app = initApp;
        dataManager = (DataManager) app.getDataComponent();
    }

    public void handleSnapshotRequest() {
        dataManager.setHighlightSelected(false);
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        WritableImage canvasImage = workspace.getDiagramCanvas().snapshot(new SnapshotParameters(), null);
        dataManager.setHighlightSelected(true);

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK + "diagram_images/"));
        fc.setTitle(props.getProperty("Save Your Snapshot"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(".png", ".png"));

        File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
        FileManager fileManager = (FileManager) app.getFileComponent();
        try {
            fileManager.saveImage(canvasImage, selectedFile);
        } catch (IOException e) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(SAVE_ERROR_TITLE), props.getProperty(SAVE_ERROR_MESSAGE));
        }
    }

    public void handleExportCode() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // PROMPT THE USER FOR A FILE NAME
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle(props.getProperty(EXPORT_CODE_TITLE));

        File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
        FileManager fileManager = (FileManager) app.getFileComponent();
        try {
            if (selectedFile != null) {
                fileManager.exportCode(dataManager, selectedFile);
            }
        } catch (Exception e) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(EXPORT_CODE_ERROR_TITLE), props.getProperty(EXPORT_CODE_ERROR_MESSAGE));
        }
    }

    public void handleSelectionTool() {
        app.getGUI().getPrimaryScene().setCursor(Cursor.MOVE);
        dataManager.setControlMode(SELECTING);
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
    }

    public void handleResizeTool() {
        app.getGUI().getPrimaryScene().setCursor(Cursor.SE_RESIZE);
        dataManager.setControlMode(PRE_SIZING);
        dataManager.selectContainer(null);
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
    }

    public void handleAddClass() {
        dataManager.addContainer(ContainerType.CLASS);
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleAddInterface() {
        dataManager.addContainer(ContainerType.INTERFACE);
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleRemoveElement() {
        dataManager.removeContainer();
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleUndo() {
        dataManager.undoChange();
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleRedo() {
        dataManager.redoChange();
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleZoomIn() {

        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleZoomOut() {

        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleShowGridChange(BooleanProperty selectedProperty) {

        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleNameChange(String name) {
        try {
            dataManager.setSelectedName(name);
        } catch (IllegalArgumentException iae) {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
            messageDialog.show(props.getProperty(NAME_UPDATE_ERROR_TITLE), iae.getMessage());
            app.getGUI().updateToolbarControls(false);
        }
    }

    public void handlePackageChange(String packageName) {
        try {
            dataManager.setSelectedPackage(packageName);
        } catch (IllegalArgumentException iae) {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
            messageDialog.show(props.getProperty(NAME_UPDATE_ERROR_TITLE), iae.getMessage());
            app.getGUI().updateToolbarControls(false);
        }
    }

    public void handleParentEdit() {
        ParentSelectionSingleton parentSelectionSingleton = ParentSelectionSingleton.getSingleton();
        parentSelectionSingleton.show(dataManager);
        // EXTENDS
        String extendClass = parentSelectionSingleton.getSelectedExtends();
        if (extendClass.equals("CANCELLED EDIT"))
            return;
        if (!extendClass.equals(dataManager.getSelectedContainer().getContainer().getExtendClass())) {
            if (!dataManager.getSelectedContainer().getContainer().getExtendClass().equals("")) {
                String oldExtendClass = dataManager.getSelectedContainer().getContainer().getExtendClass();
                for (ContainerPane container : dataManager.getContainers()) {
                    if (container.getName().equals(oldExtendClass)) {
                        dataManager.removeLinkFromSelected(LinkType.EXTENDS, container);
                        break;
                    }
                }
            }
            dataManager.getSelectedContainer().getContainer().setExtendClass(extendClass);
            if (!extendClass.equals("")) {
                ContainerPane newParent = null;
                for (ContainerPane container : dataManager.getContainers()) {
                    if (container.getName().equals(extendClass)) {
                        newParent = container;
                        break;
                    }
                }
                dataManager.addLink(LinkType.EXTENDS, dataManager.getSelectedContainer(), newParent);
            }
        }
        // IMPLEMENTS
        ArrayList<String> implementInterfaces = parentSelectionSingleton.getSelectedImplements();
        ArrayList<String> implementsToRemove = (ArrayList<String>) dataManager.getSelectedContainer().getContainer().getImplementInterfaces().clone();
        implementsToRemove.removeAll(implementInterfaces);
        implementInterfaces.removeAll(dataManager.getSelectedContainer().getContainer().getImplementInterfaces());
        for (String inter : implementsToRemove) {
            dataManager.getSelectedContainer().getContainer().getImplementInterfaces().remove(inter);
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(inter)) {
                    dataManager.removeLinkFromSelected(LinkType.IMPLEMENTS, container);
                }
            }
        }
        for (String inter : implementInterfaces) {
            dataManager.getSelectedContainer().getContainer().addImplementInterface(inter);
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(inter)) {
                    dataManager.addLink(LinkType.IMPLEMENTS, dataManager.getSelectedContainer(), container);
                }
            }
        }
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleAddVariable() {
        VariableEditSingleton variableEditSingleton = VariableEditSingleton.getSingleton();
        PackageInquirySingleton packageInquirySingleton = PackageInquirySingleton.getSingleton();
        variableEditSingleton.show(null);

        // CHECK FOR USED CLASSES
        if (!variableEditSingleton.getUsedClass().equals("")) {
            boolean needToCreate = true;
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(variableEditSingleton.getUsedClass())) {
                    needToCreate = false;
                }
            }
            if (needToCreate) {
                ContainerPane selectedContainer = dataManager.getSelectedContainer();
                dataManager.addContainer(ContainerType.CLASS);
                dataManager.setSelectedName(variableEditSingleton.getUsedClass());
                // ASK FOR PACKAGE PATH
                packageInquirySingleton.show(variableEditSingleton.getUsedClass());
                dataManager.setSelectedPackage(packageInquirySingleton.getPackage());
                dataManager.selectContainer(selectedContainer);
            }
            // ADD LINK
            ContainerPane from = null;
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(variableEditSingleton.getUsedClass())) {
                    from = container;
                    break;
                }
            }
            dataManager.addLink(LinkType.AGGREGATE, from, dataManager.getSelectedContainer());
        }
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleEditVariable(VariablePrototype variable) {
        VariableEditSingleton variableEditSingleton = VariableEditSingleton.getSingleton();
        PackageInquirySingleton packageInquirySingleton = PackageInquirySingleton.getSingleton();
        variableEditSingleton.show(variable);

        // CHECK FOR USED CLASSES
        if (!variableEditSingleton.getUsedClass().equals("")) {
            boolean needToCreate = true;
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(variableEditSingleton.getUsedClass())) {
                    needToCreate = false;
                }
            }
            if (needToCreate) {
                ContainerPane selectedContainer = dataManager.getSelectedContainer();
                dataManager.addContainer(ContainerType.CLASS);
                dataManager.setSelectedName(variableEditSingleton.getUsedClass());
                // ASK FOR PACKAGE PATH
                packageInquirySingleton.show(variableEditSingleton.getUsedClass());
                dataManager.setSelectedPackage(packageInquirySingleton.getPackage());
                dataManager.selectContainer(selectedContainer);
            }
            // ADD LINK
            ContainerPane from = null;
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(variableEditSingleton.getUsedClass())) {
                    from = container;
                }
            }
            dataManager.addLink(LinkType.AGGREGATE, from, dataManager.getSelectedContainer());
        }
        dataManager.getSelectedContainer().update();
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleRemoveVariable(VariablePrototype variable) {
        dataManager.removeVariableFromSelected(variable);
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleAddMethod() {
        PackageInquirySingleton packageInquirySingleton = PackageInquirySingleton.getSingleton();
        MethodEditSingleton methodEditSingleton = MethodEditSingleton.getSingleton();
        methodEditSingleton.show(null);

        // CHECK FOR USED CLASSES
        for (String usedClass : methodEditSingleton.getUsedClasses()) {
            boolean needToCreate = true;
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(usedClass)) {
                    needToCreate = false;
                }
            }
            if (needToCreate) {
                ContainerPane selectedContainer = dataManager.getSelectedContainer();
                for (String className : methodEditSingleton.getUsedClasses()) {
                    dataManager.addContainer(ContainerType.CLASS);
                    dataManager.setSelectedName(className);
                    // ASK FOR PACKAGE PATH
                    packageInquirySingleton.show(className);
                    dataManager.setSelectedPackage(packageInquirySingleton.getPackage());
                }
                dataManager.selectContainer(selectedContainer);
            }
            // ADD LINKS
            ContainerPane from = null;
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(usedClass)) {
                    from = container;
                }
            }
            dataManager.addLink(LinkType.ASSOCIATED, from, dataManager.getSelectedContainer());
        }
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleEditMethod(MethodPrototype method) {
        PackageInquirySingleton packageInquirySingleton = PackageInquirySingleton.getSingleton();
        MethodEditSingleton methodEditSingleton = MethodEditSingleton.getSingleton();
        methodEditSingleton.show(method);

        // CHECK FOR USED CLASSES
        for (String usedClass : methodEditSingleton.getUsedClasses()) {
            boolean needToCreate = true;
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(usedClass)) {
                    needToCreate = false;
                }
            }
            if (needToCreate) {
                ContainerPane selectedContainer = dataManager.getSelectedContainer();
                for (String className : methodEditSingleton.getUsedClasses()) {
                    dataManager.addContainer(ContainerType.CLASS);
                    dataManager.setSelectedName(className);
                    // ASK FOR PACKAGE PATH
                    packageInquirySingleton.show(className);
                    dataManager.setSelectedPackage(packageInquirySingleton.getPackage());
                }
                dataManager.selectContainer(selectedContainer);
            }
            // ADD LINKS
            ContainerPane from = null;
            for (ContainerPane container : dataManager.getContainers()) {
                if (container.getName().equals(usedClass)) {
                    from = container;
                }
            }
            dataManager.addLink(LinkType.ASSOCIATED, from, dataManager.getSelectedContainer());
        }
        dataManager.getSelectedContainer().update();
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleRemoveMethod(MethodPrototype method) {
        dataManager.removeMethodFromSelected(method);
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }

    public void handleDataManagerReset() {
        dataManager.setControlMode(null);
        app.getGUI().getPrimaryScene().setCursor(Cursor.DEFAULT);
        ((Workspace) app.getWorkspaceComponent()).reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
    }
}
