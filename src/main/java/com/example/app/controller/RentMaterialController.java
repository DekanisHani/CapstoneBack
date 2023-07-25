package com.example.app.controller;

import com.example.app.dto.RentRequest;
import com.example.app.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rent")
public class RentMaterialController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @PostMapping("{email}")
    public ResponseEntity<?> sendRentEmail(@RequestBody List<RentRequest> rentRequest, @PathVariable String email) {
        try {

            SimpleMailMessage confirmationMessage = new SimpleMailMessage();
            confirmationMessage.setTo(email);
            confirmationMessage.setFrom(sender);
            confirmationMessage.setSubject("Material Rental Confirmation");

            StringBuilder body = new StringBuilder();
            float totalAmount = 0.0f;

            for (RentRequest request : rentRequest) {
                Material material = request.getMaterial();
                float totalPriceForMaterial = request.getQuantity() * material.getPrice();
                totalAmount += totalPriceForMaterial;

                body.append(String.format(
                        "<tr><td>%s</td><td>%d</td><td>%.2f</td><td>%.2f</td></tr>",
                        material.getName(),
                        request.getQuantity(),
                        material.getPrice(),
                        totalPriceForMaterial)
                );
            }

            String confirmationEmailTemplate = """
                    <h1>Material Rental Confirmation</h1><p>You have rented the following material:</p>
                    <table style="border-collapse: collapse;" border="1" >
                        <thead>
                            <tr>
                                <th>Material Name</th>
                                <th>Quantity</th>
                                <th>Price per Unit</th>
                                <th>Total Price</th>
                            </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>
                    <p>Total Amount: $%.2f</p><br/>
                    """.formatted(body, totalAmount);

            confirmationMessage.setText(confirmationEmailTemplate.replace("\n", ""));
            javaMailSender.send(confirmationMessage);


            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("Dekanis@outlook.it");
            message.setFrom(sender);
            message.setSubject("New Material Rental Request");


            String emailTemplate = """
                    <h1>Material Rental Request</h1><p><b>%s</b> have rented the following material:</p>
                    <table style="border-collapse: collapse;" border="1" >
                        <thead>
                            <tr>
                                <th>Material Name</th>
                                <th>Quantity</th>
                                <th>Price per Unit</th>
                                <th>Total Price</th>
                            </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>
                    <p>Total Amount: $%.2f</p><br/>
                    """.formatted(email, body, totalAmount);

            message.setText(emailTemplate.replace("\n", ""));
            javaMailSender.send(message);


            return ResponseEntity.ok().build();
        } catch (Exception error) {
            System.out.println(error.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
