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
}
