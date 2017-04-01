/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_bed;

import java.util.ArrayList;
import java.util.Locale;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static jcd.data.ContainerType.ABSTRACT_CLASS;
import static jcd.data.ContainerType.CLASS;
import static jcd.data.ContainerType.INTERFACE;
import jcd.data.DataManager;
import jcd.data.MethodPrototype;
import jcd.data.Modifier;
import static jcd.data.Modifier.ABSTRACT;
import static jcd.data.Modifier.PRIVATE;
import static jcd.data.Modifier.PROTECTED;
import static jcd.data.Modifier.PUBLIC;
import static jcd.data.Modifier.STATIC;
import jcd.data.VariablePrototype;
import jcd.file.FileManager;
import jcd.gui.ContainerPane;
import jcd.gui.UMLLink;
import static javafx.application.Application.launch;
import jcd.data.ContainerType;
import jcd.data.LinkType;

/**
 *
 * @author Shane
 */
public class ImitationJUnitTest3INTERFACE extends Application {
    
    public static void compareData(ArrayList<String> preLoadStrings, 
            ArrayList<String> postLoadStrings, ArrayList preLoadDoubles, ArrayList postLoadDoubles) {
        
        // COMPARE STRING DATA
        int totalComparisons = 0;
        int numCorrect = 0;
        for (int i = 0; i < preLoadStrings.size(); ++i) {
            System.out.println("pre: " + preLoadStrings.get(i) + "   |  post: " + postLoadStrings.get(i));
            if (preLoadStrings.get(i).equals(postLoadStrings.get(i))) {
                ++numCorrect;
                ++totalComparisons;
            }
        }
        System.out.println("String comparisons: " + (numCorrect / (double)totalComparisons) * 100 + "%\n");
          
        // COMPARE INT DATA
        totalComparisons = 0;
        numCorrect = 0;
        for (int i = 0; i < preLoadDoubles.size(); ++i) {
            System.out.println("pre: " + preLoadDoubles.get(i) + "   |  post: " + postLoadDoubles.get(i));
            if (preLoadDoubles.get(i).equals(postLoadDoubles.get(i))) {
                ++numCorrect;
                ++totalComparisons;
            }  
        }
        System.out.println("Double comparisons: " + (numCorrect / (double)totalComparisons) * 100 + "%\n");
    }
    
    public static void main(String[] args) {   
        // INITIALIZE JAVAFX COMPONENTS
        Locale.setDefault(Locale.US);
	launch(args);
    }
    
