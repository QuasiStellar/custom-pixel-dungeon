package com.qsr.customspd.utils

import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object Archiver {
    fun unzip(array: ByteArray, destDirectory: String) {
        File(destDirectory).run {
            if (!exists()) mkdirs()
        }
        val zipInputStream = ZipInputStream(ByteArrayInputStream(array));
        val buff = ByteArray(1024)
        var entry: ZipEntry
        while (true) {
            entry = zipInputStream.nextEntry ?: break
            val filePath = destDirectory + File.separator + entry.name.replace('/', File.separatorChar)
            if (entry.isDirectory) {
                val dir = File(filePath)
                dir.mkdir()
                continue
            }
            val parentFolder = File(getParentDirPath(filePath))
            if (!parentFolder.exists()) parentFolder.mkdirs()
            val outputStream = BufferedOutputStream(FileOutputStream(filePath))
            var l: Int
            while (zipInputStream.read(buff).also { l = it } > 0) {
                outputStream.write(buff, 0, l)
            }
            outputStream.close()
        }
        zipInputStream.close()
    }

    private fun getParentDirPath(fileOrDirPath: String): String {
        val endsWithSlash = fileOrDirPath.endsWith(File.separator)
        return fileOrDirPath.substring(
            0, fileOrDirPath.lastIndexOf(
                File.separatorChar,
                if (endsWithSlash) fileOrDirPath.length - 2 else fileOrDirPath.length - 1
            )
        )
    }
}
