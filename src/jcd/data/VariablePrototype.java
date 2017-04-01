package jcd.data;

import java.util.ArrayList;
import static jcd.data.Modifier.*;

/**
 *
 * @author Shane
 */
public class VariablePrototype {
    public static final ArrayList<String> PRIMITIVES = new ArrayList(){{
        add("byte"); add("short"); add("int"); add("long"); add("float"); 
        add("double"); add("char"); add("boolean"); add("void");
    }}; 
    
    private String name;
    private String type;
    private ArrayList<Modifier> modifiers;
    
    public VariablePrototype() {
        modifiers = new ArrayList();
    }
    
    public VariablePrototype(String name, String type, ArrayList<Modifier> modifiers) {
        this.name = name;
        this.type = type;
        this.modifiers = new ArrayList();
        if (modifiers != null)
            this.modifiers.addAll(modifiers);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setType(String type) {
        type = type.trim();
        this.type = type;
    }
    
    public String getType() {
        return type;
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
    
    public String toString() {
        String desc = "";
        for (Modifier modifier : modifiers)
            desc += Modifier.getSymbol(modifier);
        desc += name + " : " + type;
        return desc;
    }  
    
    public String umlArgumentString() {
        return name + ": " + type;
    }
    
    public String argumentString() {
        return type + " " + name;
    }
    
    public String exportString() {
        String desc = "";
        // ACCESS MODIFIER
        for (Modifier modifier : modifiers)
            if (modifier == PUBLIC || modifier == PROTECTED || modifier == PRIVATE)
                desc += Modifier.getString(modifier) + " ";
        // OTHER MODIFIERS
        for (Modifier modifier : modifiers)
            if (modifier != PUBLIC && modifier != PROTECTED && modifier != PRIVATE)
                desc += Modifier.getString(modifier) + " ";
        desc += type + " " + name + ";";
        return desc;
    }
}
