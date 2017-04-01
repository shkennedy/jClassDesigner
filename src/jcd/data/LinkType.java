package jcd.data;

/**
 *
 * @author Shane
 */
public enum LinkType {
        EXTENDS, IMPLEMENTS, COMPOSED, AGGREGATE, DEPENDS, ASSOCIATED;

        public static LinkType get(int index) {
            switch (index) {
                case 0:
                    return EXTENDS;
                case 1:
                    return IMPLEMENTS;
                case 2:
                    return COMPOSED;
                case 3:
                    return AGGREGATE;
                case 4:
                    return DEPENDS;
                case 5:
                    return ASSOCIATED;
            }
            return null;
        }
        
        public static boolean compare(LinkType firstLink, LinkType secondLink) {
            return firstLink.ordinal() > secondLink.ordinal();
        }
    }
