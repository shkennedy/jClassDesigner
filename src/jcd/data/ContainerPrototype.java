package jcd.data;

import java.util.ArrayList;
import static jcd.data.ContainerType.*;

/**
 *
 * @author Shane
 */
public class ContainerPrototype {
    String name;
    ContainerType type;
    String packageName;
    String extendClass;
    ArrayList<String> implementInterfaces;
    ArrayList<MethodPrototype> methods;
    ArrayList<VariablePrototype> variables;
    
    public ContainerPrototype(ContainerType type) {
        this.type = type;
        if (type == CLASS)
            name = "NewClass";
        else if (type == INTERFACE)
            name = "NewInterface";
        packageName = "";
        extendClass = "";
        implementInterfaces = new ArrayList();
        methods = new ArrayList();
        variables = new ArrayList();
    }
    
    public ContainerType getType() {
        return type;
    }
    
    public void setType(ContainerType newType) {
        type = newType;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPackagePath() {
        return packageName.replace(".", "/");
    }
    
    public String getPackage() {
        return packageName;
    }
    
    public void setPackage(String packageName) {
        this.packageName = packageName;
    }
    
    public String getExtendClass() {
        return extendClass;
    }
    
    public void setExtendClass(String newExtension) {
        extendClass = newExtension;
    }
    
    public ArrayList<String> getImplementInterfaces() {
        return implementInterfaces;
    }
    
    public void addImplementInterface(String parent) {
        implementInterfaces.add(parent);
    }
    
    public ArrayList<MethodPrototype> getMethods() {
        return methods;
    }
    
    public void addMethod(MethodPrototype method) {
        methods.add(method);
    }
    
    public void removeMethod(MethodPrototype method) {
        methods.remove(method);
    }
    
    public ArrayList<VariablePrototype> getVariables() {
        return variables;
    }
    
    public void addVariable(VariablePrototype variable) {
        variables.add(variable);
    }
    
    public void removeVariable(VariablePrototype variable) {
        variables.remove(variable);
    }
}

