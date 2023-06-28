package com.galacticstudio.digidoro.ui.screens.ranking.mapper

import com.galacticstudio.digidoro.data.UserRankingModel
import com.galacticstudio.digidoro.network.dto.ranking.UserInformationData

object UserRankingMapper {
    /**
     * Maps a single [UserInformationData] object to a [UserRankingModel] object.
     *
     * @param userInformationData The user information data to be mapped.
     * @return The mapped [UserRankingModel] object.
     */
    fun mapToUserRankingModel(userInformationData: UserInformationData): UserRankingModel {
        return UserRankingModel(
            id = userInformationData.id,
            firstname = userInformationData.firstname,
            username = userInformationData.username,
            profilePic = userInformationData.profilePic,
            level = getRankingName(userInformationData.totalScore),
            dailyScore = userInformationData.dailyScore,
            weeklyScore = userInformationData.weeklyScore,
            monthlyScore = userInformationData.monthlyScore,
            totalScore = userInformationData.totalScore
        )
    }

    /**
     * Maps a list of [UserInformationData] objects to a list of [UserRankingModel] objects.
     *
     * @param userInformationDataList The list of user information data to be mapped.
     * @return The list of mapped [UserRankingModel] objects.
     */
    fun mapToUserRankingModelList(userInformationDataList: List<UserInformationData>): List<UserRankingModel> {
        return userInformationDataList.map { userInformationData ->
            UserRankingModel(
                id = userInformationData.id,
                firstname = userInformationData.firstname,
                username = userInformationData.username,
                profilePic = userInformationData.profilePic,
                level = getRankingName(userInformationData.totalScore),
                dailyScore = userInformationData.dailyScore,
                weeklyScore = userInformationData.weeklyScore,
                monthlyScore = userInformationData.monthlyScore,
                totalScore = userInformationData.totalScore
            )
        }
    }

    /**
     * Returns the ranking name based on the given score.
     *
     * @param score The score to determine the ranking name.
     * @return The ranking name based on the given score.
     */
    fun getRankingName(score: Int): String {
        return when (score) {
            in 0 until 250 -> "Dreamer"
            in 250 until 750 -> "Thunder"
            in 750 until 1000 -> "Engineer"
            in 1000 until 1300 -> "Master"
            else -> "Legend"
        }
    }

    /**
     * Returns the score range based on the given ranking name.
     *
     * @param rankingName The ranking name to determine the score range.
     * @return The score range based on the given ranking name.
     */
    fun getScoreRange(rankingName: String): Int {
        return when (rankingName) {
            "Dreamer" -> 250
            "Thunder" -> 750
            "Engineer" -> 1000
            "Master" -> 1300
            else -> 0 //"Legend"
        }
    }
}
