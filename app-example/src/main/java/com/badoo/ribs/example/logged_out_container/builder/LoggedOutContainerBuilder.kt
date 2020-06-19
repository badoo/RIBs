package com.badoo.ribs.example.logged_out_container.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.logged_out_container.LoggedOutContainer

class LoggedOutContainerBuilder(
    private val dependency: LoggedOutContainer.Dependency
) : SimpleBuilder<LoggedOutContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): LoggedOutContainer =
        DaggerLoggedOutContainerComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(LoggedOutContainer.Customisation()),
                buildParams = buildParams
            )
            .node()
}
