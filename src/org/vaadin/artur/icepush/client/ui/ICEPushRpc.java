package org.vaadin.artur.icepush.client.ui;

import com.vaadin.terminal.gwt.client.communication.ServerRpc;

public interface ICEPushRpc extends ServerRpc {
    public void push();
}
