package test_bed;

import java.util.Locale;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jcd.data.DataManager;
import jcd.data.MethodPrototype;
import jcd.data.VariablePrototype;
import jcd.file.FileManager;
import jcd.gui.ContainerPane;

/**
 *
 * @author Shane
 */
public class TestLoad extends Application { 
    
    public static void main(String[] args) {  
        // INITIALIZE JAVAFX COMPONENTS
        Locale.setDefault(Locale.US);
	launch(args);
    }
    
    public void start(Stage fakeStage) {
        // PHONY PANE TO INITIAlIZE DATAMANAGER
        Pane phony = new Pane();
        
        // ADD CONTAINERS + Links TO DATAMANAGER
        DataManager dataManager = new DataManager();
        dataManager.setDiagramComponents(phony.getChildren());
        
        FileManager fileManager = new FileManager();
        // LOAD DATA
        try {
            System.out.println("Starting test load...");
            fileManager.loadData(dataManager, "./work/DesignSaveTest.json");
            System.out.println("Finished test load.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        // PRINT DATA
        ContainerPane container;
        for (Node diagramComponent : dataManager.getDiagramComponents()) {
            if (diagramComponent instanceof ContainerPane) {
                container = (ContainerPane)diagramComponent;
                System.out.println("Name: " + container.getName() + ", Package: " + container.getPackage());
                System.out.println("Member variables:");
                for (VariablePrototype member : container.getContainer().getVariables())
                    System.out.println(member.toString());
                System.out.println("Member functions:");
                for (MethodPrototype method : container.getContainer().getMethods())
                    System.out.println(method.toString());
                System.out.println("-------------------------------------------------");
            }
        }
        System.exit(0);
    }
}
