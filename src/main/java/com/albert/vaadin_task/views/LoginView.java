package com.albert.vaadin_task.views;

import com.albert.vaadin_task.repository.UserRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/login")
@PermitAll
public class LoginView extends VerticalLayout {

    private final LoginForm loginForm;
    private final UserRepository repo;

    @Autowired
    public LoginView(UserRepository repo) {
        this.repo = repo;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.setAction("login");

        Component text = new Text("Username is a concatenation of lastname, firstname and patronymic without delimiter");

        add(text, loginForm);
    }


}
