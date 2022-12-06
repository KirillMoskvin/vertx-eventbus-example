dependencies {
    implementation("io.vertx:vertx-hazelcast:4.3.5")
    implementation(project(":eventbus"))
    implementation(project(":shared"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("io.vertx:vertx-junit5:4.3.5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}