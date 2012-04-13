package org.vaadin.artur.icepush.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class InvisibleWidget extends Widget {

    public InvisibleWidget() {
        setElement(Document.get().createDivElement());
        setVisible(false);
    }
}
