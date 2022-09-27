package ru.otus.music.search.biz.workers

import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.stubs.MsStub
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.stubReadSuccess(title: String) =
    worker {
        this.title = title
        this.on { stubCase == MsStub.SUCCESS && state == MsState.RUNNING }
        this.handle {
            state = MsState.FINISHING
            val stub = MsCompositionDiscussionStub.prepareResult {
                msRequest.composition.takeIf { it.id != MsCompositionId.NONE }
                    ?.also { this.composition = it }
            }
            compositionResponse = stub
        }
    }