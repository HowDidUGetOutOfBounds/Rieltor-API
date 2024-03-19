package com.reappa.rieltorapp.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import lombok.Data
import lombok.Getter
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
@Entity
@Data
@Getter
data class PassportMultipartFile(
    @Id
    val mpfAccountId: Long,
    //MultipartFile params
    private val mpfName: String,
    private val mpfOriginalFileName: String,
    private val mpfSize: Long,
    private val mpfContentType: String?,
    @Column(columnDefinition = "MEDIUMBLOB")
    private val mpfBytes:ByteArray?,
):MultipartFile{
    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(mpfBytes)
    }

    override fun getName(): String {
        return mpfName
    }

    override fun getOriginalFilename(): String? {
        return mpfOriginalFileName
    }

    override fun getContentType(): String? {
        return mpfContentType
    }

    override fun isEmpty(): Boolean {
        return mpfBytes==null
    }

    override fun getSize(): Long {
        return mpfSize
    }

    override fun getBytes(): ByteArray {
        if (mpfBytes==null) throw IllegalStateException("Empty password multipart file")
        return mpfBytes
    }

    override fun transferTo(dest: File) {
        // Implement the logic to transfer the file content to the specified destination file
        // You can use the getInputStream() method to read the content from the mpfBytes
        // and write it to the destination file.
        // Example:
        val inputStream = inputStream
        dest.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
}
