package com.ub.ib.security.encryption;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ub.ib.security.domain.AuthDetail;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

    private static final String SECRET = "your-256-bit-secret"; // Use env var or vault
    private final SecretKeySpec hmacKey = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

    public String createSignedJwt(AuthDetail detail) throws JOSEException {
        // Encrypt the claim set map as JSON
        String encryptedJson = encryptClaimSet(detail.getClaims());

        // Build JWT claims
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .claim("tadh_encrypted", encryptedJson)
                .expirationTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // TTL 5 mins
                .build();

        // Sign the JWT
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        signedJWT.sign(new MACSigner(hmacKey));

        return signedJWT.serialize();
    }

    private String encryptClaimSet(Map<String, String> claims) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            String json = new ObjectMapper().writeValueAsString(claims);
            byte[] encrypted = cipher.doFinal(json.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}