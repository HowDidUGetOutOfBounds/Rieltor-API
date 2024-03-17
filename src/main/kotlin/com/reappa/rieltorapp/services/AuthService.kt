package com.reappa.rieltorapp.services

import com.reappa.rieltorapp.dtos.AccountDto
import com.reappa.rieltorapp.dtos.JsonWebTokenRequest
import com.reappa.rieltorapp.dtos.JsonWebTokenResponse
import com.reappa.rieltorapp.dtos.RegistrationDto
import com.reappa.rieltorapp.enums.RolesNamesValues
import com.reappa.rieltorapp.exceptions.AppError
import com.reappa.rieltorapp.models.Account
import com.reappa.rieltorapp.models.Role
import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.Serializable

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
    @Autowired
    private val rieltorRoleService: RieltorRoleService,
) {
    @Transactional
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

    @Transactional
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

    @Transactional
    fun registerNewRieltor(
        userDetails: UserDetails,
        multipartFile: MultipartFile,
    ): ResponseEntity<*> {
        var account: Account? = accountService.findAccountByEmail(userDetails.username)
            ?: return ResponseEntity(
                AppError(HttpStatus.BAD_REQUEST.value(), "User with email ${userDetails.username} not found"),
                HttpStatus.BAD_REQUEST
            )
        if (account?.authorities?.contains<Serializable>(RolesNamesValues.ROLE_RIELTOR) == true) {
            return ResponseEntity(
                AppError(HttpStatus.CONFLICT.value(), "User with email ${userDetails.username} is already a rieltor"),
                HttpStatus.CONFLICT
            )
        }
        if (rieltorRoleService.registerRieltor(multipartFile, account?.id!!)) {
            account.authorities += roleService.findRoleByRoleName(RolesNamesValues.ROLE_RIELTOR.stringValue)
            accountService.updateAccount(account.id!!, account)
        }
        return ResponseEntity.ok("You are rieltor now")
    }

    @Transactional
    fun getPassportMultipart(
        string: String,
        accountEmail: String,
    ): ResponseEntity<*> {
        val account = accountService.findAccountByEmail(jsonWebTokenService.getEmail(string))
        if (account == null) {
            return ResponseEntity(
                AppError(HttpStatus.BAD_REQUEST.value(), "User with email ${jsonWebTokenService.getEmail(string)} not found"),
                HttpStatus.BAD_REQUEST
            )
        } else if (account.accountEmail!=accountEmail){
            return ResponseEntity(
                AppError(HttpStatus.FORBIDDEN.value(),"Forbidden"),
                HttpStatus.UNAUTHORIZED
            )
        }else if (!checkAuthority(account,RolesNamesValues.ROLE_RIELTOR)) {
            return ResponseEntity(
                AppError(HttpStatus.BAD_REQUEST.value(), "User with email ${jsonWebTokenService.getEmail(string)} is not rieltor"),
                HttpStatus.BAD_REQUEST
            )
        }
        val passportMultipartFile = account.id?.let { rieltorRoleService.getPassportMultipart(it) }
        if (passportMultipartFile?.originalFilename!=null
            && passportMultipartFile.contentType!=null) {
            return ResponseEntity.ok()
                .header("filename",
                    "${account.username}_passport")
                .contentType(MediaType.valueOf(
                    passportMultipartFile.contentType!!))
                .contentLength(
                    passportMultipartFile.size)
                .body(InputStreamResource(
                    ByteArrayInputStream(
                        passportMultipartFile.getBytes()
                    )))
        }
        return ResponseEntity(
            AppError(HttpStatus.FORBIDDEN.value(), "Unhandled exception"),
            HttpStatus.FORBIDDEN
        )
    }
    @Transactional
    fun checkAuthority(
        userDetails: UserDetails,
        authority: RolesNamesValues,
    ):Boolean{
        val role= roleService.findRoleByRoleName(authority.stringValue)
        val account = accountService.findAccountByEmail(userDetails.username)
        if (account != null) {
            return roleService.checkAuthority(account.authorities, role)
        }
        return false
    }
}
