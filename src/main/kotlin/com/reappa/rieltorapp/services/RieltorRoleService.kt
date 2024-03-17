package com.reappa.rieltorapp.services

import com.reappa.rieltorapp.exceptions.PassportMultipartException
import com.reappa.rieltorapp.models.PassportMultipartFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class RieltorRoleService(
    @Autowired
    private val passportService: PassportService,
) {
    fun registerRieltor(
        multipartFile: MultipartFile,
        accountId: Long
        ):Boolean{
        val passportMultipartFile = multipartFile.originalFilename?.let {
            PassportMultipartFile(
                accountId,
                multipartFile.name,
                it,
                multipartFile.size,
                multipartFile.contentType,
                multipartFile.bytes,
            )
        }
        if (passportMultipartFile != null) {
            passportService.savePassportMultipartFile(passportMultipartFile)
        } else throw PassportMultipartException("Passport multipart file is empty")
        return true
    }
    fun getPassportMultipart(
        accountId: Long,
    ):PassportMultipartFile{
        return passportService.getPassportByAccountId(accountId)
    }
}