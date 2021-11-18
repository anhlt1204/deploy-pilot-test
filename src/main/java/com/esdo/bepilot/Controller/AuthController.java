package com.esdo.bepilot.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.esdo.bepilot.Model.Request.AccountInputDTO;
import com.esdo.bepilot.Model.Request.AuthRequest;
import com.esdo.bepilot.Model.Request.ForgotPasswordRequest;
import com.esdo.bepilot.Model.Request.ResetPasswordRequest;
import com.esdo.bepilot.Service.AuthService;
import com.esdo.bepilot.Service.jwt.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> login(@RequestBody AuthRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(authService.login(authenticationRequest));
    }

    @PostMapping(value = "/forgot")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> forgotPass(@RequestBody ForgotPasswordRequest request) {
        authService.sendOTP(request);
        return ResponseEntity.ok("Thành công");
    }

    @PatchMapping(value = "/reset/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> reset(@PathVariable(name = "id") Long id,
                                   @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.reset(id, request));
    }

    @PostMapping(value = "/register")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> saveAccount(@RequestBody AccountInputDTO account) {
        return ResponseEntity.ok(userDetailsService.save(account));
    }

    @PostMapping(value = "/image")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> upload(@RequestParam("files") MultipartFile[] files) throws IOException {
        for (int i = 0; i < files.length; i++) {
            Map<?, ?> cloudinaryMap = cloudinary.uploader().upload(files[i].getBytes(), ObjectUtils.emptyMap());
            System.out.println(cloudinaryMap.get("secure_url").toString());
            System.out.println(cloudinaryMap.get("public_id").toString());
        }

        return ResponseEntity.ok("Ok");
    }
}