    public void start(Stage fakeStage) {
        ArrayList<ContainerPane> containers = new ArrayList();
        ArrayList<VariablePrototype> variables = new ArrayList();
        ArrayList<MethodPrototype> methods = new ArrayList();
        ArrayList<Modifier> modifiers = new ArrayList();

        // ThreadExample 0
        ContainerPane container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("ThreadExample");

        modifiers.add(PUBLIC);
        modifiers.add(STATIC);
        variables.add(new VariablePrototype("START_TEXT", "String", modifiers));
        modifiers.clear();

        modifiers.add(PUBLIC);
        modifiers.add(STATIC);
        variables.add(new VariablePrototype("PAUSE_TEXT", "String", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("window", "Stage", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("appPane", "BorderPane", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("topPane", "FlowPane", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("startButton", "Button", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("pauseButton", "Button", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("scrollPane", "ScrollPane", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("textArea", "TextArea", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("dateThread", "Thread", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("dateTask", "Task", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("counterThread", "Thread", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("counterTask", "Task", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("work", "boolean", modifiers));
        modifiers.clear();

        for (VariablePrototype variable : variables) {
            container.getContainer().addVariable(variable);
        }
        variables.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("primaryStage", "Stage", null));
        methods.add(new MethodPrototype("start", "void", modifiers, variables));
        variables.clear();
        modifiers.clear();

        modifiers.add(PUBLIC);
        methods.add(new MethodPrototype("startWork", "void", modifiers, variables));
        modifiers.clear();

        modifiers.add(PUBLIC);
        methods.add(new MethodPrototype("pauseWork", "void", modifiers, variables));
        modifiers.clear();

        modifiers.add(PUBLIC);
        methods.add(new MethodPrototype("doWork", "boolean", modifiers, variables));
        modifiers.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("textToAppend", "String", null));
        methods.add(new MethodPrototype("appendText", "void", modifiers, variables));
        variables.clear();
        modifiers.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("timeToSleep", "int", null));
        methods.add(new MethodPrototype("sleep", "void", modifiers, variables));
        variables.clear();
        modifiers.clear();

        modifiers.add(PUBLIC);
        methods.add(new MethodPrototype("initLayout", "void", modifiers, variables));
        modifiers.clear();

        modifiers.add(PUBLIC);
        methods.add(new MethodPrototype("initHandlers", "void", modifiers, variables));
        modifiers.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("initPrimaryStage", "Stage", null));
        methods.add(new MethodPrototype("initWindow", "void", modifiers, variables));
        variables.clear();
        modifiers.clear();

        modifiers.add(PUBLIC);
        methods.add(new MethodPrototype("initThreads", "void", modifiers, variables));
        modifiers.clear();

        modifiers.add(PUBLIC);
        modifiers.add(STATIC);
        variables.add(new VariablePrototype("args", "String[]", null));
        methods.add(new MethodPrototype("main", "void", modifiers, variables));
        variables.clear();
        modifiers.clear();

        for (MethodPrototype method : methods) {
            container.getContainer().addMethod(method);
        }
        methods.clear();

        containers.add(container);

        // CounterTask 1
        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("CounterTask");

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("app", "ThreadExample", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("counter", "int", modifiers));
        modifiers.clear();

        for (VariablePrototype variable : variables) {
            container.getContainer().addVariable(variable);
        }
        variables.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("initApp", "ThreadExample", null));
        methods.add(new MethodPrototype("CounterTask", "", modifiers, variables));
        variables.clear();
        modifiers.clear();

        modifiers.add(PROTECTED);
        methods.add(new MethodPrototype("call", "void", modifiers, variables));
        modifiers.clear();

        for (MethodPrototype method : methods) {
            container.getContainer().addMethod(method);
        }
        methods.clear();

        containers.add(container);

        // DateTask 2
        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("DateTask");

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("app", "ThreadExample", modifiers));
        modifiers.clear();

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("now", "Date", modifiers));
        modifiers.clear();

        for (VariablePrototype variable : variables) {
            container.getContainer().addVariable(variable);
        }
        variables.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("initApp", "ThreadExample", null));
        methods.add(new MethodPrototype("CounterTask", "", modifiers, variables));
        variables.clear();
        modifiers.clear();

        modifiers.add(PROTECTED);
        methods.add(new MethodPrototype("call", "void", modifiers, variables));
        modifiers.clear();

        for (MethodPrototype method : methods) {
            container.getContainer().addMethod(method);
        }
        methods.clear();

        containers.add(container);

        // PauseHandler 3
        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("PauseHandler");

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("app", "ThreadExample", modifiers));
        modifiers.clear();

        for (VariablePrototype variable : variables) {
            container.getContainer().addVariable(variable);
        }
        variables.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("initApp", "ThreadExample", null));
        methods.add(new MethodPrototype("PauseHandler", "", modifiers, variables));
        variables.clear();
        modifiers.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("event", "Event", null));
        methods.add(new MethodPrototype("handler", "void", modifiers, variables));
        variables.clear();
        modifiers.clear();

        for (MethodPrototype method : methods) {
            container.getContainer().addMethod(method);
        }
        methods.clear();

        containers.add(container);

        // StartHandler 4
        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("StartHandler");

        modifiers.add(PRIVATE);
        variables.add(new VariablePrototype("app", "ThreadExample", modifiers));
        modifiers.clear();

        for (VariablePrototype variable : variables) {
            container.getContainer().addVariable(variable);
        }
        variables.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("initApp", "ThreadExample", null));
        methods.add(new MethodPrototype("PauseHandler", "", modifiers, variables));
        variables.clear();
        modifiers.clear();

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("event", "Event", null));
        methods.add(new MethodPrototype("handler", "void", modifiers, variables));
        variables.clear();
        modifiers.clear();

