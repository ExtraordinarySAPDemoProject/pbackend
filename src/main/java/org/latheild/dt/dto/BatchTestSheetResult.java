package org.latheild.dt.dto;

import java.util.List;

public class BatchTestSheetResult {

    private List<BatchTestRowResult> rows;

    private int successCount;

    private int totalCount;

    public List<BatchTestRowResult> getRows() {
        return rows;
    }

    public void setRows(List<BatchTestRowResult> rows) {
        this.rows = rows;
    }

    public void addRow(BatchTestRowResult rowResult) {
        this.rows.add(rowResult);
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "BatchTestSheetResult{" +
                "rows=" + rows +
                '}';
    }

}
