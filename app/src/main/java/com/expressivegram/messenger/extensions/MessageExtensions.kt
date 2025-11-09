package com.expressivegram.messenger.extensions

import com.expressivegram.messenger.data.message.TdMessage
import com.expressivegram.messenger.utils.DateUtility
import com.expressivegram.messenger.utils.TdUtility
import com.expressivegram.messenger.utils.UserConfig
import org.drinkless.tdlib.TdApi

suspend fun TdApi.Message.getTextMessageContent(): String {
    val senderName = getSenderName()
    return when (this.content) {
        is TdApi.MessageText -> (content as TdApi.MessageText).text.text
        is TdApi.MessagePhoto -> {
            val photo = (content as TdApi.MessagePhoto)
            if (photo.caption.text.isEmpty()) {
                "📷 Photo"
            } else {
                "📷 ${photo.caption.text}"
            }
        }
        is TdApi.MessageVideo -> {
            val video = (content as TdApi.MessageVideo)
            if (video.caption.text.isEmpty()) {
                "🎥 Video"
            } else {
                "🎥 ${video.caption.text}"
            }
        }
        is TdApi.MessageDocument -> {
            val document = (content as TdApi.MessageDocument)
            if (document.caption.text.isEmpty()) {
                "📄 ${document.document.fileName}"
            } else {
                "📄 ${document.caption.text}"
            }
        }
        is TdApi.MessageAudio -> {
            val audio = (content as TdApi.MessageAudio)
            if (audio.caption.text.isEmpty()) {
                "🎵 ${audio.audio.title}"
            } else {
                "🎵 ${audio.caption.text}"
            }
        }
        is TdApi.MessageAnimation -> {
            val animation = (content as TdApi.MessageAnimation)
            if (animation.caption.text.isEmpty()) {
                "🎞️ GIF"
            } else {
                "🎞️ ${animation.caption.text}"
            }
        }
        is TdApi.MessageSticker -> (content as TdApi.MessageSticker).sticker.emoji + " Sticker"
        is TdApi.MessagePaidMedia -> {
            val paidMedia = (content as TdApi.MessagePaidMedia)
            if (paidMedia.caption.text.isEmpty()) {
                "💰 Paid Media"
            } else {
                "💰 ${paidMedia.caption.text}"
            }
        }
        is TdApi.MessageVideoNote -> "📽️ ${(content as TdApi.MessageVideoNote).videoNote.duration} sec, Video Note"
        is TdApi.MessageVoiceNote -> {
            val voiceNote = (content as TdApi.MessageVoiceNote)
            "🎤 ${voiceNote.voiceNote.duration} sec, ${voiceNote.caption.text}"
        }
        is TdApi.MessageContact -> "$senderName shared contact ${(content as TdApi.MessageContact).contact.firstName}"
        is TdApi.MessageLocation -> "📍 location"
        is TdApi.MessageExpiredPhoto -> "Expired photo"
        is TdApi.MessageExpiredVideo -> "Expired video"
        is TdApi.MessageExpiredVideoNote -> "Expired video note"
        is TdApi.MessageExpiredVoiceNote -> "Expired voice note"
        is TdApi.MessageVenue -> "Venue"
        is TdApi.MessageAnimatedEmoji -> (content as TdApi.MessageAnimatedEmoji).emoji
        is TdApi.MessageDice -> (content as TdApi.MessageDice).emoji
        is TdApi.MessageGame -> (content as TdApi.MessageGame).game.shortName
        is TdApi.MessagePoll -> (content as TdApi.MessagePoll).poll.question.text
        is TdApi.MessageStory -> "Story"
        is TdApi.MessageInvoice -> (content as TdApi.MessageInvoice).productInfo.title
        is TdApi.MessageCall -> {
            val call = (content as TdApi.MessageCall)
            val callType = if (call.isVideo) "Audio" else "Video"
            val discardReason = when (call.discardReason) {
                is TdApi.CallDiscardReasonEmpty -> "Empty"
                is TdApi.CallDiscardReasonMissed -> "Missed"
                is TdApi.CallDiscardReasonDeclined -> "Declined"
                is TdApi.CallDiscardReasonDisconnected -> "Disconnected"
                is TdApi.CallDiscardReasonHungUp -> "Hung Up"
                is TdApi.CallDiscardReasonUpgradeToGroupCall -> "Upgraded to Group"
                else -> "Unknown"
            }

            "$discardReason $callType call (${call.duration} sec)"
        }
        is TdApi.MessageGroupCall -> ""
        is TdApi.MessageVideoChatScheduled -> {
            val scheduled = (content as TdApi.MessageVideoChatScheduled)
            val date = DateUtility.getDateFromUnix(scheduled.startDate)
            val formattedDate = DateUtility.getFormattedDate(date)
            "$senderName scheduled video chat in $formattedDate"
        }
        is TdApi.MessageVideoChatStarted -> "Video chat started"
        is TdApi.MessageVideoChatEnded -> {
            val duration = (content as TdApi.MessageVideoChatEnded).duration
            val date = DateUtility.getDateFromUnix(duration)
            val formattedDate = DateUtility.getFormattedDate(date, "dd")
            "Video chat ended ($formattedDate)"
        }
        is TdApi.MessageInviteVideoChatParticipants -> {
            val userIds = (content as TdApi.MessageInviteVideoChatParticipants).userIds
            val users = userIds.toString().split(",").map { it.trim() }
            "$senderName invited ${users.joinToString(", ")} in to video chat"
        }
        is TdApi.MessageBasicGroupChatCreate -> {
            val title = (content as TdApi.MessageBasicGroupChatCreate).title
            "$senderName crated group $title"
        }
        is TdApi.MessageSupergroupChatCreate -> {
            val title = (content as TdApi.MessageSupergroupChatCreate).title
            "$senderName crated supergroup $title"
        }
        is TdApi.MessageChatChangeTitle -> {
            val title = (content as TdApi.MessageChatChangeTitle).title
            "$senderName changed group title to $title"
        }
        is TdApi.MessageChatDeletePhoto -> "$senderName deleted chat photo"
        is TdApi.MessageChatAddMembers -> {
            val userIds = (content as TdApi.MessageChatAddMembers).memberUserIds
            "$senderName added ${userIds.size} members"
        }
        is TdApi.MessageChatJoinByLink -> "$senderName joined group by link"
        is TdApi.MessageChatJoinByRequest -> "$senderName joined group by request"
        is TdApi.MessageChatDeleteMember -> {
            val userId = (content as TdApi.MessageChatDeleteMember).userId
            "$senderName removed $userId"
        }
        is TdApi.MessageChatUpgradeTo -> {
            val supergroupId = (content as TdApi.MessageChatUpgradeTo).supergroupId
            "$senderName upgraded to supergroup $supergroupId"
        }
        is TdApi.MessageChatUpgradeFrom -> {
            val upgradedFrom = (content as TdApi.MessageChatUpgradeFrom)
            "${upgradedFrom.title} upgraded to supergroup"
        }
        is TdApi.MessagePinMessage -> "$senderName pinned message"
        is TdApi.MessageScreenshotTaken -> "$senderName took screenshot"
        is TdApi.MessageChatSetBackground -> "$senderName set new background"
        is TdApi.MessageChatSetTheme -> {
            val theme = (content as TdApi.MessageChatSetTheme).themeName
            "$senderName set new theme" + if (theme.isNotEmpty()) " $theme" else ""
        }
        is TdApi.MessageChatSetMessageAutoDeleteTime -> {
            val autoDelete = content as TdApi.MessageChatSetMessageAutoDeleteTime
            val fromUser = if (autoDelete.fromUserId != 0L) "${autoDelete.fromUserId}" else senderName
            if (autoDelete.messageAutoDeleteTime != 0) {
                val date = DateUtility.getDateFromUnix(autoDelete.messageAutoDeleteTime)
                val formattedDate = DateUtility.getFormattedDate(date)
                "$fromUser set a self-destruct time for all chats. All new messages will be automatically deleted after $formattedDate they've been sent."
            } else {
                "$fromUser disabled the auto-delete timer"
            }
        }
        is TdApi.MessageChatBoost -> "$senderName boosted chat ${(content as TdApi.MessageChatBoost).boostCount} times."
        is TdApi.MessageForumTopicCreated -> "The topic \"${(content as TdApi.MessageForumTopicCreated).name}\" was created."
        is TdApi.MessageForumTopicEdited -> "$senderName edited the topic \"${(content as TdApi.MessageForumTopicEdited).name}\""
        is TdApi.MessageForumTopicIsClosedToggled -> {
            val isClosed = (content as TdApi.MessageForumTopicIsClosedToggled).isClosed
            if (isClosed) {
                "The topic was closed."
            } else {
                "The topic was opened."
            }
        }
        is TdApi.MessageForumTopicIsHiddenToggled -> {
            val isHidden = (content as TdApi.MessageForumTopicIsHiddenToggled).isHidden
            if (isHidden) {
                "The topic was hidden."
            } else {
                "The topic was shown."
            }
        }
        is TdApi.MessageSuggestProfilePhoto -> "$senderName suggested a profile photo"
        is TdApi.MessageCustomServiceAction -> "${(content as TdApi.MessageCustomServiceAction).text}"
        is TdApi.MessageGameScore -> "$senderName scored ${(content as TdApi.MessageGameScore).score} points in the game"
        is TdApi.MessagePaymentSuccessful -> "PaymentSuccessful"
        is TdApi.MessagePaymentSuccessfulBot -> "PaymentSuccessfulBot"
        is TdApi.MessagePaymentRefunded -> "PaymentRefunded"
        is TdApi.MessageGiftedPremium -> "$senderName GiftedPremium"
        is TdApi.MessagePremiumGiftCode -> "PremiumGiftCode"
        is TdApi.MessageGiveawayCreated -> "GiveawayCreated"
        is TdApi.MessageGiveaway -> "Giveaway"
        is TdApi.MessageGiveawayCompleted -> "GiveawayCompleted"
        is TdApi.MessageGiveawayWinners -> "GiveawayWinners"
        is TdApi.MessageGiftedStars -> "GiftedStars"
        is TdApi.MessageGiveawayPrizeStars -> "GiveawayPrizeStars"
        is TdApi.MessageGift -> "Gift"
        is TdApi.MessageUpgradedGift -> "UpgradedGift"
        is TdApi.MessageRefundedUpgradedGift -> "RefundedUpgradedGift"
        is TdApi.MessagePaidMessagesRefunded -> "PaidMessagesRefunded"
        is TdApi.MessagePaidMessagePriceChanged -> {
            val price = (content as TdApi.MessagePaidMessagePriceChanged).paidMessageStarCount
            if (price > 0) {
                "Messages now cost $price Stars each in this group."
            } else {
                "Messages are now free in this group."
            }
        }
        is TdApi.MessageDirectMessagePriceChanged -> {
            val priceChanged = (content as TdApi.MessageDirectMessagePriceChanged)
            if (priceChanged.isEnabled) {
                if (priceChanged.paidMessageStarCount > 0) {
                    "Channel allows Direct Messages for ${priceChanged.paidMessageStarCount} Stars for each."
                } else {
                    "Channel enabled Direct Messages."
                }
            } else {
                "Channel disabled Direct Messages."
            }
        }
        is TdApi.MessageContactRegistered -> "$senderName joined to Telegram!"
        is TdApi.MessageUsersShared -> "UsersShared"
        is TdApi.MessageChatShared -> "ChatShared"
        is TdApi.MessageBotWriteAccessAllowed -> "BotWriteAccessAllowed"
        is TdApi.MessageWebAppDataSent -> "🤖 WebApp data sent"
        is TdApi.MessageWebAppDataReceived -> "🤖 WebApp data received"
        is TdApi.MessagePassportDataSent -> "🤖 Passport data sent"
        is TdApi.MessagePassportDataReceived -> "🤖 Passport data received"
        is TdApi.MessageProximityAlertTriggered -> ""
        is TdApi.MessageUnsupported -> "⛄ Message unsupported"
        else -> "❓ Unsupported message content"
    }
}

