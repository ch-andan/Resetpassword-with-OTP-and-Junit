package com.pacewisdom.admin.controller;

import com.pacewisdom.admin.Entity.User;
import com.pacewisdom.admin.exception.PWSException;
import com.pacewisdom.admin.service.AdminService;
import com.pacewisdom.admin.utility.JwtUtil;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/admin")
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/forgot_password")
    public String processForgotPassword(@RequestParam String email, Model model) throws PWSException, MessagingException, UnsupportedEncodingException {
        String token = RandomString.make(30);

        try {
            adminService.updateResetPasswordToken(token, email);
            String resetPasswordLink = (email) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (PWSException ex) {
            model.addAttribute("error", ex.getMessage());
        }

        return "forgot_password_form";
    }


    public void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@PWS.com", "PWS Support");
        String recipientEmail = email;
        helper.setTo(email);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);

    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam String token, Model model) throws PWSException {
        User user = adminService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset_password_form";
    }


    @PostMapping("/reset_password")
    public String processResetPassword(@RequestParam String token, String password, Model model) throws PWSException {
        User user = adminService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message In_valid token";
        } else {
            adminService.updatePassword(user, password);

            model.addAttribute("message", "You have successfully changed your password.");
            return "message valid token";
        }

    }
}

