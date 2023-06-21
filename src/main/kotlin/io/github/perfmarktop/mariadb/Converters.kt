package io.github.perfmarktop.mariadb

import com.google.gson.JsonArray
import io.github.sgpublic.kotlin.core.util.fromGson
import io.github.sgpublic.kotlin.core.util.toGson
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.util.LinkedList
import java.util.StringJoiner

/**
 * @author Madray Haven
 * @Date 2023/6/20 下午5:30
 */

@Converter(autoApply = true)
class CpuCatConverter: AttributeConverter<List<CpuPerfMarkEntity.Cat>, String> {
    override fun convertToDatabaseColumn(attribute: List<CpuPerfMarkEntity.Cat>?): String {
        return StringJoiner(", ").also {
            attribute?.forEach { value ->
                it.add(value.name)
            }
        }.toString()
    }

    override fun convertToEntityAttribute(dbData: String?): List<CpuPerfMarkEntity.Cat> {
        return LinkedList<CpuPerfMarkEntity.Cat>().also {
            val values = dbData?.split(", ") ?: return@also
            for (value in values) {
                it.add(CpuPerfMarkEntity.Cat.valueOf(value))
            }
        }
    }
}

@Converter(autoApply = true)
class GpuCatConverter: AttributeConverter<List<GpuPerfMarkEntity.Cat>, String> {
    override fun convertToDatabaseColumn(attribute: List<GpuPerfMarkEntity.Cat>?): String {
        return StringJoiner(", ").also {
            attribute?.forEach { value ->
                it.add(value.name)
            }
        }.toString()
    }

    override fun convertToEntityAttribute(dbData: String?): List<GpuPerfMarkEntity.Cat> {
        return LinkedList<GpuPerfMarkEntity.Cat>().also {
            val values = dbData?.split(", ") ?: return@also
            for (value in values) {
                it.add(GpuPerfMarkEntity.Cat.valueOf(value))
            }
        }
    }
}

@Converter(autoApply = true)
class PubDateConverter: AttributeConverter<PubDate, String> {
    override fun convertToDatabaseColumn(attribute: PubDate?): String? {
        return attribute?.toString()
    }

    override fun convertToEntityAttribute(dbData: String?): PubDate? {
        return dbData?.asPubDate
    }
}