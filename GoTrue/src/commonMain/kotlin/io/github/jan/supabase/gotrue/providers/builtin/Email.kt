package io.github.jan.supabase.gotrue.providers.builtin

import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.supabaseJson
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Authentication method with email and password
 */
object Email : DefaultAuthProvider<Email.Config, Email.Result> {

    @Serializable(with = DefaultAuthProvider.Config.Companion::class)
    data class Config(var email: String = "", var password: String = ""): DefaultAuthProvider.Config()
    @Serializable
    data class Result(
        val id: String,
        val email: String,
        @SerialName("confirmation_sent_at") val confirmationSentAt: Instant,
        @SerialName("created_at") val createdAt: Instant,
        @SerialName("updated_at") val updatedAt: Instant,
    )

    @OptIn(ExperimentalSerializationApi::class)
    override fun decodeResult(json: JsonObject): Result = try {
        supabaseJson.decodeFromJsonElement(json)
    } catch(e: MissingFieldException) {
        throw SupabaseEncodingException("Couldn't decode sign up email result. Input: $json")
    }

    override fun encodeCredentials(credentials: Config.() -> Unit): String = supabaseJson.encodeToString(Config().apply(credentials))

}