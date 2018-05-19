package org.latheild.dt.dto;

public class BatchTestCaseDTO {

    private String filePath;

    private String className;

    private String[] options;

    private String xlsPath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getXlsPath() {
        return xlsPath;
    }

    public void setXlsPath(String xlsPath) {
        this.xlsPath = xlsPath;
    }
}
