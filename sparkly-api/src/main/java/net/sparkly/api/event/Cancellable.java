package net.sparkly.api.event;

public class Cancellable {

    private boolean cancelled;
    
    public boolean cancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public void cancel() {
        setCancelled(true);
    }
}
