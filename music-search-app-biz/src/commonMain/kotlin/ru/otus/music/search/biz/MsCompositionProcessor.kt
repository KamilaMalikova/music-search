package ru.otus.music.search.biz

import ru.otus.music.search.biz.groups.operation
import ru.otus.music.search.biz.groups.stub
import ru.otus.music.search.biz.groups.validation
import ru.otus.music.search.biz.workers.initStatus
import ru.otus.music.search.biz.workers.stubAcceptSuccess
import ru.otus.music.search.biz.workers.stubCommentSuccess
import ru.otus.music.search.biz.workers.stubCreateSuccess
import ru.otus.music.search.biz.workers.stubDbError
import ru.otus.music.search.biz.workers.stubDeclineSuccess
import ru.otus.music.search.biz.workers.stubNoCase
import ru.otus.music.search.biz.workers.stubNoCommentFound
import ru.otus.music.search.biz.workers.stubNoDiscussionFound
import ru.otus.music.search.biz.workers.stubReadSuccess
import ru.otus.music.search.biz.workers.stubSearchSuccess
import ru.otus.music.search.biz.workers.stubValidationBadText
import ru.otus.music.search.biz.workers.stubValidationBadId
import ru.otus.music.search.biz.workers.validation.finishFilterValidation
import ru.otus.music.search.biz.workers.validation.finishValidation
import ru.otus.music.search.biz.workers.validation.validateAuthorIdNotEmpty
import ru.otus.music.search.biz.workers.validation.validateCommentHasContent
import ru.otus.music.search.biz.workers.validation.validateCommentIdNotEmpty
import ru.otus.music.search.biz.workers.validation.validateCommentNotEmpty
import ru.otus.music.search.biz.workers.validation.validateCompositionIdEmpty
import ru.otus.music.search.biz.workers.validation.validateCompositionIdNotEmpty
import ru.otus.music.search.biz.workers.validation.validateOwnerIdNotEmpty
import ru.otus.music.search.common.MsContext
import ru.otus.music.search.common.models.MsCommand
import ru.otus.music.search.cor.rootChain
import ru.otus.music.search.cor.worker

class MsCompositionProcessor {
    suspend fun exec(ctx: MsContext) =
        BusinessChain.exec(ctx)

    private companion object {
        val BusinessChain = rootChain<MsContext> {
            initStatus("Initialization of status")

            operation("Creation of object", MsCommand.CREATE) {
                stub("Handling stubs") {
                    stubCreateSuccess("Imitation of successful handling")
                    stubValidationBadId("Imitation of id validation error")
                    stubDbError("Imitation of DB error")
                    stubNoCase("Error: stub is not found")
                }

                validation {
                    worker("Copy base data") { msValidating = msRequest.copy() }
                    validateCompositionIdEmpty("Validation of composition id empty")
                    validateOwnerIdNotEmpty("Validation of ownerId")

                    finishValidation("Ending validation")
                }
            }

            operation("Creation of object", MsCommand.READ) {
                stub("Handling stubs") {
                    stubReadSuccess("Imitation of successful handling")
                    stubValidationBadId("Imitation of id validation error")
                    stubNoDiscussionFound("Imitating of not found")
                    stubDbError("Imitation of DB error")
                    stubNoCase("Error: stub is not found")
                }

                validation {
                    worker("Copy base data") { msValidating = msRequest.deepCopy() }
                    validateCompositionIdNotEmpty("Validation of composition id not empty")

                    finishValidation("Ending validation")
                }
            }

            operation("Comment of object", MsCommand.COMMENT) {
                stub("Handling stubs") {
                    stubCommentSuccess("Imitation of successful handling")
                    stubValidationBadId("Imitation of id validation error")
                    stubNoDiscussionFound("Imitating of not found")
                    stubValidationBadText("Imitating no text provided")
                    stubDbError("Imitation of DB error")
                    stubNoCase("Error: stub is not found")
                }

                validation {
                    worker("Copy base data") { msValidating = msRequest.deepCopy() }
                    worker("Trim comment") {
                        msValidating.comment.text = msValidating.comment.text.trim()
                    }
                    validateCompositionIdNotEmpty("Validation of composition id not empty")
                    validateAuthorIdNotEmpty("Validation of ownerId")
                    validateCommentNotEmpty("Validation of comment not to be empty")
                    validateCommentHasContent("Validation of comment to have content")

                    finishValidation("Ending validation")
                }
            }

            operation("Comment of object", MsCommand.ACCEPT) {
                stub("Handling stubs") {
                    stubAcceptSuccess("Imitation of successful handling")
                    stubValidationBadId("Imitation of id validation error")
                    stubNoCommentFound("Imitating of not found")
                    stubDbError("Imitation of DB error")
                    stubNoCase("Error: stub is not found")
                }

                validation {
                    worker("Copy base data") { msValidating = msRequest.deepCopy() }

                    validateCompositionIdNotEmpty("Validation of composition id not empty")
                    validateCommentIdNotEmpty("Validation of commentId")
                    finishValidation("Ending validation")

                }
            }

            operation("Comment of object", MsCommand.DECLINE) {
                stub("Handling stubs") {
                    stubDeclineSuccess("Imitation of successful handling")
                    stubValidationBadId("Imitation of id validation error")
                    stubNoCommentFound("Imitating of not found")
                    stubDbError("Imitation of DB error")
                    stubNoCase("Error: stub is not found")
                }

                validation {
                    worker("Copy base data") { msValidating = msRequest.deepCopy() }

                    validateCompositionIdNotEmpty("Validation of composition id not empty")
                    validateCommentIdNotEmpty("Validation of commentId")
                    finishValidation("Ending validation")
                }
            }

            operation("Comment of object", MsCommand.SEARCH) {
                stub("Handling stubs") {
                    stubSearchSuccess("Imitation of successful handling")
                    stubDbError("Imitation of DB error")
                    stubNoCase("Error: stub is not found")
                }

                validation {
                    worker("Copy base data") { filterValidating = filterRequest.copy() }

                    finishFilterValidation("Ending validation")
                }
            }
        }.build()
    }
}