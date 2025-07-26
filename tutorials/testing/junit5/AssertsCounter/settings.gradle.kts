rootProject.name = "asserts-counter"

include("ACAgent")
include("ACPlugin")

project(":ACAgent").name = "asserts-counter-agent"
project(":ACPlugin").name = "asserts-counter-plugin"