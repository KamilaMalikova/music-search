package ru.otus.music.search.mappers.v1.exceptions

import ru.otus.music.search.common.models.MsCommand

class UnknownMsCommand(command: MsCommand)
    : Throwable("Wrong command $command at mapping toTransport stage")