package ru.otus.music.search.mappers.v1.exceptions

class UnknownRequestClass(clazz: Class<*>)
    : RuntimeException("Class $clazz cannot be mapped to MsContext")