package com.reappa.rieltorapp.controllers

import com.reappa.rieltorapp.dtos.JsonWebTokenRequest
import com.reappa.rieltorapp.dtos.JsonWebTokenResponse
import com.reappa.rieltorapp.dtos.RegistrationDto
import com.reappa.rieltorapp.services.AuthService
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@AllArgsConstructor
@RequestMapping(path = ["/auth"], )
class AuthController(
    @Autowired
    private val authService: AuthService,
    ){
    @RequestMapping(method = [RequestMethod.POST], path = ["/registerNewAccount"])
    fun registerNewAccount(@RequestBody registrationDto: RegistrationDto): ResponseEntity<*> {
        return authService.createUser(registrationDto)
    }
    
    @RequestMapping(method = [RequestMethod.POST], path = ["/login"])
    fun createToken(@RequestBody authRequest: JsonWebTokenRequest?):ResponseEntity<String> {
        val responce = authService.createToken(authRequest)
        return ResponseEntity.ok(responce.body.toString()) 
    }
}
