package ru.otus.music.search.biz.workers

import ru.otus.music.search.MsCompositionDiscussionStub
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsComposition
import ru.otus.music.search.common.models.MsCompositionId
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.common.models.MsUserId
import ru.otus.music.search.common.stubs.MsStub
import ru.otus.music.search.cor.ICorChainDsl
import ru.otus.music.search.cor.worker

fun ICorChainDsl<MsContext>.stubCreateSuccess(title: String) =
    worker {
        this.title = title
        on { stubCase ==  MsStub.SUCCESS && state == MsState.RUNNING }
        handle {
            state = MsState.FINISHING
            val stub = MsCompositionDiscussionStub.prepareResult {
                msRequest.composition.takeIf { it.isNotEmpty()  }?.also { this.composition = it }
            }
            compositionResponse = stub
        }
    }

private fun MsComposition.isNotEmpty() =
    id != MsCompositionId.NONE && owner != MsUserId.NONE