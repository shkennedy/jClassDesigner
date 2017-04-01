package jcd.data;

import static jcd.data.Modifier.*;
import java.util.ArrayList;

/**
 *
 * @author Shane
 */
public class MethodPrototype {

    private String name;
    private String returnType;
    private ArrayList<Modifier> modifiers;
    private ArrayList<VariablePrototype> arguments;

    public MethodPrototype() {
        modifiers = new ArrayList();
        arguments = new ArrayList();
    }

    public MethodPrototype(String name, String returnType, ArrayList<Modifier> modifiers, ArrayList<VariablePrototype> arguments) {
        this.name = name;
        this.returnType = returnType;
        this.modifiers = new ArrayList();
        if (modifiers != null) {
            this.modifiers.addAll(modifiers);
        }
        this.arguments = new ArrayList();
        if (arguments != null) {
            this.arguments.addAll(arguments);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnType() {
        return returnType;
    }

    public void addModifier(Modifier modifier) {
        modifiers.add(modifier);
    }

    public void addModifier(int index, Modifier modifier) {
        modifiers.add(index, modifier);
    }

    public void setModifiers(ArrayList<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public ArrayList<Modifier> getModifiers() {
        return modifiers;
    }

    public void addArgument(VariablePrototype argument) {
        arguments.add(argument);
    }

    public void setArguments(ArrayList<VariablePrototype> arguments) {
        this.arguments = arguments;
    }

    public void removeLastArgument() {
        arguments.remove(arguments.size() - 1);
    }

    public ArrayList<VariablePrototype> getArguments() {
        return arguments;
    }

    public String toString() {
        String desc = "";
        for (Modifier modifier : modifiers) {
            desc += Modifier.getSymbol(modifier);
        }
        desc += name + "(";
        for (int i = 0; i < arguments.size(); ++i) {
            desc += arguments.get(i).umlArgumentString();
            if (i < arguments.size() - 1) {
                desc += ", ";
            }
        }
        desc += ")";
        if (!returnType.equals("")) {
            desc += " : " + returnType;
        }
        return desc;
    }

    public String exportString() {
        String desc = "\t";
        // ACCESS MODIFIER
        for (Modifier modifier : modifiers) {
            if (modifier == PUBLIC || modifier == PROTECTED || modifier == PRIVATE) {
                desc += Modifier.getString(modifier) + " ";
            }
        }
        // OTHER MODIFIERS
        for (Modifier modifier : modifiers) {
            if (modifier != PUBLIC && modifier != PROTECTED && modifier != PRIVATE) {
                desc += Modifier.getString(modifier) + " ";
            }
        }
        desc += returnType + " " + name + "(";
        // ARGUMENTS
        for (int i = 0; i < arguments.size(); ++i) {
            desc += arguments.get(i).argumentString();
            if (i < arguments.size() - 1) {
                desc += ", ";
            }
        }
        desc += ") {\n";
        // RETURN STATEMENT
        if (VariablePrototype.PRIMITIVES.contains(returnType)) {
            if (returnType.equals("void")) {
            } else if (returnType.equals("boolean")) {
                desc += "\t\treturn false;";
            } else {
                desc += "\t\treturn 0;";
            }
        }
        else {
            desc += "\t\treturn null;";
        }
        desc += "\n\t}";
        return desc;
    }
}
