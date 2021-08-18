package com.bwapp.culpeper.security;

import com.bwapp.culpeper.view.AdminView;
import com.bwapp.culpeper.view.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!SecurityUtils.isUserLoggedIn()
                && AdminView.class.equals(event.getNavigationTarget())) {
            event.rerouteTo(LoginView.class);
        }
    }
}
