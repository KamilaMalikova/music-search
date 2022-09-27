package ru.otus.music.search.biz.workers

import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.stubs.MsStub
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.stubSearchSuccess(title: String) =
    worker {
        this.title = title
        this.on { stubCase == MsStub.SUCCESS && state == MsState.RUNNING }
        this.handle {
            state = MsState.FINISHING
            val stub = MsCompositionDiscussionStub.prepareSearchList(
                status = filterRequest.discussionStatus,
                owner = filterRequest.ownerId
            )

            compositionsResponse = stub
        }
    }