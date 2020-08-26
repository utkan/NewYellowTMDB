package io.github.utkan.data.repository.mapper

import io.github.utkan.common.Mapper
import io.github.utkan.data.network.dto.DatesDto
import io.github.utkan.data.repository.model.Dates
import javax.inject.Inject

class DatesMapper @Inject constructor() : Mapper<DatesDto, Dates> {
    override fun map(input: DatesDto): Dates {
        return Dates(
            minimum = input.minimum,
            maximum = input.maximum,
        )
    }
}
