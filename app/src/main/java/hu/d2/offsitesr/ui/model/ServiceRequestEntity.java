package hu.d2.offsitesr.ui.model;

import java.io.Serializable;

import java.util.List;

import hu.d2.offsitesr.util.EnvironmentTool;

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
    private String assetNum;
    private String ciNum;
    private String statusDate;
    private String ticketId;
    private String notes;
    private String classStructure;
    private String priority;
    private List<WorkLog> workLogs;
    private List<Task> tasks;
    private Asset asset;

    private List<Attachment> attachments;


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

    public String getReportDate()
    {
        return EnvironmentTool.convertDateString(reportDate);

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

    public String getAssetNum() {

        return assetNum;
    }

    public void setAssetNum(String asset) {
        this.assetNum =asset;
    }


    public String getCINum() {
        return ciNum;
    }

    public void setCINum(String ci) {
        this.ciNum =ci;
    }



    public String getStatusDate() {

        return EnvironmentTool.convertDateString( statusDate);
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

    public List<Task> getTasks() {
        return tasks;
    }


    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }



    public Asset getAsset(){
        return asset;
    }

    public void setAsset(Asset asset){
        this.asset=asset;
    }


}
