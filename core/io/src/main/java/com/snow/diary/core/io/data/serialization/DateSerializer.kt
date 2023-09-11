package com.snow.diary.core.io.data.serialization;

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate

object DateSerializer : KSerializer<LocalDate> {

    override val descriptor = PrimitiveSerialDescriptor("ISO 8601", kind = PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate =
        try {
            LocalDate.parse(decoder.decodeString())
        } catch(_: Exception) {
            throw SerializationException()
        }

    override fun serialize(encoder: Encoder, value: LocalDate) = encoder
        .encodeString(value.toString())

}