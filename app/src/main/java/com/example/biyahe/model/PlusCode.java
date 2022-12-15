package com.example.biyahe.model;

import java.io.Serializable;

/** A Plus Code encoded location reference. */
public class PlusCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The global Plus Code identifier. */
    public String globalCode;

    /** The compound Plus Code identifier. May be null for locations in remote areas. */
    public String compoundCode;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[PlusCode: ");
        sb.append(globalCode);
        if (compoundCode != null) {
            sb.append(", compoundCode=").append(compoundCode);
        }
        sb.append("]");
        return sb.toString();
    }
}

