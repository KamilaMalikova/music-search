package ru.otus.music.search.mappers.v1.exceptions

class UnknownMsStatus(status: String)
    : Throwable("Wrong status $status at mapping toTransport stage")
