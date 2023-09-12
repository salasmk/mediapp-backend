package com.mitocode.controller;

import com.mitocode.model.ResetMail;
import com.mitocode.model.User;
import com.mitocode.service.ILoginService;
import com.mitocode.service.IResetMailService;
import com.mitocode.util.EmailUtil;
import com.mitocode.util.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final ILoginService loginService;

    private final IResetMailService resetMailService;

    private final EmailUtil emailUtil;

    //[Example username: mitotest21@gmail.com]
    @PostMapping(value = "/sendMail")
    public ResponseEntity<Integer> sendMail(@RequestBody String username) throws Exception {
        int rpta = 0;

        User us = loginService.checkUsername(username);
        if(us != null && us.getIdUser() > 0) {
            ResetMail resetMail = new ResetMail();
            resetMail.setRandom(UUID.randomUUID().toString());
            resetMail.setUser(us);
            resetMail.setExpiration(10);
            resetMailService.save(resetMail);

            Mail mail = new Mail();
            mail.setFrom("email.prueba.demo@gmail.com");
            mail.setTo(us.getUsername());
            mail.setSubject("RESTABLECER CONTRASEÑA  MEDIAPP");

            Map<String, Object> model = new HashMap<>();
            String url = "http://localhost:4200/#/forgot/" + resetMail.getRandom();
            model.put("user", resetMail.getUser().getUsername());
            model.put("resetUrl", url);
            mail.setModel(model);

            emailUtil.sendMail(mail);

            rpta = 1;
        }
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }

    @GetMapping(value = "/reset/check/{random}")
    public ResponseEntity<Integer> checkRandom(@PathVariable("random") String random) {
        int rpta = 0;
        try {
            if (random != null && !random.isEmpty()) {
                ResetMail rm = resetMailService.findByRandom(random);
                if (rm != null && rm.getId() > 0) {
                    if (!rm.isExpired()) {
                        rpta = 1;
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(rpta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(rpta, HttpStatus.OK);
    }

    @PostMapping(value = "/reset/{random}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> resetPassword(@PathVariable("random") String random, @RequestBody String password) {
        try {
            ResetMail rm = resetMailService.findByRandom(random);
            if (rm != null && rm.getId() > 0) {
                if (!rm.isExpired()) {
                    loginService.changePassword(password, rm.getUser().getUsername());
                    resetMailService.delete(rm);
                }
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
