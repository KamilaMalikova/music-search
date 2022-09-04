package ru.otus.music.search.common

import java.io.File

val EMPTY_FILE
    get() = File("${System.getProperty("user.dir")}/empty-file")