package com.albert.vaadin_task.views;

import com.albert.vaadin_task.views.components.UserEditor;
import com.albert.vaadin_task.model.User;
import com.albert.vaadin_task.repository.UserRepository;
import com.albert.vaadin_task.security.SecurityService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Route("/admin")
@RolesAllowed("ROLE_ADMIN")
public class AdminView extends VerticalLayout {

    private UserRepository repo;
    private SecurityService securityService;

    private final Grid<User> grid;
    private final TextField filter = new TextField("", "Type to filter");
    private final Button newButton = new Button("Add new");
    private final Button logout = new Button("Logout", click -> securityService.logout());
    private final TextField edit = new TextField("", "ID to edit");
    private final Button editButton = new Button("Edit user with ID");
    private final HorizontalLayout logoutLayout = new HorizontalLayout(logout);
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, newButton);
    private final HorizontalLayout editToolbar = new HorizontalLayout(edit, editButton);
    private final UserEditor editor;

    @Autowired
    public AdminView(UserRepository repo, UserEditor editor, SecurityService securityService) {
        this.repo = repo;
        this.editor = editor;
        this.securityService = securityService;
        this.grid = new Grid<>(User.class);

        // build layout

        add(logoutLayout, toolbar, editToolbar, grid, editor);

        logoutLayout.setAlignItems(Alignment.END);

        grid.setHeight("300px");
        grid.setColumns("id", "lastName", "firstName", "patronymic", "birthDate", "email", "phoneNumber");
        grid.getColumnByKey("id").setWidth("70px").setFlexGrow(0);
        grid.getColumnByKey("email").setWidth("300px").setFlexGrow(0);

        filter.setPlaceholder("Filter by name");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listUsers(e.getValue()));

        // Connect selected User to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editUser(e.getValue());
        });

        // Instantiate and edit new User the new button is clicked
        newButton.addClickListener(event -> editor.editUser(new User()));
        editButton.addClickListener(event -> goToEditUser());
        edit.addKeyPressListener(Key.ENTER, event -> goToEditUser());

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listUsers(filter.getValue());
        });

        // Initialize listing
        listUsers("");
    }

    private void goToEditUser() {
        if (!edit.isEmpty()) {
            Optional<User> optional = repo.findById(Long.parseLong(edit.getValue()));
            if (optional.isPresent()) {
                editor.editUser(optional.get());
            } else {
                Notification.show("Wrong ID");
            }
        } else {
            Notification.show("Enter ID to edit");
        }
    }

    private void listUsers(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(repo.findByName(filterText));
        } else {
            grid.setItems(repo.findAll());
        }
    }
}
