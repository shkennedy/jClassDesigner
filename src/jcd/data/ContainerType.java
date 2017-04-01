package jcd.data;

/**
 *
 * @author Shane
 */
public enum ContainerType {
    CLASS, 
    ABSTRACT_CLASS, 
    INTERFACE, 
    ENUMERATION;
    
    public static ContainerType get(int index) {
        switch (index) {
            case 0:
                return CLASS;
            case 1:
                return ABSTRACT_CLASS;
            case 2:
                return INTERFACE;
            case 3:
                return ENUMERATION;
        }
        return null;
    }
    
    public static String getString(ContainerType type) {
        switch (type) {
            case CLASS:
                return "class";
            case ABSTRACT_CLASS:
                return "abstract class";
            case INTERFACE:
                return "interface";
            case ENUMERATION:
                return "enum";
        }
        return null;
    }
}
