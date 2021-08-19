package com.mirumagency.uhc.premember.core.models;

public interface Layout {

    /**
     * Returns whether the column/component child of a column should be the
     * same height as the tallest column in the grid, based on a property of
     * the parent grid.
     *
     * @return true if the column should be the same height as its siblings; false otherwise
     */
    boolean isSameHeight();

}
