package com.snow.diary.csv.util

import java.nio.charset.Charset
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun String.base64encode(
        charset: Charset = Charsets.UTF_8
): String = Base64
        .Mime
        .encode(toByteArray(charset))


@OptIn(ExperimentalEncodingApi::class)
fun String.base64decode(
        charset: Charset = Charsets.UTF_8
): String = Base64
        .Mime
        .decode(toByteArray(charset))
        .decodeToString()