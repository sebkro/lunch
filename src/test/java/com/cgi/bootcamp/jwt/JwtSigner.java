package com.cgi.bootcamp.jwt;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtSigner {
	
	private static final String PRIVATE_CLAIM = "bla";
	private static final String SUBJECT = "id4711";
	private static final String ISSUER = "com.cgi.bootcamp";
	private static final String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJviNS95FlRRPuIUqU4VUE6McIndfKNKO+Awxbj5FOT4/fNpTG1yJM1WJmZ708R3OCTgp9XDmIJaXvW1YmjOfcpJOGP8YXizFnZ915lbTRPiULwYdjrte5fI2Aqwmn8eKpf7qUHWbNQxcjn9bda1Xa+LOlAF3Bg4Ngux36obFHS/AgMBAAECgYBEcz57jCZb/Bkq2XvGx+EtvMmmIFYctfo45fHM2cNtEFEjU7e1z4uNhyjxk6pX+Sn0N5o7adpMUzajmnSOJS5itTtZ9hJhcQv7GqypYanz5SpEPAiBA7XF2nYVwVD+FO22sp2OWynzAszBG4xJvRQCqTPDlsz0rUzMImLQmQP9gQJBAO5JffofzyA6ZvVYcCspDc0MkSbm2OsfKUaXo8sAB7ZU3xZFRB+1MMze9psDKucTFrm/CLpgn2vxtSe32ZGC+N8CQQCneJxgHucDKfMvir4ZUgAmCscz2BrJ2UKCN27j+kvMtrcYoKIZfgc7Ou4N6b5QgjcPzQpj1Xca/5jkFRtsOaAhAkBWmplRrfvun6tJ4mGqHhqo+rdKpz4RflrXEEf0gMdMyl3mTzhqOcVJwINqZWVTZImu6rJ2YqWuasPHcrd9fleLAkBp0gFlj0FDs4cxnEagczsazqausLfEoZ6v710HewnYcf/h9OZYm1TwbT0s12zlLFKkjlgpWyImkilGvbuVOBhhAkArZdrNfX6n4EMb+klF0j976JcbZspc6LhE/+eOGPCEUSyW73Z+Hlj+UIpIXpR6ifoi+pySKLfjFJcRwVQCzYm0";
	
	public static void main(String[] args) {
		Algorithm encode = Algorithm.RSA512(null, toPrivateKey(PRIVATE_KEY));
		Date date = new Date();
		String token = JWT.create()
		        .withIssuer(ISSUER)
		        .withExpiresAt(DateUtils.addMinutes(date, 30))
		        .withIssuedAt(date)
		        .withSubject(SUBJECT)
		        .withClaim(PRIVATE_CLAIM, "blubb")
		        .sign(encode);
		System.out.println(token);
	}
	
	private static RSAPrivateKey toPrivateKey(String s) {
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(s.getBytes()));
        
        KeyFactory kf;
		try {
			kf = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) kf.generatePrivate(spec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			return null;
		}
	}

}
