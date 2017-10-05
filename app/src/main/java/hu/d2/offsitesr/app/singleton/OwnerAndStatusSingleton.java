package hu.d2.offsitesr.app.singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by csabinko on 2017.09.19..
 */

public class OwnerAndStatusSingleton {
    private static final OwnerAndStatusSingleton ourInstance = new OwnerAndStatusSingleton();

    private static String STATUS = "Status";
    private static String OWNER = "Owner";
    private static String OWNER_GROUP = "OwnerGroup";

    private Map<String, String> statuses ;
    private Map<String, String> owners;
    private Map<String, String> ownerGroups;

    public static OwnerAndStatusSingleton getInstance() {
        return ourInstance;
    }

    private OwnerAndStatusSingleton() {
    }

    public Map<String, String> getStatuses() {
        return statuses;
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

    public void setStatuses(List<String> statusesList) {
        statuses = setMap(STATUS,statusesList);
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

}