fun TdApi.Message.getSenderId(): Long {
    return when (this.senderId) {
        is TdApi.MessageSenderChat -> (senderId as TdApi.MessageSenderChat).chatId
        is TdApi.MessageSenderUser -> (senderId as TdApi.MessageSenderUser).userId
        else -> chatId
    }
}

suspend fun TdApi.Message.getSenderName(): String {
    val instance = TdUtility.getInstance().getClient()
    return when (this.senderId) {
        is TdApi.MessageSenderChat -> {
            val id = (senderId as TdApi.MessageSenderChat).chatId
            val chat = instance.execute(TdApi.GetChat(id))
            if (chat.isChannel()) "" else chat.getChatTitle()
        }

        is TdApi.MessageSenderUser -> {
            val id = (senderId as TdApi.MessageSenderUser).userId
            val chat = instance.execute(TdApi.GetChat(chatId))

            if (id == UserConfig.getInstance().getCurrentUser()?.id) {
                "You"
            }
            else if (chat.type is TdApi.ChatTypePrivate || chat.type is TdApi.ChatTypeSecret) {
                ""
            } else {
                val user = instance.execute(TdApi.GetUser(id))
                user.firstName + (if (user.lastName.isNotEmpty()) " ${user.lastName}" else "")
            }
        }

        else -> "$chatId"
    }
}

