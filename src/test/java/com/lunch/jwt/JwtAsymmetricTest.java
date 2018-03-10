package com.lunch.jwt;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;


public class JwtAsymmetricTest {
	
	private static final String PRIVATE_CLAIM = "bla";
	private static final String SUBJECT = "id4711";
	private static final String ISSUER = "com.cgi.bootcamp";
	
	private String publicKeyStr;
	private String privateKeyStr;
	
	@Before
	public void init() throws IllegalArgumentException, UnsupportedEncodingException, NoSuchAlgorithmException {
		Pair<RSAPublicKey, RSAPrivateKey> keys = getKeys();
		/**
		 * encode und decode from/to String nur damit ich weiß, wie das geht
		 */
		publicKeyStr = Base64.getEncoder().encodeToString(keys.getValue0().getEncoded());
		privateKeyStr = Base64.getEncoder().encodeToString(keys.getValue1().getEncoded());
	}
	
	@Test
	public void shouldAcceptToken() {
		Algorithm encode = Algorithm.RSA512(toPublicKey(publicKeyStr), toPrivateKey(privateKeyStr));
		Date date = new Date();
		String token = JWT.create()
		        .withIssuer(ISSUER)
		        .withExpiresAt(DateUtils.addMinutes(date, 30))
		        .withIssuedAt(date)
		        .withSubject(SUBJECT)
		        .withClaim(PRIVATE_CLAIM, "blubb")
		        .sign(encode);
		
		Algorithm decode = Algorithm.RSA512(toPublicKey(publicKeyStr), null);
		JWTVerifier verifier = JWT.require(decode)
				.withIssuer(ISSUER)
	    		.build(); 
	    DecodedJWT jwt = verifier.verify(token);
	    
	    Assert.assertEquals(ISSUER, jwt.getIssuer());
	    Assert.assertEquals(SUBJECT, jwt.getSubject());
	    Assert.assertEquals("blubb", jwt.getClaim(PRIVATE_CLAIM).asString());
	}

	@Test (expected = SignatureVerificationException.class)
	public void shouldRejectTokenDueToAnIllegalSecret() throws IllegalArgumentException, UnsupportedEncodingException {
		Algorithm encode = Algorithm.RSA512(toPublicKey(publicKeyStr), toPrivateKey(privateKeyStr));
		Date date = new Date();
		String token = JWT.create()
		        .withIssuer(ISSUER)
		        .withExpiresAt(DateUtils.addMinutes(date, 30))
		        .withIssuedAt(date)
		        .withSubject(SUBJECT)
		        .withClaim(PRIVATE_CLAIM, "blubb")
		        .sign(encode);
		
		Pair<RSAPublicKey, RSAPrivateKey> newKeys = getKeys();
		Algorithm decode = Algorithm.RSA512(newKeys.getValue0(), null);
		JWTVerifier verifier = JWT.require(decode)
				.withIssuer(ISSUER)
	    		.build(); 
	    verifier.verify(token); //and expect exception
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
	
	private RSAPrivateKey toPrivateKey(String s) {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(s.getBytes()));
        
        KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) kf.generatePrivate(spec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			return null;
		}
	}
	
	private static Pair<RSAPublicKey, RSAPrivateKey> getKeys() {
		KeyPairGenerator generator;
		try {
			generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			KeyPair pair = generator.generateKeyPair();
			
			RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();
			return new Pair<>(publicKey, privateKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

}
