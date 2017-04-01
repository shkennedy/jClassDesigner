package jcd.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jcd.data.ContainerType;
import jcd.data.DataManager;
import jcd.data.LinkType;
import jcd.data.MethodPrototype;
import jcd.data.Modifier;
import jcd.data.VariablePrototype;
import jcd.gui.ContainerPane;
import jcd.gui.LineSegment;
import jcd.gui.UMLLink;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;

/**
 *
 * @author Shane
 */
public class FileManager implements AppFileComponent {

    // CONTAINER PANE
    private static final String JSON_CONTAINER_ARRAY = "containers";
    private static final String JSON_CONTAINER_TYPE = "container_type";
    private static final String JSON_CONTAINER_NAME = "container_name";
    private static final String JSON_EXTENDS = "extends";
    private static final String JSON_IMPLEMENTS_ARRAY = "implements";
    private static final String JSON_PACKAGE_NAME = "package_name";
    private static final String JSON_X = "x";
    private static final String JSON_Y = "y";
    private static final String JSON_WIDTH = "width";
    private static final String JSON_HEIGHT = "height";

    // VARIABLE
    private static final String JSON_VARIABLE_ARRAY = "variables";
    private static final String JSON_VARIABLE_NAME = "variable_name";
    private static final String JSON_VARIABLE_TYPE = "variable_type";
    private static final String JSON_VARIABLE_MODIFIER_ARRAY = "variable_modifiers";

    // METHOD
    private static final String JSON_METHOD_ARRAY = "methods";
    private static final String JSON_METHOD_NAME = "method_name";
    private static final String JSON_METHOD_RETURN_TYPE = "return_type";
    private static final String JSON_METHOD_MODIFIER_ARRAY = "method_modifiers";
    private static final String JSON_METHOD_ARGUMENT_ARRAY = "method_arguments";

    // LINK
    private static final String JSON_LINK_ARRAY = "links";
    private static final String JSON_LINK_TYPE = "link_type";
    private static final String JSON_FROM_CONTAINER_NAME = "from_name";
    private static final String JSON_FROM_CONTAINER_PACKAGE = "from_package";
    private static final String JSON_TO_CONTAINER_NAME = "to_name";
    private static final String JSON_TO_CONTAINER_PACKAGE = "to_package";
    private static final String JSON_LINE_SEGMENT_ARRAY = "line_segments";
    private static final String JSON_BREAK_POINT_ARRAY = "break_points";

    public FileManager() {
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager) data;
        JsonArrayBuilder containerAB = Json.createArrayBuilder();
        JsonArrayBuilder linkAB = Json.createArrayBuilder();
  
        // CREATE + ADD DIAGRAM COMPONENTS
        for (ContainerPane container : dataManager.getContainers())
                containerAB.add(makeContainerJsonObject(container));
        for (UMLLink link : dataManager.getLinks())
                linkAB.add(makeUMLLinkJsonObject(link));
  
        // CREATE UNIFIED DATAMANAGER JSONOBJ
        JsonArray containerArray = containerAB.build();
        JsonArray linkArray = linkAB.build();
        JsonObject dataManagerJsonObj = Json.createObjectBuilder().add(JSON_CONTAINER_ARRAY, containerArray)
                .add(JSON_LINK_ARRAY, linkArray).build();

