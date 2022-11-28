package ru.otus.music.search.common.exceptions

import ru.otus.music.search.common.models.MsCompositionLock

class RepoConcurrencyException(expectedLock: MsCompositionLock, actualLock: MsCompositionLock?): RuntimeException(
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)