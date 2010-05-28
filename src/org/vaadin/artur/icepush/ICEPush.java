package org.vaadin.artur.icepush;

import java.util.Map;

import org.icepush.PushContext;
import org.vaadin.artur.icepush.client.ui.VICEPush;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VICEPush widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.artur.icepush.client.ui.VICEPush.class)
public class ICEPush extends AbstractComponent {

    public static final String PUSH_GROUP = "ICEPush-1";

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.addAttribute(VICEPush.PUSH_URL, "/abc");
        target.addAttribute(VICEPush.PUSH_GROUP, PUSH_GROUP);
    }

    /**
     * Receive and handle events and other variable changes from the client.
     * 
     * {@inheritDoc}
     */
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        // No need to handle event
    }

    public void push() {
        Application app = getApplication();
        if (app == null) {
            throw new RuntimeException(
                    "Must be attached to an application to push");
        }

        // Push changes
        ApplicationContext context = app.getContext();
        if (context instanceof WebApplicationContext) {
            PushContext pushContext = PushContext
                    .getInstance(((WebApplicationContext) context)
                            .getHttpSession().getServletContext());
            pushContext.push(ICEPush.PUSH_GROUP);
        } else {
            throw new RuntimeException("Only servlet deployment is supported");
        }
    }
}
