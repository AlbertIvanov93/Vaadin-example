package com.albert.vaadin_task.views;

import com.albert.vaadin_task.model.User;
import com.albert.vaadin_task.repository.UserRepository;
import com.albert.vaadin_task.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/user")
@RolesAllowed("ROLE_USER")
public class UserView extends VerticalLayout {
    private UserRepository repo;
    private SecurityService securityService;

    private Grid<User> grid;
    private Button logout = new Button("Logout", click -> securityService.logout());
    private HorizontalLayout logoutLayout = new HorizontalLayout(logout);

    @Autowired
    public UserView(UserRepository repo, SecurityService securityService) {
        this.grid = new Grid<>(User.class);
        this.repo = repo;
        this.securityService = securityService;

        grid.setHeight("300px");
        grid.setColumns("id", "lastName", "firstName", "patronymic", "birthDate", "email", "phoneNumber");
        grid.getColumnByKey("id").setWidth("70px").setFlexGrow(0);
        grid.getColumnByKey("email").setWidth("300px").setFlexGrow(0);

        logoutLayout.setAlignItems(Alignment.END);

        add(logoutLayout, grid);

        showUser();
    }

    private void showUser() {
        User user = (User) securityService.getAuthenticatedUser();
        if (user != null) {
            grid.setItems(user);
        } else {
            grid.setItems(repo.findById(1L).get());
        }
    }
}
