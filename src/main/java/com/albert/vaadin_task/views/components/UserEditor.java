package com.albert.vaadin_task.views.components;

import com.albert.vaadin_task.model.User;
import com.albert.vaadin_task.repository.UserRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDate;

@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {
    private final UserRepository repo;

    /**
     * The currently edited user
     */
    private User user;

    /* Fields to edit properties in User entity */
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final TextField patronymic = new TextField("Patronymic");
    private final TextField email = new TextField("Email");
    private final TextField phoneNumber = new TextField("Phone number");
    private final TextField birthYear = new TextField("Birth year");
    private final TextField birthMonth = new TextField("Birth month");
    private final TextField birthDayOfMonth = new TextField("Birth day of month");


    /* Action buttons */
    private final Button save = new Button("Save", VaadinIcon.CHECK.create());
    private final Button cancel = new Button("Cancel");
    private final Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    private final HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private final HorizontalLayout fullName = new HorizontalLayout(lastName, firstName, patronymic);
    private final HorizontalLayout birthDate = new HorizontalLayout(birthDayOfMonth, birthMonth, birthYear);

    private final Binder<User> binder = new Binder<>(User.class);

    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public UserEditor(UserRepository repo) {
        this.repo = repo;

        add(fullName, birthDate, email, phoneNumber, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());
        setVisible(false);
    }

    public void cancel() {
        setVisible(false);
    }

    public void save() {
        user.setBirthDate(new Date(Integer.parseInt(birthYear.getValue()) - 1900,
                Integer.parseInt(birthMonth.getValue()) - 1,
                Integer.parseInt(birthDayOfMonth.getValue())));
        repo.save(user);
        changeHandler.onChange();
    }

    public void delete() {
        repo.delete(user);
        changeHandler.onChange();
    }

    public void editUser(User newUser) {
        if (newUser == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = newUser.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            // In a more complex app, you might want to load
            // the entity/DTO with lazy loaded relations for editing
            user = repo.findById(newUser.getId()).get();
        } else {
            user = newUser;
        }
        //cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(user);
        if (user.getBirthDate() != null) {
            LocalDate bd = user.getBirthDate().toLocalDate();
            birthYear.setValue(String.valueOf(bd.getYear()));
            birthMonth.setValue(String.valueOf(bd.getMonth().getValue()));
            birthDayOfMonth.setValue(String.valueOf(bd.getDayOfMonth()));
        } else  {
            birthYear.setValue("");
            birthMonth.setValue("");
            birthDayOfMonth.setValue("");
        }

        setVisible(true);

        // Focus first name initially
        firstName.focus();
    }
}
