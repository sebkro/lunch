package com.cgi.bootcamp.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;


public class JwtSymmetricTest {
	
	private static final String PRIVATE_CLAIM = "bla";
	private static final String SUBJECT = "id4711";
	private static final String ISSUER = "com.cgi.bootcamp";
	private Algorithm algorithm;
	
	@Before
	public void init() throws IllegalArgumentException, UnsupportedEncodingException {
		algorithm = Algorithm.HMAC512("secret");
	}
	
	@Test
	public void shouldAcceptToken() {
		Date date = new Date();
		String token = JWT.create()
		        .withIssuer(ISSUER)
		        .withExpiresAt(DateUtils.addMinutes(date, 30))
		        .withIssuedAt(date)
		        .withSubject(SUBJECT)
		        .withClaim(PRIVATE_CLAIM, "blubb")
		        .sign(algorithm);
		
		JWTVerifier verifier = JWT.require(algorithm)
				.withIssuer(ISSUER)
	    		.build(); 
	    DecodedJWT jwt = verifier.verify(token);
	    
	    Assert.assertEquals(ISSUER, jwt.getIssuer());
	    Assert.assertEquals(SUBJECT, jwt.getSubject());
	    Assert.assertEquals("blubb", jwt.getClaim(PRIVATE_CLAIM).asString());
	}

	@Test (expected = SignatureVerificationException.class)
	public void shouldRejectTokenDueToAnIllegalSecret() throws IllegalArgumentException, UnsupportedEncodingException {
		Algorithm attackAlgorithm = Algorithm.HMAC512("wrongSecret");
		Date date = new Date();
		String token = JWT.create()
				.withIssuer(ISSUER)
				.withExpiresAt(DateUtils.addMinutes(date, 30))
				.withIssuedAt(date)
				.withSubject(SUBJECT)
				.withClaim(PRIVATE_CLAIM, "blubb")
				.sign(attackAlgorithm);
		
		
		JWTVerifier verifier = JWT.require(algorithm)
				.withIssuer(ISSUER)
				.build(); 
		verifier.verify(token); // and expect exception
	}

	@Test (expected = TokenExpiredException.class)
	public void shouldRejectTokenBecauseItIsExpired() throws IllegalArgumentException, UnsupportedEncodingException {
		Date date = new Date();
		String token = JWT.create()
				.withIssuer(ISSUER)
				.withExpiresAt(DateUtils.addMinutes(date, -30))
				.withIssuedAt(DateUtils.addMinutes(date, -60))
				.withSubject(SUBJECT)
				.withClaim(PRIVATE_CLAIM, "blubb")
				.sign(algorithm);
		
		
		JWTVerifier verifier = JWT.require(algorithm)
				.withIssuer(ISSUER)
				.build(); 
		verifier.verify(token); // and expect exception
	}

	@Test (expected = InvalidClaimException.class)
	public void shouldRejectTokenBecauseIatIsInTheFuture() throws IllegalArgumentException, UnsupportedEncodingException {
		Date date = new Date();
		String token = JWT.create()
				.withIssuer(ISSUER)
				.withExpiresAt(DateUtils.addMinutes(date, 30))
				.withIssuedAt(DateUtils.addSeconds(date, 1))
				.withSubject(SUBJECT)
				.withClaim(PRIVATE_CLAIM, "blubb")
				.sign(algorithm);
		
		
		JWTVerifier verifier = JWT.require(algorithm)
				.withIssuer(ISSUER)
				.build(); 
		verifier.verify(token); // and expect exception
	}

	@Test
	public void shouldAcceptTokenFromTheFutureDueToConfiguredLeeway() throws IllegalArgumentException, UnsupportedEncodingException {
		Date date = new Date();
		String token = JWT.create()
				.withIssuer(ISSUER)
				.withExpiresAt(DateUtils.addMinutes(date, 30))
				.withIssuedAt(DateUtils.addSeconds(date, 1))
				.withSubject(SUBJECT)
				.withClaim(PRIVATE_CLAIM, "blubb")
				.sign(algorithm);
		
		
		JWTVerifier verifier = JWT.require(algorithm)
				.withIssuer(ISSUER)
				.acceptLeeway(2)
				.build(); 
		 DecodedJWT jwt = verifier.verify(token);
	    
	    Assert.assertEquals(ISSUER, jwt.getIssuer());
	    Assert.assertEquals(SUBJECT, jwt.getSubject());
	    Assert.assertEquals("blubb", jwt.getClaim(PRIVATE_CLAIM).asString());
	}

}
