package hu.d2.offsitesr.ui.model;

import java.io.Serializable;

/**
 * Created by csabinko on 2017.09.21..
 */

public class TicketHolder implements Serializable {

    public static String SERIALIZABLE_NAME = "TicketHolder_Serializable";
    private ServiceRequestEntity entity;
    private int position;
    private boolean changed = false;

    public TicketHolder(ServiceRequestEntity entity, int position) {
        this.entity = entity;
        this.position = position;
    }

    public ServiceRequestEntity getEntity() {

        System.out.println(" TICKETHOlder = "+entity.getTicketId());
        return entity;
    }

    public int getPosition() {
        return position;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}


