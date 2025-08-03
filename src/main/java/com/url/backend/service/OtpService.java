package com.url.backend.service;

import com.url.backend.DTO.OtpDto;
import com.url.backend.entity.OtpEntry;
import com.url.backend.repo.OtpRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepo otpRepo;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 3;
    private static final int COOLDOWN_MINUTES = 10;

    public OtpDto sendCodeUsingMail(String email) {
        String otp = generateOtp();
        OtpEntry otpEntry = otpRepo.findByEmail(email);

        if (otpEntry != null) {
            // Handle retry cooldown
            if (otpEntry.getRetryTime() != null && LocalDateTime.now().isBefore(otpEntry.getRetryTime())) {
                Duration duration = Duration.between(LocalDateTime.now(), otpEntry.getRetryTime());
                long minutesLeft = duration.toMinutes();
                long secondsLeft = duration.minusMinutes(minutesLeft).getSeconds();
                String msg = "Please try again after " + minutesLeft + " minutes and " + secondsLeft + " seconds.";
                return new OtpDto(msg);
            }

            // Reset retry state if cooldown passed
            if (otpEntry.getRetryTime() != null && LocalDateTime.now().isAfter(otpEntry.getRetryTime())) {
                otpEntry.setRetryTime(null);
                otpEntry.setCount(MAX_ATTEMPTS);
            }

            // If attempts exhausted
            if (otpEntry.getCount() <= 0) {
                otpEntry.setRetryTime(LocalDateTime.now().plusMinutes(COOLDOWN_MINUTES));
                otpRepo.save(otpEntry);
                return new OtpDto("OTP limit reached, try again after " + COOLDOWN_MINUTES + " minutes.");
            }

            // Update OTP details
            otpEntry.setOtp(otp);
            otpEntry.setCreated_at(LocalDateTime.now());
            otpEntry.setExpired_at(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
            otpEntry.setCount(otpEntry.getCount() - 1);
        } else {
            // New user OTP entry
            otpEntry = new OtpEntry();
            otpEntry.setEmail(email);
            otpEntry.setOtp(otp);
            otpEntry.setCreated_at(LocalDateTime.now());
            otpEntry.setExpired_at(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
            otpEntry.setCount(MAX_ATTEMPTS - 1);
        }

        otpRepo.save(otpEntry);
        sendEmail(email, otp);
        return buildOtpDto(otpEntry);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otpValue = 100000 + random.nextInt(900000); // generates 6-digit OTP
        return String.valueOf(otpValue);
    }

    private void sendEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(fromEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp + "\nIt will expire in " + OTP_EXPIRY_MINUTES + " minutes.");
        mailSender.send(message);
    }

    private OtpDto buildOtpDto(OtpEntry entry) {
        OtpDto dto = new OtpDto();
        dto.setMessage("Code sent to email");
        dto.setExpireTime(entry.getExpired_at());
        dto.setCount(entry.getCount());
        dto.setOtp(entry.getOtp());
        return dto;
    }
}
