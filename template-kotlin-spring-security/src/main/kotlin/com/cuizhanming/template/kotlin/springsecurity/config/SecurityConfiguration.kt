package com.cuizhanming.template.kotlin.springsecurity.config

import com.cuizhanming.template.kotlin.springsecurity.business.filter.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfiguration (
    private val authenticationProvider: AuthenticationProvider
) {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): DefaultSecurityFilterChain =
        http.csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("api/auth", "api/auth/refresh", "/error").permitAll()
                    .requestMatchers( "/api/articles").permitAll()
                    .requestMatchers(POST, "/api/users").permitAll()
                    .requestMatchers("/api/users**").hasRole("ADMIN")
//                    .requestMatchers("/graphiql", "/altair").permitAll()
//                    .requestMatchers("/api/graphql").permitAll()
                    .anyRequest().fullyAuthenticated()
            }
            .sessionManagement{
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()


}