package org.latheild.dt.dto;

import java.util.List;

public class BatchTestRowResult {

    private List<Object> cells;

    public List<Object> getCells() {
        return cells;
    }

    public void setCells(List<Object> cells) {
        this.cells = cells;
    }

    public void addCell(Object ele) {
        this.cells.add(ele);
    }

    @Override
    public String toString() {
        return "BatchTestRowResult{" +
                "cells=" + cells +
                '}';
    }

}
