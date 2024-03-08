package com.reappa.rieltorapp.config

import com.reappa.rieltorapp.services.JsonWebTokenService
import io.jsonwebtoken.ExpiredJwtException

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.security.SignatureException

@Component
@RequiredArgsConstructor
@Slf4j
class JsonWebTokenRequestFilter (
    @Autowired
    private val jsonWebTokenService: JsonWebTokenService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HEADER_NAME)
        var email: String? = null
        var jwt: String? = null
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            jwt = authHeader.substring(BEARER_PREFIX.length)
            try {
                email = jsonWebTokenService.getEmail(jwt)
            } catch (e: ExpiredJwtException) {
                e.printStackTrace()
            } catch (e: SignatureException) {
                e.printStackTrace()
            }
        }
        if (email != null && SecurityContextHolder.getContext().authentication == null) {
            val token = UsernamePasswordAuthenticationToken(
                email,
                null,
                jwt?.let {
                    jsonWebTokenService.getRoles(it).map(::SimpleGrantedAuthority)
                }
            )
            SecurityContextHolder.getContext().authentication = token

        }
        filterChain.doFilter(request, response)
    }

    companion object {
        const val BEARER_PREFIX: String = "Bearer "
        const val HEADER_NAME: String = "Authorization"
    }
}