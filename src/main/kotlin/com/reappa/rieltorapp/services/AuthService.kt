package com.reappa.rieltorapp.services



import com.reappa.rieltorapp.dtos.AccountDto
import com.reappa.rieltorapp.dtos.JsonWebTokenRequest
import com.reappa.rieltorapp.dtos.JsonWebTokenResponse
import com.reappa.rieltorapp.dtos.RegistrationDto
import com.reappa.rieltorapp.enums.RolesNamesValues
import com.reappa.rieltorapp.exceptions.AppError
import com.reappa.rieltorapp.models.Role
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
@RequiredArgsConstructor
class AuthService(
    @Autowired
    private val accountService: AccountService,
    @Autowired
    private val roleService: RoleService,
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val passwordEncoder: PasswordEncoder,
    @Autowired
    private val jsonWebTokenService: JsonWebTokenService,
) {
    fun createToken(@RequestBody authRequest: JsonWebTokenRequest?): ResponseEntity<*> {
        try {
            if (authRequest == null) {
                return ResponseEntity(
                    AppError(HttpStatus.BAD_REQUEST.value(), "Empty request"), HttpStatus.BAD_REQUEST
                )
            }
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(authRequest.email, authRequest.password)
            )
            val userDetails = accountService.loadUserByUsername(authRequest.email)

            val token = jsonWebTokenService.generateToken(userDetails)

            return ResponseEntity.ok(JsonWebTokenResponse(token))
        } catch (e: BadCredentialsException) {
            return ResponseEntity(
                AppError(HttpStatus.UNAUTHORIZED.value(), "Incorrect login or password"),
                HttpStatus.UNAUTHORIZED
            )
        }

    }

    fun createUser(@RequestBody registrationDto: RegistrationDto): ResponseEntity<*> {
        if (registrationDto.password != registrationDto.confirmPassword) {
            return ResponseEntity(
                AppError(HttpStatus.BAD_REQUEST.value(), "Password and confirm don't match"),
                HttpStatus.BAD_REQUEST
            )
        }
        if (accountService.findAccountByEmail(registrationDto.email) != null) {
            return ResponseEntity(
                AppError(HttpStatus.BAD_REQUEST.value(), "Email is already registered"),
                HttpStatus.BAD_REQUEST
            )
        }

        val role: Role = roleService.findRoleByRoleName(RolesNamesValues.ROLE_CUSTOMER.stringValue)

        val encodedPassword: String = passwordEncoder.encode(registrationDto.password)
        val accountFromDB = accountService.saveNewAccount(registrationDto.email, encodedPassword, role)

        return ResponseEntity.ok(accountFromDB.id?.let { AccountDto(it, accountFromDB.accountEmail) })
    }

}