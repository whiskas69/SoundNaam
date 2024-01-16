package com.example.soundnaam.view;

import com.example.soundnaam.POJO.RegisterUsers;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.EnumSet;
import java.util.Map;

@Route("RegisterPage")
public class RegisterPage extends Div {
    public RegisterPage() {
        Div mainContainer = new Div();
        mainContainer.getStyle().set("display", "flex");

        mainContainer.getStyle().set("height", "100vh"); // 100% of the viewport height

        // Create orange background on the left (1/3 of the height)

        Div leftSection = new Div();

        leftSection.getStyle().set("text-align", "center");
        Image logoImage = new Image("/images/Logo.png", "Logo");
        logoImage.getStyle().set("width", "300px").set("height", "150px");

        H1 headerRegister = new H1("Register");
        Button registerButton = new Button("register");

        registerButton.getStyle().set("background-color", "#5F9DB2").set("color", "white").set("border", "1px solid white").set("border-radius", "5px").set("width", "400px").set("margin-top", "50px");


        headerRegister.getStyle().set("text-align", "center").set("margin-top", "100px").set("color", "white");
        leftSection.getStyle().set("background-color", "#5F9DB2");
        leftSection.getStyle().set("flex", "3");
        leftSection.getStyle().set("height", "100%"); // 1/3 of the height
        leftSection.add(logoImage, headerRegister, registerButton);


        // Create login form on the right (2/3 of the height)

       Div rightSection = new Div();

       rightSection.getStyle().set("flex", "5");
        FormLayout loginForm = new FormLayout();
        H1 header = new H1("Register");
        RouterLink login = new RouterLink("Login", MainView.class);
        login.getStyle().set("text-align", "center").set("margin-top", "20px");

        header.getStyle().set("text-align", "center");
        TextField emailField = new TextField("Email");
        PasswordField passwordField = new PasswordField("Password");
        TextField usernameField = new TextField("Username");
        RadioButtonGroup<String> genderRadio = new RadioButtonGroup<>("Gender", "Male", "Female");
        RadioButtonGroup<String> roleRadio = new RadioButtonGroup<>("Role", "Artist", "User");



        Button registersButton = new Button("Register");

        loginForm.add(header, roleRadio, emailField, passwordField, usernameField, genderRadio, registersButton, login);

        loginForm.getStyle().set("width", "500px");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(loginForm);

        horizontalLayout.getStyle().set("margin", "200px");

        rightSection.add(horizontalLayout);



        mainContainer.add(leftSection, rightSection);

        add(mainContainer);

        //validation
        Binder<RegisterUsers> binder = new Binder<>();

        binder.forField(emailField)
                .asRequired("Email is required.")
                .withValidator(email -> email.matches(".+@.+\\..+"), "Invalid email address.")
                .bind(RegisterUsers::getEmail, RegisterUsers::setEmail);

        binder.forField(passwordField)
                .asRequired("Password is required.")
                .bind(RegisterUsers::getPassword, RegisterUsers::setPassword);

        binder.forField(usernameField)
                .asRequired("Username is required.")
                .bind(RegisterUsers::getUsername, RegisterUsers::setUsername);

        binder.forField(genderRadio)
                .asRequired("Gender is required.")
                .bind(RegisterUsers::getGender, RegisterUsers::setGender);
        binder.forField(roleRadio)
                .asRequired("role is required.")
                .bind(RegisterUsers::getRole, RegisterUsers::setRole);





        Dialog dialogCreateWrong = new Dialog();
        Dialog dialogCreateSuccess = new Dialog();
        dialogCreateSuccess.setCloseOnOutsideClick(false);
        dialogCreateWrong.setCloseOnOutsideClick(false);

        // Create components for the dialog
        H1 dialogHeader = new H1("Message");
        dialogHeader.getStyle().set("text-align", "center");
        Paragraph dialogContent = new Paragraph("Your Email have user use");
        Button okButton = new Button("OK", event -> {
            dialogCreateWrong.close();
            // You can add additional logic if needed after clicking OK
        });

        okButton.getStyle().set("text-align", "center");


        dialogCreateWrong.add(dialogHeader, dialogContent, okButton);



        H1 dialogHeaderS = new H1("Message");
        dialogHeaderS.getStyle().set("text-align", "center");
        Paragraph dialogContentS = new Paragraph("Register Success");
        Button okButtonS = new Button("OK", event -> {
            dialogCreateSuccess.close();
            // You can add additional logic if needed after clicking OK
        });
        okButtonS.getStyle().set("text-align", "center");



        dialogCreateSuccess.add(dialogHeaderS, dialogContentS, okButtonS);

        registersButton.addClickListener(event -> {

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

            String email = emailField.getValue();
            String password = passwordField.getValue();
            String username = usernameField.getValue();
            String gender = genderRadio.getValue();
            String role = roleRadio.getValue();

            System.out.println(gender);

            if (binder.isValid()) {
                // The form is valid, proceed with registration
                RegisterUsers registrationData = new RegisterUsers();
                if (binder.writeBeanIfValid(registrationData)) {
                    // Data is valid, proceed with registration
                    // Call the registration endpoint using registrationData
                    if (password != "" || email != "" || username != "" || gender != null || role != null) {
                        formData.add("email", email);
                        formData.add("password", password);
                        formData.add("username", username);
                        formData.add("gender", gender);
                        formData.add("role", role);

                        System.out.println(formData);

                        Map<String, Object> userList = WebClient.create()
                                .post()
                                .uri("http://localhost:8080/register")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .body(BodyInserters.fromFormData(formData))
                                .retrieve()
                                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                                })
                                .block();

                        System.out.println(userList);
                        if (userList != null) {
                            // Loop through the entries and print each key-value pair
                            for (Map.Entry<String, Object> entry : userList.entrySet()) {
                                System.out.println(entry.getValue());

                                if(entry.getValue().equals("emailHave")){
                                    dialogCreateWrong.open();
                                }
                                else{
                                    dialogCreateSuccess.open();
                                    UI.getCurrent().navigate(MainView.class);
                                }
                            }
                        } else {
                            System.out.println("Response map is null.");
                        }



                    } else {
                        System.out.println("ใส่ข้อมูลไม่ครบ");
                    }

                } else {
                    // Failed to write bean
                    Notification.show("Please require all data");
                }
            } else {
                // Show a notification or handle invalid form
                Notification.show("Please correct the errors in the form.");
            }




        });
    }

}