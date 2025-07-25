package com.cuizhanming.template.kotlin.springsecurity.business.filter

import com.cuizhanming.template.kotlin.springsecurity.atp.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userDetailsService: UserDetailsService,
    private val tokenService: TokenService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")
        if (authHeader.doesNotStartWith()) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader!!.extractTokenValue()
        val email = tokenService.extractEmail(token)

        if (email != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(email)
            if (tokenService.isValid(token, userDetails)) {
                updateSecurityContext(userDetails, request)
            }
            filterChain.doFilter(request, response)
        }
    }
}

private fun updateSecurityContext(userDetails: UserDetails?, request: HttpServletRequest) {
    val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails?.authorities)
    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
    SecurityContextHolder.getContext().authentication = authToken
}

private fun String.extractTokenValue(): String = this.substringAfter("Bearer ")

private fun String?.doesNotStartWith(): Boolean = this == null || !this.startsWith("Bearer ")
