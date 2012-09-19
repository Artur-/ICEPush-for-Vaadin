package org.vaadin.artur.icepush.client.ui;

import com.vaadin.shared.communication.ServerRpc;



public interface ICEPushRpc extends ServerRpc {
    public void push();
}
