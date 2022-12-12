package ru.otus.music.search.biz

import ru.otus.music.search.biz.general.prepareResult
import ru.otus.music.search.biz.groups.operation
import ru.otus.music.search.biz.groups.stub
import ru.otus.music.search.biz.groups.validation
import ru.otus.music.search.biz.permissions.accessValidation
import ru.otus.music.search.biz.permissions.chainPermissions
import ru.otus.music.search.biz.permissions.frontPermissions
import ru.otus.music.search.biz.permissions.searchTypes
import ru.otus.music.search.biz.repo.repoCreateComment
import ru.otus.music.search.biz.repo.repoCreateComposition
import ru.otus.music.search.biz.repo.repoFilter
import ru.otus.music.search.biz.repo.repoPrepareAccept
import ru.otus.music.search.biz.repo.repoPrepareComment
import ru.otus.music.search.biz.repo.repoPrepareDecline
import ru.otus.music.search.biz.repo.repoReadComposition
import ru.otus.music.search.biz.repo.repoUpdateComment
import ru.otus.music.search.biz.repo.repoUpdateComposition
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
import ru.otus.music.search.common.models.MsSettings
import ru.otus.music.search.common.models.MsState
import ru.otus.music.search.cor.chain
import ru.otus.music.search.cor.rootChain
import ru.otus.music.search.cor.worker

class MsCompositionProcessor(
    val settings: MsSettings = MsSettings()
) {
    suspend fun exec(ctx: MsContext) =
        BusinessChain.exec(ctx.apply { settings = this@MsCompositionProcessor.settings })

    private companion object {
        val BusinessChain = rootChain<MsContext> {
            initStatus("Initialization of status")
            initRepo("Initialization of repo")

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
                chainPermissions("Checking user permissions")
                chain {
                    title = "Saving composition"
                    repoPrepareCreate("Preparation of object to save")
                    accessValidation("Checking access rights")
                    repoCreateComposition("Inserting composition into db")
                }
                frontPermissions("Checking front grants")
                prepareResult("Preparation of response")
            }

            operation("Reading of object", MsCommand.READ) {
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
                chainPermissions("Checking user permissions")
                chain {
                    title = "Reading composition discussion"
                    repoReadComposition("Reading composition from DB")
                    accessValidation("Checking access rights")
                    worker {
                        title = "Preparation read response"
                        on { state == MsState.RUNNING }
                        handle { msRepoDone = msRepoRead }
                    }
                }
                frontPermissions("Checking front grants")
                prepareResult("Preparation of response")
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
                chainPermissions("Checking user permissions")
                chain {
                    title = "Comment"
                    repoReadComposition("Reading composition from DB")
                    repoPrepareComment("Preparing comment")
                    accessValidation("Checking access rights")
                    repoCreateComment("Creating comment")
                }
                frontPermissions("Checking front grants")
                prepareResult("Preparation of response")
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
                chainPermissions("Checking user permissions")
                chain {
                    title = "Accept comment"
                    repoReadComposition("Reading composition from DB")
                    repoPrepareAccept("Preparing comment")
                    accessValidation("Checking access rights")
                    repoUpdateComment("Update comment")
                    repoUpdateComposition("Update composition")
                }
                frontPermissions("Checking front grants")
                prepareResult("Preparation of response")
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
                chainPermissions("Checking user permissions")
                chain {
                    title = "Decline comment"
                    repoReadComposition("Reading composition from DB")
                    repoPrepareDecline("Preparing comment")
                    accessValidation("Checking access rights")
                    repoUpdateComment("Update comment")
                    repoUpdateComposition("Update composition")
                }
                frontPermissions("Checking front grants")
                prepareResult("Preparation of response")
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
                chainPermissions("Checking user permissions")
                chain {
                    title = "Filter"
                    searchTypes("Prepare search request")
                    repoFilter("Filtering in DB")
                }
                frontPermissions("Checking front grants")
                prepareResult("Preparation of response")
            }
        }.build()
    }
}