        for (MethodPrototype method : methods) {
            container.getContainer().addMethod(method);
        }
        methods.clear();

        containers.add(container);

        // EventHandler (interface) 5
        container = new ContainerPane(INTERFACE, 500, 50, 20, 20);
        container.setName("EventHandler");

        modifiers.add(PUBLIC);
        variables.add(new VariablePrototype("event", "Event", null));
        methods.add(new MethodPrototype("handle", "void", modifiers, variables));
        variables.clear();
        modifiers.clear();

        for (MethodPrototype method : methods) {
            container.getContainer().addMethod(method);
        }
        methods.clear();

        containers.add(container);

        // Application (abstract class) 6
        container = new ContainerPane(ABSTRACT_CLASS, 500, 50, 20, 20);
        container.setName("Application");

        modifiers.add(ABSTRACT);
        variables.add(new VariablePrototype("primaryStage", "Stage", null));
        methods.add(new MethodPrototype("start", "void", modifiers, variables));
        variables.clear();
        modifiers.clear();

        for (MethodPrototype method : methods) {
            container.getContainer().addMethod(method);
        }
        methods.clear();

        containers.add(container);

        // API CLASSES 7-16
        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("Task");
        containers.add(container);

        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("Date");
        containers.add(container);

        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("Platform");
        containers.add(container);

        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("Stage");
        containers.add(container);

        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("BorderPane");
        containers.add(container);

        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("FlowPane");
        containers.add(container);

        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("Button");
        containers.add(container);

        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("ScrollPane");
        containers.add(container);

        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("TextArea");
        containers.add(container);

        container = new ContainerPane(CLASS, 500, 50, 20, 20);
        container.setName("Thread");
        containers.add(container);

