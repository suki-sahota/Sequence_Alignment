package sequencealignment.symbol;

import java.util.HashMap;
import java.util.Map;

/* Enumeration for the symbols used in this project.
 * Contains custom value fields, constructor, and
 * getter methods for constant time access. These
 * four letters will be used during the sequence
 * alignment.
 */
public enum Symbol {
    // Symbol Character and index
    A('A', 0),
    C('C', 1),
    G('G', 2),
    T('T', 3);

    // Enumeration fields
    private final Character label;
    private final int index;

    // Map for constant access to Symbols from fields
    private static final Map<Character, Symbol> BY_LABEL = new HashMap<>();
    private static final Map<Integer, Symbol> BY_INDEX = new HashMap<>();

    // Populates when class loads
    static {
        for (Symbol s : values()) {
            BY_LABEL.put(s.label, s);
            BY_INDEX.put(s.index, s);
        }
    }

    // Constructor to add more symbols (if we want to extend problem)
    private Symbol(Character label, int index) {
        this.label = label;
        this.index = index;
    }

    // Customized valueOf() method for label
    public static Symbol valueOfLabel(Character label) {
        return BY_LABEL.get(label);
    }

    // Customized valueOf() method for index
    public static Symbol valueOfIndex(int index) {
        return BY_INDEX.get(index);
    }

    public Character getLabel() {
        return this.label;
    }

    public int getIndex() {
        return this.index;
    }
}
