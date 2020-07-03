package com.badoo.ribs.example.login.mapper

import com.badoo.ribs.example.login.Login.Output
import com.badoo.ribs.example.login.feature.LoginFeature.News

internal object NewsToOutput : (News) -> Output? {

    override fun invoke(news: News): Output? =
        TODO("Implement LoginNewsToOutput mapping")
}
