package com.reappa.rieltorapp.controllers

import com.reappa.rieltorapp.dtos.JsonWebTokenRequest
import com.reappa.rieltorapp.dtos.RegistrationDto
import com.reappa.rieltorapp.services.AuthService
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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

    @RequestMapping(method = [RequestMethod.POST], path = ["/registerRieltor"])
    fun registerNewRieltor(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam("Passport") file: MultipartFile,
    ):ResponseEntity<*>{
        // Проверка, что файл имеет расширение .jpg или .png
        val extension = file.originalFilename?.substringAfterLast(".")
        if (extension != "jpg" && extension != "png") {
            return ResponseEntity.badRequest().body("Разрешены только изображения (jpg, png)")
        }
        return authService.registerNewRieltor(userDetails,file)
    }
}
