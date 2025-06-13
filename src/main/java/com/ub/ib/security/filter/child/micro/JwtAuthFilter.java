package com.ub.ib.security.filter.child.micro;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter {
        //extends OncePerRequestFilter {

    private static final String SECRET = "your-256-bit-secret";
    private final SecretKeySpec hmacKey = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

    /*@Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {

    }*/

    /*@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String jwt = request.getHeader("X-TADH-AUTH");
        if (jwt != null) {
            try {
                SignedJWT signedJWT = SignedJWT.parse(jwt);

                if (!signedJWT.verify(new MACVerifier(hmacKey))) {
                    throw new BadCredentialsException("Invalid JWT signature");
                }

                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                String encryptedPayload = claims.getStringClaim("tadh_encrypted");
                Map<String, Object> decryptedClaims = decryptClaims(encryptedPayload);

                TadhAuthenticationDetail detail = new TadhAuthenticationDetail();
                detail.setClaims(decryptedClaims);
                detail.setExpiryTime(claims.getExpirationTime().getTime());

                if (detail.getExpiryTime() < System.currentTimeMillis()) {
                    throw new BadCredentialsException("TADH token expired");
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        "tadh-user", null, Collections.emptyList());
                authentication.setDetails(detail);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                throw new BadCredentialsException("Failed to validate TADH JWT", e);
            }
        }

        chain.doFilter(request, response);
    }

    private Map<String, Object> decryptClaims(String encryptedBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedBase64));
        return new ObjectMapper().readValue(decrypted, new TypeReference<>() {});
    }*/
}
