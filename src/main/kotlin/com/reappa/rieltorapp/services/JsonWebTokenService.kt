package com.reappa.rieltorapp.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*
import java.util.stream.Collectors

@Service
class JsonWebTokenService (
    @Value("\${jwt.secret}")
    private val secret: String,
    @Value("\${jwt.lifetime}")
    private val jwtLifetime: Duration,
){
    fun generateToken(userDetails: UserDetails):String{
        val claims: MutableMap<String, Any?> = HashMap()
        val roleList = userDetails.authorities
            .stream().map {
                obj: GrantedAuthority -> obj.authority
            }.collect(Collectors.toList())
        claims["roles"] = roleList
        val issuedDate = Date()
        val expiredDate = Date(issuedDate.time + jwtLifetime.toMillis())
        val jwt= Jwts.builder()
            .claims(claims)
            .subject(userDetails.username)
            .issuedAt(issuedDate)
            .expiration(expiredDate)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
        return jwt
    }
    
    fun getEmail(token: String): String {
        return getAllClaimFromToken(token).subject
    }

    fun getRoles(token: String): List<String> {
        val list:List<String> = getAllClaimFromToken(token)
            .get("roles", List::class.java) as List<String>
        return list
    }

    private fun getAllClaimFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
