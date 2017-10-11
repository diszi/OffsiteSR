package hu.d2.offsitesr.app.singleton;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.d2.offsitesr.R;

/**
 * Created by csabinko on 2017.09.19..
 */

public class HolderSingleton {
    private static final HolderSingleton ourInstance = new HolderSingleton();

    private static String OWNER = "Owner";
    private static String OWNER_GROUP = "OwnerGroup";

    private HashMap<String, String> ticketStatuses;
    private HashMap<String, String> taskStatuses;
    private Map<String, String> owners;
    private Map<String, String> ownerGroups;
    private Map<String, String> priorities;
    private Map<String, String> prioritiesHelper;

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public static HolderSingleton getInstance() {
        return ourInstance;
    }

    private HolderSingleton() {
    }

    public HashMap<String, String> getTicketStatuses() {
        if (ticketStatuses == null){
            createTicketStatusMap();
        }
        return ticketStatuses;
    }

    public HashMap<String, String> getTaskStatuses() {
        if (taskStatuses == null){
            createTaskStatusMap();
        }
        return taskStatuses;
    }

    public Map<String, String> getOwnerGroups() {
        return ownerGroups;
    }

    public Map<String, String> getOwners() {
        return owners;
    }

    public void setOwnerGroups(List<String> ownerGroupsList) {
        ownerGroups = setMap(OWNER_GROUP,ownerGroupsList);
    }

    public void setOwners(List<String> ownersList) {
        owners = setMap(OWNER,ownersList);
    }

    private Map<String, String> setMap(String prefix,List<String> inputList) {
        Map<String, String> result = new HashMap<>();
        for (String name : inputList) {
            result.put(createID(prefix,name),name);
        }
        return result;
    }

    private static String createID(String prefix, String name) {
        return "compDialog_" + prefix + "_" + name;
    }

    public Map<String, String> getPriorities() {
        if (priorities==null){
            createPriorityMaps();
        }
        return priorities;
    }

    public String getPriorityValue(String id){
        if (priorities==null){
            createPriorityMaps();
        }
        return priorities.get(id);
    }

    public String getPriorityId(String id){
        if (prioritiesHelper==null){
            createPriorityMaps();
        }
        return prioritiesHelper.get(id);
    }

    public void createTicketStatusMap(){
        ticketStatuses = new HashMap<>();
        if (context!=null){
            String[] statusKeys = context.getResources().getStringArray(R.array.ticket_status_keys);
            String[] statusValues = context.getResources().getStringArray(R.array.ticket_status_values);

            for (int i = 0; i<statusKeys.length;i++){
                ticketStatuses.put(statusKeys[i],statusValues[i]);
            }

        }
    }

    public void createTaskStatusMap(){
        taskStatuses = new HashMap<>();
        if (context!=null){
            String[] statusKeys = context.getResources().getStringArray(R.array.task_status_keys);
            String[] statusValues = context.getResources().getStringArray(R.array.task_status_values);

            for (int i = 0; i<statusKeys.length;i++){
                taskStatuses.put(statusKeys[i],statusValues[i]);
            }

        }
    }

    public void createPriorityMaps(){
        priorities = new HashMap<>();
        prioritiesHelper = new HashMap<>();
        if (context!=null){
            String[] priorityId = context.getResources().getStringArray(R.array.priority_ids);
            String[] priorityValues = context.getResources().getStringArray(R.array.priority_values);

            for (int i = 0; i<priorityId.length;i++){
                priorities.put(priorityId[i],priorityValues[i]);
                prioritiesHelper.put(priorityValues[i],priorityId[i]);
            }

        }
    }


}
