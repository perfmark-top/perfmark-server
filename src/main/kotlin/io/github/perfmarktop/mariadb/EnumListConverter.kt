package io.github.perfmarktop.mariadb

import io.github.sgpublic.kotlin.core.util.fromGson
import io.github.sgpublic.kotlin.core.util.toGson
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

/**
 * @author Madray Haven
 * @Date 2023/6/20 下午5:30
 */
@Converter(autoApply = true)
class EnumListConverter: AttributeConverter<List<Enum<*>>, String> {
    override fun convertToDatabaseColumn(attribute: List<Enum<*>>?): String {
        return attribute?.toGson() ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<Enum<*>> {
        ArrayList<Enum<*>>().also {
            return javaClass.fromGson(dbData ?: return it)
        }
    }
}