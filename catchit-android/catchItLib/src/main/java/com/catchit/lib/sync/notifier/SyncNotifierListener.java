package com.catchit.lib.sync.notifier;

/**
 * Lister for SyncNotifier events
 */
public interface SyncNotifierListener {

    /**
     * Called whenever an sync operation needed (defined by SyncNotifier.Builder class)
     */
    void onSyncNeeded();
}
