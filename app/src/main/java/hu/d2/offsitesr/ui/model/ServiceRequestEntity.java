package hu.d2.offsitesr.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by csabinko on 2017.09.15..
 */

public class ServiceRequestEntity implements Serializable{

    public static String SERIALIZABLE_NAME = "ServiceRequest_Serializable";

    private String affectedPerson;
    private String ticketClass;
    private String description;
    private String ownerGroup;
    private String owner;
    private String reportedBy;
    private String reportDate;
    private String status;
    private String statusDate;
    private String ticketId;
    private String notes;
    private String classStructure;
    private String priority;
    private List<WorkLog> workLogs;


    public ServiceRequestEntity() {
        // empty
    }

    public String getAffectedPerson() {
        return affectedPerson;
    }

    public void setAffectedPerson(String affectedPerson) {
        this.affectedPerson = affectedPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerGroup() {
        return ownerGroup;
    }

    public void setOwnerGroup(String ownerGroup) {
        this.ownerGroup = ownerGroup;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    public void setTicketClass(String ticketClass) {
        this.ticketClass = ticketClass;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<WorkLog> getWorkLogs() {
        return workLogs;
    }

    public void setWorkLogs(List<WorkLog> workLogs) {
        this.workLogs = workLogs;
    }

    public String getClassStructure() {
        return classStructure;
    }

    public void setClassStructure(String classStructure) {
        this.classStructure = classStructure;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
