package hu.d2.offsitesr.ui.model;

import java.io.Serializable;

/**
 * Created by csabinko on 2017.09.15..
 */

public class WorkLog implements Serializable{

    public static String SERIALIZABLE_NAME = "WorkLog_Serializable";

    private String id;
    private String recordKey;
    private String createdBy;
    private String createdDate;
    private String logType;
    private String description;


    public String getRecordKey() {
        return recordKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
