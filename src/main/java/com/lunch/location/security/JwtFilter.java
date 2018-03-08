package com.lunch.location.security;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

public class JwtFilter extends GenericFilterBean {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
	
	private String publicKey;

	private String issuer;
	
	private JWTVerifier verifier;
	
	public JwtFilter(String publicKey, String issuer) {
		this.publicKey = publicKey;
		this.issuer = issuer;
	}
	
	@PostConstruct
	public void init() {
		LOGGER.info("issuer: {}, public key: {}", issuer, publicKey);
		Algorithm decode = Algorithm.RSA512(toPublicKey(publicKey), null);
		verifier = JWT.require(decode)
				.withIssuer(issuer)
	    		.build(); 
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		String authHeader = request.getHeader("Authorization");
		if (StringUtils.isNoneBlank(authHeader) && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				verifier.verify(token);
				filterChain.doFilter(req, res);
			} catch (JWTVerificationException e) {
				LOGGER.info("invalid Token");
				((HttpServletResponse) res).sendError(403);
			}
			
		} else {
			LOGGER.info("missing Token");
			((HttpServletResponse) res).sendError(401);
		}
	}
	
	private RSAPublicKey toPublicKey(String s) {
		X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(Base64.getDecoder().decode(s.getBytes()));
        KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			return (RSAPublicKey) kf.generatePublic(X509publicKey); 
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			return null;
		}
	}
}
