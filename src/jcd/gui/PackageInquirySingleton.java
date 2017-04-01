package jcd.gui;

import java.net.URL;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Shane
 */
public class PackageInquirySingleton extends Stage {
    
    private static PackageInquirySingleton singleton;
    
    private String packagePath;
    
    private Scene inquiryScene;
    private VBox inquiryPane;
    private Label inquiryLabel;
    private TextField packageTF;
    private Button applyButton;
    
    private PackageInquirySingleton() {}
    
    public static PackageInquirySingleton getSingleton() {
        if (singleton == null)
            singleton = new PackageInquirySingleton();
        return singleton;
    }
    
    public void init(Stage owner) {
        setTitle("Package Inquiry");
        initModality(Modality.WINDOW_MODAL);
        initOwner(owner);
        
        packagePath = "";
        
        inquiryLabel = new Label();
        packageTF = new TextField();
        applyButton = new Button("Apply");
        inquiryPane = new VBox();
        inquiryPane.setAlignment(Pos.CENTER);
        inquiryPane.getChildren().add(inquiryLabel);
        inquiryPane.getChildren().add(packageTF);
        inquiryPane.getChildren().add(applyButton);
        
        inquiryScene = new Scene(inquiryPane);
        setScene(inquiryScene);
        
        setupHandlers();
        initStyle();
    }
    
    public void show(String className) {
        inquiryLabel.setText("Please enter package path for '" + className + "'");
        packageTF.setText("");
        packagePath = "";
        showAndWait();
    }
    
    public String getPackage() {
        return packagePath;
    }
    
    private void setupHandlers() {
        applyButton.setOnAction(e -> {
            packagePath = packageTF.getText();
            PackageInquirySingleton.this.close();
        });
    }
    
    private void initStyle() {
        URL stylesheetURL = getClass().getResource("../css/jcd_style.css");
        String stylesheetPath = stylesheetURL.toExternalForm();
        inquiryScene.getStylesheets().add(stylesheetPath);
        inquiryPane.getStyleClass().add("components_toolbar");
    }
}
