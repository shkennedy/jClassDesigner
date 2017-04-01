package jcd.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.scene.Node;
import jcd.data.ContainerPrototype;
import jcd.data.ContainerType;
import jcd.data.DataManager;
import jcd.data.MethodPrototype;
import jcd.data.VariablePrototype;
import jcd.gui.ContainerPane;

/**
 *
 * @author Shane
 */
public class JavaProjectWriter {

    ArrayList<ContainerPrototype> containersToMake;
    File projectDirectory;

    protected JavaProjectWriter(DataManager dataManager, File projectDirectory) {
        containersToMake = new ArrayList();
        for (Node component : dataManager.getDiagramComponents()) {
            if (component instanceof ContainerPane) {
                if (verifyNotAPI(((ContainerPane) component).getContainer())) {
                    containersToMake.add(((ContainerPane) component).getContainer());
                }
            }
        }
        this.projectDirectory = projectDirectory;
    }

    private boolean verifyNotAPI(ContainerPrototype container) {
        for (Package apiPackage : Package.getPackages()) {
            try {
                if (Class.forName(apiPackage.getName() + "." + container.getName()) != null) {
                    return false;
                }
            } catch (ClassNotFoundException cnfe) {
            }
        }
        return true;
    }

    // Controller for writing
    protected void write() throws IOException, Exception {
        makeDirectories();
        for (ContainerPrototype container : containersToMake) {
            writeContainerFile(container);
        }
    }

    private void makeDirectories() throws IOException {
        projectDirectory.mkdir();
        File directoryPath;
        for (ContainerPrototype container : containersToMake) {
            directoryPath = new File(projectDirectory.getAbsolutePath() + "/"
                    + container.getPackagePath());
            directoryPath.mkdirs();
        }
    }

    private void writeContainerFile(ContainerPrototype container) throws IOException {
        File containerFile = new File(projectDirectory.getAbsolutePath() + "/"
                + container.getPackagePath() + "/" + container.getName() + ".java");
        PrintWriter pw = new PrintWriter(containerFile);

        // WRITE PACKAGE
        if (container.getPackage().equals("")) {
            pw.println("package " + projectDirectory.getName() + ";\n");
        } else {
            pw.println("package " + container.getPackage() + ";\n");
        }

        // WRITE IMPORTS
        writeImports(container, pw);

        // WRITE HEADER
        writeHeader(container, pw);

        // WRITE MEMBER VARIABLES
        for (VariablePrototype memberVar : container.getVariables()) {
            pw.print("\t" + memberVar.exportString() + "\n");
        }
        pw.print("\n");

        // WRITE METHODS
        for (MethodPrototype method : container.getMethods()) {
            pw.print(method.exportString() + "\n\n");
        }

        pw.print("}");
        pw.close();
    }

    private void writeHeader(ContainerPrototype container, PrintWriter pw) {
        pw.print("public " + ContainerType.getString(container.getType()) + " "
                + container.getName() + " ");
        if (!container.getExtendClass().equals("")) {
            pw.print("extends " + container.getExtendClass() + " ");
        }
        if (!container.getImplementInterfaces().isEmpty()) {
            pw.print("implements ");
            ArrayList<String> interfaces = container.getImplementInterfaces();
            for (int i = 0; i < interfaces.size(); ++i) {
                pw.print(interfaces.get(i) + " " + ((i < interfaces.size() - 1)? ", " : " "));
            }
        }
        pw.println("{\n");
    }

    private void writeImports(ContainerPrototype container, PrintWriter pw) {
        String path;
        String importPaths = "";
        // MEMBER VARIABLES
        for (VariablePrototype member : container.getVariables()) {
            if (!VariablePrototype.PRIMITIVES.contains(member.getType())
                    && !member.getType().equals(container.getName())) { // NEED IMPORT
                if (!(path = findImportPath(container, member.getType())).equals("")) {
                    if (!importPaths.contains(path)) {
                        importPaths += "import " + path + ";\n";
                    }
                }
            }
        }
        // METHODS
        for (MethodPrototype method : container.getMethods()) {
            if (!VariablePrototype.PRIMITIVES.contains(method.getReturnType())
                    && !method.getReturnType().equals(container.getName())) { // NEED IMPORT
                if (!(path = findImportPath(container, method.getReturnType())).equals("")) {
                    if (!importPaths.contains(path)) {
                        importPaths += "import " + path + ";\n";
                    }
                }
            }
        }
        pw.print(importPaths + "\n");
    }

    private String findImportPath(ContainerPrototype container, String type) {
        String importPath = "";
        // SEARCH API
        Class foundClass;
        for (Package apiPackage : Package.getPackages()) {
            if (!apiPackage.getName().equals("java.lang")) {
                try {
                    foundClass = Class.forName(apiPackage.getName() + "." + type);
                    return foundClass.getName();
                } catch (ClassNotFoundException cnfe) {
                }
            }
        }
        // SEARCH PROJECT
        for (ContainerPrototype currContainer : containersToMake) {
            if (type.equals(currContainer.getName())) {
                if (currContainer.getPackagePath().equals(container.getPackagePath())) {
                    break;
                }
                importPath = currContainer.getPackage() + "." + currContainer.getName();
            }
        }
        if (!importPath.equals("")) {
            return importPath;
        }
        return importPath;
    }
}
