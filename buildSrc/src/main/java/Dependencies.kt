import Versions.paging_version

object Versions {
    const val koin_version = "3.2.0"
    const val retrofit_version = "2.9.0"
    const val nav_version = "2.5.2"
    const val paging_version = "3.1.1"
    const val ktx_version = "2.5.1"
    const val room_version = "2.4.3"

}

object Koin {
    const val koin_core = "io.insert-koin:koin-core:${Versions.koin_version}"
    const val koin_android = "io.insert-koin:koin-android:${Versions.koin_version}"
    const val koin_androidx_navigation =
        "io.insert-koin:koin-androidx-navigation:${Versions.koin_version}"
}

object Retrofit {
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit_version}"
    const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:4.2.1"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit_version}"
}

object Navigation {
    const val navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.nav_version}"
    const val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.nav_version}"
}

object Paging {
    const val paging_runtime = "androidx.paging:paging-runtime:${Versions.paging_version}"
    const val paging_common = "androidx.paging:paging-common:${Versions.paging_version}"
}

object Android {
    const val core_ktx = "androidx.core:core-ktx:1.8.0"
    const val appcompat = "androidx.appcompat:appcompat:1.5.0"
    const val material = "com.google.android.material:material:1.6.1"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:1.5.2"
}

object TestDependencies {
    const val junit = "junit:junit:4.13.2"
    const val ext_junit = "androidx.test.ext:junit:1.1.3"
    const val espresso_core = "androidx.test.espresso:espresso-core:3.4.0"
}

object KtxDependencies {
    const val lifecycle_runtime_ktx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.ktx_version}"
    const val lifecycle_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ktx_version}"
}

object Room {
    const val room_runtime = "androidx.room:room-runtime:${Versions.room_version}"
    const val room_ktx = "androidx.room:room-ktx:${Versions.room_version}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room_version}"
    const val room_paging = "androidx.room:room-paging:${Versions.room_version}"
}