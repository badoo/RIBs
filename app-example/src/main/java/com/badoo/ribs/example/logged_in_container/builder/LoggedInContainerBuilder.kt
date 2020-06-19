package com.badoo.ribs.example.logged_in_container.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.example.logged_in_container.LoggedInContainer

class LoggedInContainerBuilder(
    private val dependency: LoggedInContainer.Dependency
) : SimpleBuilder<LoggedInContainer>() {

    override fun build(buildParams: BuildParams<Nothing?>): LoggedInContainer =
        DaggerLoggedInContainerComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = buildParams.getOrDefault(LoggedInContainer.Customisation()),
                buildParams = buildParams
            )
            .node()
}
