package com.anh.movie;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;

public class TokenUtil {

	public static String getJWTString(String username, String roles, Date expires, Key key) {
		// Issue a token (can be a random String persisted to a database or a JWT token)
		// The issued token must be associated to a user
		// Return the issued token
		if (username == null) {
			throw new NullPointerException("null username is illegal");
		}
		if (roles == null) {
			throw new NullPointerException("null roles are illegal");
		}
		if (key == null) {
			throw new NullPointerException("null key is illegal");
		}

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		String jwtString = Jwts.builder().setIssuer("Movie").setSubject(username)
				.setAudience(StringUtils.join(Arrays.asList(roles), ",")).setIssuedAt(new Date())
				.signWith(signatureAlgorithm, key).compact();
		return jwtString;
	}

	public static boolean isValid(String token, Key key) {
		try {
			Jwts.parser().setSigningKey(key).parseClaimsJws(token.trim());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getName(String jwsToken, Key key) {
		if (isValid(jwsToken, key)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
			return claimsJws.getBody().getSubject();
		}
		return null;
	}

	public static String getRoles(String jwsToken, Key key) {
		if (isValid(jwsToken, key)) {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
			return claimsJws.getBody().getAudience();
		}
		return "";
	}
}
