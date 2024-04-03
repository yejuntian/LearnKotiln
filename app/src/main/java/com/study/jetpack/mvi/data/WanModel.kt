package com.study.jetpack.mvi.data

data class WanModel(
    var desc: String,
    var imagePath: String,
    var title: String,
    var url: String,
)

data class RankModel(
    var curPage: Int,
    val datas: List<RankItem>,
)

data class RankItem(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: Int,
    val username: String,
)