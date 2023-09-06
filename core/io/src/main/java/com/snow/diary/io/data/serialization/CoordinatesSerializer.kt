package com.snow.diary.io.data.serialization

import com.snow.diary.model.data.Coordinates
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object CoordinatesSerializer: KSerializer<Coordinates> {

    override val descriptor = PrimitiveSerialDescriptor("Coordinates", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Coordinates = decoder
        .decodeString()
        .let { string ->
            try {
                val arr = string.split("|")
                Coordinates(
                    arr[0].toFloat(),
                    arr[1].toFloat()
                )
            } catch (_: Exception) {
                throw SerializationException()
            }
        }

    override fun serialize(encoder: Encoder, value: Coordinates) = encoder
        .encodeString("${value.x}|${value.y}")
}