fun TdApi.Message.getForumTopicId(): Long {
    return when (this.topicId) {
        is TdApi.MessageTopicForum -> (topicId as TdApi.MessageTopicForum).forumTopicId
        is TdApi.MessageTopicDirectMessages -> (topicId as TdApi.MessageTopicDirectMessages).directMessagesChatTopicId
        is TdApi.MessageTopicSavedMessages -> (topicId as TdApi.MessageTopicSavedMessages).savedMessagesTopicId
        else -> 0
    }
}

suspend fun TdApi.Message.getLastMessageText(): String {
    val instance = TdUtility.getInstance().getClient()
    val chat = instance.execute(TdApi.GetChat(this.chatId))
    val text = this.getTextMessageContent()
    val author = when (chat.type) {
        is TdApi.ChatTypeSupergroup -> {
            if (chat.isChannel()) {
                ""
            } else {
                this.getSenderName()
            }
        }
        is TdApi.ChatTypeSecret -> ""
        is TdApi.ChatTypePrivate -> ""
        else -> this.getSenderName()
    }

    return author + (if (author.isNotEmpty()) ": " else "") + text
}

suspend fun TdApi.Message.toTdMessage(): TdMessage {
    val realSenderId = getSenderId()
    val tdReply = replyTo?.toTdReply()
    val text = getTextMessageContent()
    val senderName = getSenderName()
    val sentDate = DateUtility.getDateFromUnix(date)
    val client = TdUtility.getInstance().getClient()
    val senderPhoto = when (senderId) {
        is TdApi.MessageSenderChat -> client.execute(TdApi.GetChat(realSenderId)).photo?.small
        is TdApi.MessageSenderUser -> client.execute(TdApi.GetUser(realSenderId)).profilePhoto?.small
        else -> null
    }

    return TdMessage(
        id = id,
        senderName = senderName,
        senderObject = senderId,
        content = content,
        sentDate = sentDate,
        text = text,
        reply = tdReply,
        isFromMe = realSenderId == UserConfig.getInstance().getClientUserId(),
        senderPhoto = senderPhoto,
        mediaAlbumId = mediaAlbumId
    )
}