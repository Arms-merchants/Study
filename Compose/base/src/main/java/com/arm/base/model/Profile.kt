package com.arm.base.model

import com.arm.base.utils.TimeUtil


/**
 * @Author: leavesC
 * @Date: 2021/6/22 17:05
 * @Desc:
 * @Github：https://github.com/leavesC
 */
/**
 * 基本资料
 */
open class BaseProfile(
    open val userId: String,
    open val faceUrl: String,
    open val nickname: String,
    open val remark: String,
    open val signature: String,
) {

    val showName: String
        get() {
            return remark.takeIf { it.isNotBlank() }
                ?: nickname.takeIf { it.isNotBlank() }
                ?: userId
        }

}

/**
 * 人的信息
 */
data class PersonProfile(
    override val userId: String,
    override val faceUrl: String,
    override val nickname: String,
    override val remark: String,
    override val signature: String,
    val isFriend: Boolean = false
) : BaseProfile(
    userId = userId,
    faceUrl = faceUrl,
    nickname = nickname,
    remark = remark,
    signature = signature
) {

    companion object {

        val Empty =
            PersonProfile(
                userId = "",
                faceUrl = "",
                nickname = "",
                remark = "",
                signature = "",
                isFriend = false
            )

    }

}

/**
 * 群组成员资料
 */
data class GroupMemberProfile(
    override val userId: String,
    override val faceUrl: String,
    override val nickname: String,
    override val remark: String,
    override val signature: String,
    val role: String,
    val isOwner: Boolean,
    val joinTime: Long
) : BaseProfile(
    userId = userId,
    faceUrl = faceUrl,
    nickname = nickname,
    remark = remark,
    signature = signature
) {

    val joinTimeFormat =
        TimeUtil.formatTime(time = joinTime * 1000, format = TimeUtil.YYYY_MM_DD_HH_MM_SS)

}

/**
 * 群信息
 */
data class GroupProfile(
    val id: String,
    val faceUrl: String,
    val name: String,
    val introduction: String,
    val createTime: Long,
    val memberCount: Int
) {

    companion object {

        val Empty = GroupProfile(
            id = "",
            faceUrl = "",
            name = "",
            introduction = "",
            createTime = 0L,
            memberCount = 0
        )

    }

    val createTimeFormat =
        TimeUtil.formatTime(time = createTime * 1000, format = TimeUtil.YYYY_MM_DD_HH_MM_SS)

}