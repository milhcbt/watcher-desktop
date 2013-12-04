/*
 * Copyright belong to www.codencare.com and its client.
 * for more information contact imanlhakim@gmail.com
 */
package com.codencare.watcher.controller.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this Exception class when integrity of entity violated.
 * @author Iman L Hakim <imanlhakim at gmail.com>
 */
public class IllegalOrphanException extends Exception {
    private final List<String> messages;
    public IllegalOrphanException(List<String> messages) {
        super((messages != null && messages.size() > 0 ? messages.get(0) : null));
        if (messages == null) {
            this.messages = new ArrayList<>();
        }
        else {
            this.messages = messages;
        }
    }
    public List<String> getMessages() {
        return messages;
    }
}
