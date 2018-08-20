package com.modelink.admin.bean;

import javax.persistence.Id;
import java.io.Serializable;

public class ExceptionLogger implements Serializable {

    @Id
    private Long id;

    private String loggerKey;
    private String loggerDate;
    private String loggerDesc;
    private String loggerType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoggerKey() {
        return loggerKey;
    }

    public void setLoggerKey(String loggerKey) {
        this.loggerKey = loggerKey;
    }

    public String getLoggerDate() {
        return loggerDate;
    }

    public void setLoggerDate(String loggerDate) {
        this.loggerDate = loggerDate;
    }

    public String getLoggerDesc() {
        return loggerDesc;
    }

    public void setLoggerDesc(String loggerDesc) {
        this.loggerDesc = loggerDesc;
    }

    public String getLoggerType() {
        return loggerType;
    }

    public void setLoggerType(String loggerType) {
        this.loggerType = loggerType;
    }
}
