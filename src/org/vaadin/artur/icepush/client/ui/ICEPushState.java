package org.vaadin.artur.icepush.client.ui;

import com.vaadin.terminal.gwt.client.ComponentState;

public class ICEPushState extends ComponentState {
    private String pushGroup = "ICEPush-1";
    private String codeJavascriptLocation = null;

    public String getPushGroup() {
        return pushGroup;
    }

    public void setPushGroup(String pushGroup) {
        this.pushGroup = pushGroup;
    }

    public String getCodeJavascriptLocation() {
        return codeJavascriptLocation;
    }

    public void setCodeJavascriptLocation(String codeJavascriptLocation) {
        this.codeJavascriptLocation = codeJavascriptLocation;
    }

}
