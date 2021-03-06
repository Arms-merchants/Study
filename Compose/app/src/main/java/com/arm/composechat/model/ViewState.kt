package com.arm.composechat.model

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.DrawerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import com.arm.base.model.*
import com.arm.composechat.extend.getIntArgument
import com.arm.composechat.extend.getStringArgument


/**
 * @Author: leavesC
 * @Date: 2021/7/6 15:17
 * @Desc:
 * @Github：https://github.com/leavesC
 */
sealed class Screen(val route: String) {

    private companion object {
        private const val loginScreen = "loginScreen"

        private const val homeScreen = "homeScreen"

        private const val friendProfileScreen = "friendProfileScreen"
        private const val keyFriendId = "keyFriendId"

        private const val groupProfileScreen = "groupProfileScreen"
        private const val keyGroupId = "keyGroupId"

        private const val chatScreen = "chatScreen"
        private const val keyChatScreenPartyType = "keyChatScreenPartyType"
        private const val keyChatScreenPartyId = "keyChatScreenPartyId"
    }

    object LoginScreen : Screen(route = loginScreen)

    object HomeScreen : Screen(route = homeScreen)

    object FriendProfileScreen : Screen(route = friendProfileScreen + "/{${keyFriendId}}") {

        fun generateRoute(friendId: String): String {
            return friendProfileScreen + "/${friendId}"
        }

        fun getArgument(entry: NavBackStackEntry): String {
            return entry.getStringArgument(key = keyFriendId)
        }

    }

    object GroupProfileScreen : Screen(route = groupProfileScreen + "/{${keyGroupId}}") {

        fun generateRoute(groupId: String): String {
            return groupProfileScreen + "/${groupId}"
        }

        fun getArgument(entry: NavBackStackEntry): String {
            return entry.getStringArgument(key = keyGroupId)
        }

    }

    object ChatScreen :
        Screen(route = chatScreen + "/{${keyChatScreenPartyType}}" + "/{${keyChatScreenPartyId}}") {

        fun generateRoute(chat: Chat): String {
            return chatScreen + "/${chat.type}" + "/${chat.id}"
        }

        fun getArgument(entry: NavBackStackEntry): Chat {
            val type = entry.getIntArgument(key = keyChatScreenPartyType)
            val id = entry.getStringArgument(key = keyChatScreenPartyId)
            return Chat.find(type = type, id = id)
        }

    }

}

data class LoginScreenState(
    val showLogo: Boolean,
    val showInput: Boolean,
    val showLoading: Boolean,
    val loginSuccess: Boolean,
    val lastLoginUserId: String
)

enum class HomeScreenTab(
    val icon: ImageVector
) {
    Conversation(
        icon = Icons.Filled.Favorite
    ),
    Friendship(
        icon = Icons.Filled.Album,
    ),
    Person(
        icon = Icons.Filled.WbSunny,
    );
}

data class HomeScreenDrawerState(
    val drawerState: DrawerState,
    val appTheme: AppTheme,
    val userProfile: PersonProfile,
    val switchToNextTheme: () -> Unit,
    val updateProfile: (faceUrl: String, nickname: String, signature: String) -> Unit,
    val logout: () -> Unit,
)

data class HomeScreenTopBarState(
    val screenSelected: HomeScreenTab,
    val openDrawer: () -> Unit,
    val onAddFriend: () -> Unit,
    val onJoinGroup: () -> Unit,
)

data class HomeScreenBottomBarState(
    val homeScreenList: List<HomeScreenTab>,
    val homeScreenSelected: HomeScreenTab,
    val unreadMessageCount: Long,
    val onHomeScreenTabSelected: (HomeScreenTab) -> Unit
)

data class HomeScreenSheetContentState @ExperimentalMaterialApi constructor(
    val modalBottomSheetState: ModalBottomSheetState,
    val toAddFriend: (userId: String) -> Unit,
    val toJoinGroup: (groupId: String) -> Unit
)

data class ConversationScreenState(
    val listState: LazyListState,
    val conversationList: List<Conversation>,
    val onClickConversation: (Conversation) -> Unit,
    val onDeleteConversation: (Conversation) -> Unit,
    val onPinnedConversation: (Conversation, Boolean) -> Unit
)

data class FriendshipScreenState(
    val listState: LazyListState,
    val joinedGroupList: List<GroupProfile>,
    val friendList: List<PersonProfile>,
    val onClickGroup: (GroupProfile) -> Unit,
    val onClickFriend: (PersonProfile) -> Unit
)

data class PersonProfileScreenState(
    val personProfile: PersonProfile
)

data class ChatScreenState(
    val messageList: List<Message>,
    val mushScrollToBottom: Boolean,
    val showLoadMore: Boolean,
    val loadFinish: Boolean,
)

data class GroupProfileScreenState(
    val groupProfile: GroupProfile,
    val memberList: List<GroupMemberProfile>
)