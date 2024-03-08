package com.reappa.rieltorapp

import com.reappa.rieltorapp.enums.RolesNamesValues
import com.reappa.rieltorapp.services.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RieltorAppApplication(
//    @Autowired
//    private val roleService: RoleService,
) : CommandLineRunner {

    override fun run(vararg args: String?) {
//        roleService.createRole(RolesNamesValues.ROLE_RIELTOR.stringValue)
//        roleService.createRole(RolesNamesValues.ROLE_CUSTOMER.stringValue)
//        roleService.createRole(RolesNamesValues.ROLE_MODERATOR.stringValue)
    }
}

fun main(args: Array<String>) {
    runApplication<RieltorAppApplication>(*args)
}
