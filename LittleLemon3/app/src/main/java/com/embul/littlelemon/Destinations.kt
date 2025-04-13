package com.embul.littlelemon

interface Destinations {
    val route: String
    val title: String
}

object Home: Destinations {
    override val route = "Home"
    override val title = "Home"
}

object Profile: Destinations {
    override val route = "Profile"
    override val title = "Profile"
}

object Onboarding: Destinations {
    override val route = "Onboarding"
    override val title = "Onboarding"
}