/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

/**
 *
 * @author Shane
 */
public enum Modifier {
    PUBLIC,
    PROTECTED,
    PRIVATE,
    STATIC,
    FINAL,
    ABSTRACT,
    SYNCHRONIZED,
    VOLATILE;

    public static Modifier get(int index) {
        switch (index) {
            case 0:
                return PUBLIC;
            case 1:
                return PROTECTED;
            case 2:
                return PRIVATE;
            case 3:
                return STATIC;
            case 4:
                return FINAL;
            case 5:
                return ABSTRACT;
            case 6:
                return SYNCHRONIZED;
            case 7:
                return VOLATILE;
        }
        return null;
    }

    public static String getString(Modifier modifier) {
        switch (modifier) {
            case PUBLIC:
                return "public";
            case PROTECTED:
                return "protected";
            case PRIVATE:
                return "private";
            case STATIC:
                return "static";
            case FINAL:
                return "final";
            case ABSTRACT:
                return "abstract";
            case SYNCHRONIZED:
                return "synchronized";
            case VOLATILE:
                return "volatile";
        }
        return null;
    }
    
    public static String getSymbol(Modifier modifier) {
        switch (modifier) {
            case PUBLIC:
                return "+";
            case PROTECTED:
                return "#";
            case PRIVATE:
                return "-";
            case STATIC:
                return "$";
            case FINAL:
                return "final";
            case ABSTRACT:
                return "{abstract}";
            case SYNCHRONIZED:
                return "{synchronized}";
            case VOLATILE:
                return "{volatile}";
        }
        return null;
    }
}
