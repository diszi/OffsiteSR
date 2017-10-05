package hu.d2.offsitesr.ui.model;

import java.util.List;

/**
 * Created by csabinko on 2017.10.05..
 */

public class OwnerHolder {

    private List<String> ownerList;
    private List<String> ownerGroupList;

    public OwnerHolder(List<String> ownerList, List<String> ownerGroupList) {
        this.ownerList = ownerList;
        this.ownerGroupList = ownerGroupList;
    }

    public List<String> getOwnerList() {
        return ownerList;
    }

    public List<String> getOwnerGroupList() {
        return ownerGroupList;
    }
}
