package jcd.controller;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Cursor;
import static jcd.controller.ControlMode.*;
import jcd.data.DataManager;
import jcd.gui.ContainerPane;
import jcd.gui.Workspace;
import saf.AppTemplate;

/**
 *
 * @author Shane
 */
public class CanvasController {
    public static final int SNAP_SCALE = 15;
    
    AppTemplate app;
    DataManager dataManager;
    private boolean snapEnabled;
    
    public CanvasController(AppTemplate initApp) {
	app = initApp;
	dataManager = (DataManager)app.getDataComponent();
        snapEnabled = false;
    }
    
    public void handleMousePress(int x, int y) {
        if (dataManager.currControlModeIs(SELECTING)) {
            ContainerPane selectedContainer = dataManager.selectTopElement(x, y);
            if (selectedContainer != null) {
                app.getGUI().getPrimaryScene().setCursor(Cursor.MOVE);
                dataManager.setControlMode(DRAGGING);
            } else {
                app.getGUI().getPrimaryScene().setCursor(Cursor.DEFAULT);
            }
        } else if (dataManager.currControlModeIs(PRE_SIZING)) {
            ContainerPane selectedContainer = dataManager.selectTopElementForResize(x, y);
            if (selectedContainer != null) {
                dataManager.setControlMode(SIZING);
            } else {
                app.getGUI().getPrimaryScene().setCursor(Cursor.DEFAULT); 
            }        
        }  
        ((Workspace)app.getWorkspaceComponent()).reloadWorkspace();
    }
    
    public void handleMouseRelease(int x, int y) {
        if (dataManager.currControlModeIs(DRAGGING)) {
            if (snapEnabled) {
                int snapScale = (int)dataManager.getScale() * SNAP_SCALE;
                dataManager.getSelectedContainer().setLocation(x - (x % snapScale), y - (y % snapScale));
            }
            else 
                dataManager.getSelectedContainer().setLocation(x, y);
            ((Workspace)app.getWorkspaceComponent()).reloadWorkspace();
            app.getGUI().getPrimaryScene().setCursor(Cursor.MOVE);
            dataManager.setControlMode(SELECTING);
        } else if (dataManager.currControlModeIs(SELECTING))
            app.getGUI().getPrimaryScene().setCursor(Cursor.MOVE);
        else if (dataManager.currControlModeIs(SIZING)) {
            if (snapEnabled) {
                int snapScale = (int)dataManager.getScale() * SNAP_SCALE;
                dataManager.getSelectedContainer().resize(x - (x % snapScale), y - (y % snapScale));
            } else
                dataManager.getSelectedContainer().resize(x, y);
            dataManager.setControlMode(PRE_SIZING);
            dataManager.selectContainer(null);
        } else if (dataManager.currControlModeIs(PRE_SIZING))
            app.getGUI().getPrimaryScene().setCursor(Cursor.SE_RESIZE);
    }
    
    public void handleMouseDragged(int x, int y) {
        if (dataManager.currControlModeIs(DRAGGING)) {
            if (snapEnabled) {
                int snapScale = (int)dataManager.getScale() * SNAP_SCALE;
                dataManager.getSelectedContainer().setLocation(x - (x % snapScale), y - (y % snapScale));
            }
            else 
                dataManager.getSelectedContainer().setLocation(x, y);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.currControlModeIs(SIZING)) {
            if (snapEnabled) {
                int snapScale = (int)dataManager.getScale() * SNAP_SCALE;
                dataManager.getSelectedContainer().resize(x - (x % snapScale), y - (y % snapScale));
            }
            dataManager.getSelectedContainer().resize(x, y);
            app.getGUI().updateToolbarControls(false);
        }
    }
    
    public void handleMouseExited(int x, int y) {
        
    }
    
    public void handleSnappingChange(BooleanProperty enabled) {
        snapEnabled = enabled.get();
    }
}
