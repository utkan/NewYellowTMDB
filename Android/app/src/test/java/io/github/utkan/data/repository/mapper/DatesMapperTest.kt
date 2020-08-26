package io.github.utkan.data.repository.mapper

import io.github.utkan.DataFactory
import io.github.utkan.data.repository.model.Dates
import org.junit.Test

import org.junit.Assert.*

class DatesMapperTest {

    @Test
    fun map() {
        // given
        val input = DataFactory.DATES_DTO
        val datesMapper = DatesMapper()

        // when
        val output = datesMapper.map(input)

        // then
        assertEquals(
            Dates(
                minimum = input.minimum,
                maximum = input.maximum,
            ), output
        )
    }
}