        // SET INHERITANCE
        ArrayList<UMLLink> links = new ArrayList();
        links.add(new UMLLink(LinkType.EXTENDS, containers.get(0), containers.get(6))); // ThreadExample extends Application
        links.get(0).split(links.get(0).getLineSegments().get(0));
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(0), containers.get(1))); // ThreadExample aggregate of CounterTask
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(0), containers.get(2))); // ThreadExample aggregate of DateTask
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(0), containers.get(3))); // ThreadExample aggregate of PauseHandler
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(0), containers.get(4))); // ThreadExample aggregate of StartHandler

        links.add(new UMLLink(LinkType.EXTENDS, containers.get(1), containers.get(7))); // CounterTask extends Task
        links.add(new UMLLink(LinkType.DEPENDS, containers.get(1), containers.get(9))); // CounterTask depends on Platform
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(1), containers.get(0))); // CounterTask aggregate of ThreadExample

        links.add(new UMLLink(LinkType.EXTENDS, containers.get(2), containers.get(7))); // DateTask extends Task
        links.add(new UMLLink(LinkType.DEPENDS, containers.get(2), containers.get(9))); // DateTask depends on Platform
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(2), containers.get(0))); // DateTask aggregate of ThreadExample
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(8), containers.get(2))); // Date aggregate of DateTask

        links.add(new UMLLink(LinkType.IMPLEMENTS, containers.get(3), containers.get(5))); // PauseHandler implements EventHandler
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(3), containers.get(0))); // PauseHandler aggregate of ThreadExample

        links.add(new UMLLink(LinkType.IMPLEMENTS, containers.get(4), containers.get(5))); // StartHandler implements EventHandler
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(4), containers.get(0))); // StartHandler aggregate of ThreadExample

        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(10), containers.get(0))); // Stage aggregate of ThreadExample
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(11), containers.get(0))); // BorderPane aggregate of ThreadExample
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(12), containers.get(0))); // FlowPane aggregate of ThreadExample
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(13), containers.get(0))); // Button aggregate of ThreadExample
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(14), containers.get(0))); // ScrollPane aggregate of ThreadExample
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(15), containers.get(0))); // TextArea aggregate of ThreadExample
        links.add(new UMLLink(LinkType.AGGREGATE, containers.get(16), containers.get(0))); // Thread aggregate of ThreadExample

        // PHONY PANE TO INITIAlIZE DATAMANAGER
        Pane phony = new Pane();
        
        // ADD CONTAINERS + Links TO DATAMANAGER
        DataManager dataManager = new DataManager();
        dataManager.setDiagramComponents(phony.getChildren());
        for (ContainerPane currContainer : containers) {
            dataManager.addContainer(currContainer);
        }
        for (UMLLink link : links) {
            dataManager.addLink(link);
        }

        // SAVE FILE
        FileManager fileManager = new FileManager();
        try {
            System.out.println("Starting test save...");
            fileManager.saveData(dataManager, "./work/DesignSaveTest.json");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Done test save.\n");
        
        // LOAD FILE
        try {
            System.out.println("Starting test load...");
            fileManager.loadData(dataManager, "./work/DesignSaveTest.json");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Done test load.\n");
        
        // DATA COMPONENTS TO BE COMPARED -- CHANGE HERE
        ArrayList<String> preLoadStrings = new ArrayList();
        ArrayList<Double> preLoadDoubles = new ArrayList();
        ArrayList<String> postLoadStrings = new ArrayList();
        ArrayList<Double> postLoadDoubles = new ArrayList();
        
        //----------------------------------------------------------------------------------------------------
        // STRINGS
        // 0
        preLoadStrings.add(containers.get(0).getContainer().getName());
        postLoadStrings.add(dataManager.getContainer(0).getContainer().getName());
        
        // 1
        preLoadStrings.add(ContainerType.getString(containers.get(5).getContainer().getType()));
        postLoadStrings.add(ContainerType.getString(dataManager.getContainer(5).getContainer().getType()));
        
        // 2
        preLoadStrings.add(containers.get(0).getContainer().getMethods().get(0).toString());
        postLoadStrings.add(dataManager.getContainer(0).getContainer().getMethods().get(0).toString());
        
        // 3
        preLoadStrings.add(containers.get(1).getContainer().getVariables().get(0).toString());
        postLoadStrings.add(dataManager.getContainer(1).getContainer().getVariables().get(0).toString());
        
        // 4
        preLoadStrings.add(containers.get(3).getContainer().getName());
        postLoadStrings.add(dataManager.getContainer(3).getContainer().getName());
        
        // DOUBLES
        // 0
        preLoadDoubles.add(containers.get(0).getLayoutX());
        postLoadDoubles.add(dataManager.getContainer(0).getLayoutX());
        
        // 1
        preLoadDoubles.add(containers.get(3).getWidth());
        postLoadDoubles.add(dataManager.getContainer(3).getWidth());
        
        // 2
        preLoadDoubles.add(links.get(0).getLineSegments().get(0).getStartX());
        postLoadDoubles.add(dataManager.getLink(0).getLineSegments().get(0).getStartX());
        
        // 3
        preLoadDoubles.add(links.get(0).getLineSegments().get(0).getEndX());
        postLoadDoubles.add(dataManager.getLink(0).getLineSegments().get(0).getEndX());
        
        // 4
        preLoadDoubles.add(links.get(0).getLineSegments().get(1).getEndX());
        postLoadDoubles.add(dataManager.getLink(0).getLineSegments().get(1).getEndX());
        //----------------------------------------------------------------------------------------------------
        
        
        // TEST RESULTS
        compareData(preLoadStrings, postLoadStrings, preLoadDoubles, postLoadDoubles);
        
        System.exit(0);
    }
}
