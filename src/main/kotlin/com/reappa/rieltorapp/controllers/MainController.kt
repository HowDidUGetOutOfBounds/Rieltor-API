package com.reappa.rieltorapp.controllers

import com.reappa.rieltorapp.services.AuthService
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping(path = ["/main"])
class MainController(
    private val authService: AuthService,
) {
    @RequestMapping(method = [RequestMethod.GET], path = ["/unsecured"])
    fun unsecuredData(): String {
        return "unsecuredData"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/secured"])
    fun securedData(): String {
        return "securedData"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/admin"])
    fun adminData(): String {
        return "adminData"
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["{email}/passport"])
    fun getPassport(
        @PathVariable("email") accountEmail: String,
        @RequestHeader("Authorization") string: String,
    ): ResponseEntity<*>{

        return authService.getPassportMultipart(string.substring(7),accountEmail)
    }
}
