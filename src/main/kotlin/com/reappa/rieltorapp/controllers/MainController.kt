package com.reappa.rieltorapp.controllers

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping(path = ["/main"])
class MainController {
    @GetMapping("/unsecured")
    fun unsecuredData(): String {
        return "unsecuredData"
    }

    @GetMapping("/secured")
    fun securedData(): String {
        return "securedData"
    }

    @GetMapping("/admin")
    fun adminData(): String {
        return "adminData"
    }

    @GetMapping("/info")
    fun userData(principal: Principal): String {
        return principal.name
    }
}