        // WRITE IT TO A JSON FILE
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);
        jsonWriter.writeObject(dataManagerJsonObj);
        jsonWriter.close();

        OutputStream outputStream = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(outputStream);
        jsonFileWriter.writeObject(dataManagerJsonObj);
        String prettyPrinted = stringWriter.toString();
        File checkIfExists = new File(filePath);
        if (checkIfExists.exists()) {
            checkIfExists.delete();
        }
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }

    private JsonObject makeContainerJsonObject(ContainerPane containerPane) {
        JsonArrayBuilder jsonAB = Json.createArrayBuilder();
        JsonObjectBuilder jsonOB = Json.createObjectBuilder();
        // CONTAINER THINGS
        jsonOB.add(JSON_CONTAINER_TYPE, containerPane.getType().ordinal())
                .add(JSON_CONTAINER_NAME, containerPane.getName())
                .add(JSON_PACKAGE_NAME, containerPane.getPackage())
                .add(JSON_X, containerPane.getLayoutX())
                .add(JSON_Y, containerPane.getLayoutY())
                .add(JSON_WIDTH, containerPane.getWidth())
                .add(JSON_HEIGHT, containerPane.getHeight());
        
        // PARENTS
        jsonOB.add(JSON_EXTENDS, containerPane.getContainer().getExtendClass());
        for (String inter : containerPane.getContainer().getImplementInterfaces())
            jsonAB.add(inter); 
        jsonOB.add(JSON_IMPLEMENTS_ARRAY, jsonAB.build());
        jsonAB = Json.createArrayBuilder();
        // VARIABLES
        for (VariablePrototype variable : containerPane.getContainer().getVariables()) {
            jsonAB.add(makeVariableJsonObject(variable));
        }
        jsonOB.add(JSON_VARIABLE_ARRAY, jsonAB.build());
        jsonAB = Json.createArrayBuilder();
        // METHODS
        for (MethodPrototype method : containerPane.getContainer().getMethods()) {
            jsonAB.add(makeMethodJsonObject(method));
        }
        jsonOB.add(JSON_METHOD_ARRAY, jsonAB.build());
        return jsonOB.build();
    }

    private JsonObject makeMethodJsonObject(MethodPrototype method) {
        JsonArrayBuilder jsonAB = Json.createArrayBuilder();
        JsonObjectBuilder jsonOB = Json.createObjectBuilder();
        // NAME + RETURN
        jsonOB.add(JSON_METHOD_NAME, method.getName())
                .add(JSON_METHOD_RETURN_TYPE, method.getReturnType());
        // MODIFIERS
        for (Modifier modifier : method.getModifiers()) {
            jsonAB.add(modifier.ordinal());
        }
        jsonOB.add(JSON_METHOD_MODIFIER_ARRAY, jsonAB.build());
        jsonAB = Json.createArrayBuilder();
        // ARGUMENTS
        for (VariablePrototype variable : method.getArguments()) {
            jsonAB.add(makeVariableJsonObject(variable));
        }
        jsonOB.add(JSON_METHOD_ARGUMENT_ARRAY, jsonAB.build());
        return jsonOB.build();
    }

    private JsonObject makeVariableJsonObject(VariablePrototype variable) {
        JsonArrayBuilder jsonAB = Json.createArrayBuilder();
        JsonObjectBuilder jsonOB = Json.createObjectBuilder();
        // NAME + RETURN
        jsonOB.add(JSON_VARIABLE_NAME, variable.getName())
                .add(JSON_VARIABLE_TYPE, variable.getType());
        // MODIFIERS
        for (Modifier modifier : variable.getModifiers()) {
            jsonAB.add(modifier.ordinal());
        }
        jsonOB.add(JSON_VARIABLE_MODIFIER_ARRAY, jsonAB.build());
        return jsonOB.build();
    }

    private JsonObject makeUMLLinkJsonObject(UMLLink umlLink) {
        JsonArrayBuilder jsonAB = Json.createArrayBuilder();
        JsonObjectBuilder jsonOB = Json.createObjectBuilder();
        // FROM + TO CONTAINERS
        jsonOB.add(JSON_FROM_CONTAINER_NAME, umlLink.getLink().getFromContainer().getName())
                .add(JSON_FROM_CONTAINER_PACKAGE, umlLink.getLink().getFromContainer().getPackage())
                .add(JSON_TO_CONTAINER_NAME, umlLink.getLink().getToContainer().getName())
                .add(JSON_TO_CONTAINER_PACKAGE, umlLink.getLink().getToContainer().getPackage())
                .add(JSON_LINK_TYPE, umlLink.getLink().getType().ordinal());
        // LINE SEGMENTS
        for (LineSegment segment : umlLink.getLineSegments()) {
            jsonAB.add(makeLineSegmentObject(segment));
        }
        jsonOB.add(JSON_LINE_SEGMENT_ARRAY, jsonAB.build());
        return jsonOB.build();
    }

    private JsonObject makeLineSegmentObject(LineSegment segment) {
        JsonArrayBuilder jsonAB = Json.createArrayBuilder();
        JsonObjectBuilder jsonOB = Json.createObjectBuilder();
        // START POINT
        jsonOB.add(JSON_X, segment.getStartPoint().getCenterX())
                .add(JSON_Y, segment.getStartPoint().getCenterY());
        jsonAB.add(jsonOB.build());
        jsonOB = Json.createObjectBuilder();
        // END POINT
        jsonOB.add(JSON_X, segment.getEndPoint().getCenterX())
                .add(JSON_Y, segment.getEndPoint().getCenterY());
        jsonAB.add(jsonOB.build());
        jsonOB = Json.createObjectBuilder();
        jsonOB.add(JSON_BREAK_POINT_ARRAY, jsonAB.build());
        return jsonOB.build();
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager) data;
        dataManager.reset();

        JsonObject json = loadJSONFile(filePath);
        // LOAD CONTAINER PANES + LINKS
        loadContainerPanes(json.getJsonArray(JSON_CONTAINER_ARRAY), dataManager);
        loadLinks(json.getJsonArray(JSON_LINK_ARRAY), dataManager);
    }

    private void loadContainerPanes(JsonArray containerPaneArray, DataManager dataManager) {
        for (int i = 0; i < containerPaneArray.size(); ++i) {
            dataManager.addContainer(loadContainerPane(containerPaneArray.getJsonObject(i)));
        }
    }

    private ContainerPane loadContainerPane(JsonObject jsonContainerPane) {
        ContainerPane container = new ContainerPane(ContainerType.get(jsonContainerPane.getInt(JSON_CONTAINER_TYPE)));
        container.setName(jsonContainerPane.getString(JSON_CONTAINER_NAME));
        container.setPackage(jsonContainerPane.getString(JSON_PACKAGE_NAME));
        container.setLayoutX(jsonContainerPane.getInt(JSON_X));
        container.setLayoutY(jsonContainerPane.getInt(JSON_Y));
        
        container.getContainer().setExtendClass(jsonContainerPane.getString(JSON_EXTENDS));
        JsonArray implementsArray = jsonContainerPane.getJsonArray(JSON_IMPLEMENTS_ARRAY);
        for (int i = 0; i < implementsArray.size(); ++i)
            container.getContainer().addImplementInterface(implementsArray.getString(i));

        JsonArray methods = jsonContainerPane.getJsonArray(JSON_METHOD_ARRAY);
        for (int i = 0; i < methods.size(); ++i) {
            container.getContainer().addMethod(loadMethod(methods.getJsonObject(i)));
        }

        JsonArray variables = jsonContainerPane.getJsonArray(JSON_VARIABLE_ARRAY);
        for (int i = 0; i < variables.size(); ++i) {
            container.getContainer().addVariable(loadVariable(variables.getJsonObject(i)));
        }
        container.update();
        container.setSize(jsonContainerPane.getInt(JSON_WIDTH), jsonContainerPane.getInt(JSON_HEIGHT));
        return container;
    }

    private MethodPrototype loadMethod(JsonObject jsonMethod) {
        MethodPrototype method = new MethodPrototype();
        method.setName(jsonMethod.getString(JSON_METHOD_NAME));
        method.setReturnType(jsonMethod.getString(JSON_METHOD_RETURN_TYPE));

        JsonArray modifiers = jsonMethod.getJsonArray(JSON_METHOD_MODIFIER_ARRAY);
        for (int i = 0; i < modifiers.size(); ++i) {
            method.addModifier(Modifier.get(modifiers.getInt(i)));
        }

        JsonArray arguments = jsonMethod.getJsonArray(JSON_METHOD_ARGUMENT_ARRAY);
        for (int i = 0; i < arguments.size(); ++i) {
            method.addArgument(loadVariable(arguments.getJsonObject(i)));
        }

        return method;
    }

    private VariablePrototype loadVariable(JsonObject jsonVariable) {
        VariablePrototype variable = new VariablePrototype();
        variable.setName(jsonVariable.getString(JSON_VARIABLE_NAME));
        variable.setType(jsonVariable.getString(JSON_VARIABLE_TYPE));

        JsonArray modifiers = jsonVariable.getJsonArray(JSON_VARIABLE_MODIFIER_ARRAY);
        for (int i = 0; i < modifiers.size(); ++i) {
            variable.addModifier(Modifier.get(modifiers.getInt(i)));
        }

        return variable;
    }

    private void loadLinks(JsonArray linkArray, DataManager dataManager) {
        for (int i = 0; i < linkArray.size(); ++i) {
            dataManager.addLink(loadUMLLink(linkArray.getJsonObject(i), dataManager));
        }
    }

    private UMLLink loadUMLLink(JsonObject jsonLink, DataManager dataManager) {
        // MAKE NEW UMLLINK
        UMLLink link = new UMLLink(LinkType.get(jsonLink.getInt(JSON_LINK_TYPE)),
                dataManager.getContainer(jsonLink.getString(JSON_FROM_CONTAINER_NAME), jsonLink.getString(JSON_FROM_CONTAINER_PACKAGE)),
                dataManager.getContainer(jsonLink.getString(JSON_TO_CONTAINER_NAME), jsonLink.getString(JSON_TO_CONTAINER_PACKAGE)));
        // MAKE LINE SEGMENTS
        JsonArray jsonSegments = jsonLink.getJsonArray(JSON_LINE_SEGMENT_ARRAY);
        JsonArray jsonBreakPoints;
        link.getLineSegments().clear();
        if (jsonSegments.size() == 1) {
            link.getLineSegments().add(new LineSegment(link.getLink().getFromContainer(),
                    link.getLink().getToContainer()));
        } else {
            for (int i = 0; i < jsonSegments.size(); ++i) {
                // FIRST SEGMENT - BIND TO fromContainer
                if (i == 0) {
                    link.getLineSegments().add(new LineSegment(link.getLink()
                            .getFromContainer(), null));
                } // MIDDLE SEGMENT - BIND TO NEIGHBOR
                else if (i > jsonSegments.size() - 1) {
                    link.getLineSegments().add(new LineSegment(link.getLineSegments()
                            .get(i - 1).getEndPoint(), null));
                } // END SEGMENT - BIND TO NEIGHBOR AND toContainer
                else {
                    link.getLineSegments().add(new LineSegment(link.getLineSegments()
                            .get(i - 1).getEndPoint(), link.getLink().getToContainer()));
                }
                // SET POSITIONS OF POINTS
                jsonBreakPoints = jsonSegments.getJsonObject(i).getJsonArray(JSON_BREAK_POINT_ARRAY);
                link.getLineSegments().get(i).setLocaions(
                        jsonBreakPoints.getJsonObject(0).getInt(JSON_X),
                        jsonBreakPoints.getJsonObject(0).getInt(JSON_Y),
                        jsonBreakPoints.getJsonObject(1).getInt(JSON_X),
                        jsonBreakPoints.getJsonObject(1).getInt(JSON_Y));
            }
        }
        return link;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    public void exportCode(DataManager dataManager, File selectedFile) throws Exception {
        if (selectedFile.exists()) {
            selectedFile.delete();
        }
        JavaProjectWriter jfw = new JavaProjectWriter(dataManager, selectedFile);
        jfw.write();
    }

    public void saveImage(WritableImage image, File filePath) throws IOException {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", filePath);
    }

    @Override
    public void importData(AppDataComponent data, String filePath) {

    }

    @Override
    public void exportData(AppDataComponent data, String filePath) {

    }
}
