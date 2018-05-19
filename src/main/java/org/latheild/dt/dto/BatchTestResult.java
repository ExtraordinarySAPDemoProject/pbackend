package org.latheild.dt.dto;

import java.util.List;

public class BatchTestResult {

    private List<BatchTestSheetResult> sheets;

    public List<BatchTestSheetResult> getSheets() {
        return sheets;
    }

    public void setSheets(List<BatchTestSheetResult> sheets) {
        this.sheets = sheets;
    }

    @Override
    public String toString() {
        return "BatchTestResult{" +
                "sheets=" + sheets +
                '}';
    }

}
