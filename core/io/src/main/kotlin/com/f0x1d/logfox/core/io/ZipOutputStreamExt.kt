package com.f0x1d.logfox.core.io

import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun ZipOutputStream.putZipEntry(name: String, content: ByteArray) {
    putNextEntry(ZipEntry(name))
    write(content)
    closeEntry()
}

fun ZipOutputStream.putZipEntry(name: String, file: File) {
    putNextEntry(ZipEntry(name))
    file.inputStream().use { it.copyTo(this) }
    closeEntry()
}
